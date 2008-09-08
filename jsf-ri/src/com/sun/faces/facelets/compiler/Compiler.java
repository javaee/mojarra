/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.compiler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import com.sun.faces.facelets.FaceletException;
import com.sun.faces.facelets.FaceletHandler;
import com.sun.faces.facelets.tag.CompositeTagDecorator;
import com.sun.faces.facelets.tag.CompositeTagLibrary;
import com.sun.faces.facelets.tag.TagDecorator;
import com.sun.faces.facelets.tag.TagLibrary;
import com.sun.faces.facelets.tag.ui.UILibrary;
import com.sun.faces.facelets.util.ParameterCheck;
import com.sun.faces.facelets.util.ReflectionUtil;

/**
 * A Compiler instance may handle compiling multiple sources
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public abstract class Compiler {

    protected final static Logger log = Logger.getLogger("facelets.compiler");

    public final static String EXPRESSION_FACTORY = "compiler.ExpressionFactory";

    private static final TagLibrary EMPTY_LIBRARY = new CompositeTagLibrary(
            new TagLibrary[0]);

    private static final TagDecorator EMPTY_DECORATOR = new CompositeTagDecorator(
            new TagDecorator[0]);

    private boolean validating = false;

    private boolean trimmingWhitespace = false;

    private boolean trimmingComments = false;

    private final List libraries = new ArrayList();

    private final List decorators = new ArrayList();

    private final Map features = new HashMap();

    private boolean initialized = false;

    /**
     * 
     */
    public Compiler() {
        
    }

    private synchronized void initialize() {
        if (this.initialized)
            return;
        log.fine("Initializing");
        try {
            TagLibraryConfig cfg = new TagLibraryConfig();
            cfg.loadMojarraImplicit(this);
            cfg.loadImplicit(this);
            
            if (!this.createTagLibrary().containsNamespace(UILibrary.Namespace)) {
                log.severe("Missing Built-in Tag Libraries! Make sure they are included within the META-INF directory of Facelets' Jar");
            }
            
        } catch (IOException e) {
            log.log(Level.SEVERE, "Compiler Initialization Error", e);
        } finally {
            this.initialized = true;
        }
        log.fine("Initialization Successful");
    }

    public final FaceletHandler compile(URL src, String alias)
            throws IOException, FaceletException, ELException, FacesException {
        //if (!this.initialized)
        //    this.initialize();
        return this.doCompile(src, alias);
    }

    protected abstract FaceletHandler doCompile(URL src, String alias)
            throws IOException, FaceletException, ELException, FacesException;

    public final TagDecorator createTagDecorator() {
        if (this.decorators.size() > 0) {
            return new CompositeTagDecorator((TagDecorator[]) this.decorators
                    .toArray(new TagDecorator[this.decorators.size()]));
        }
        return EMPTY_DECORATOR;
    }

    public final void addTagDecorator(TagDecorator decorator) {
        ParameterCheck.notNull("decorator", decorator);
        if (!this.decorators.contains(decorator)) {
            this.decorators.add(decorator);
        }
    }

    public final ExpressionFactory createExpressionFactory() {
        ExpressionFactory el = null;
        el = (ExpressionFactory) this.featureInstance(EXPRESSION_FACTORY);
        if (el == null) {
            try {
                el = FacesContext.getCurrentInstance().getApplication()
                        .getExpressionFactory();
                if (el == null) {
                    log.warning("No default ExpressionFactory from Faces Implementation, attempting to load from Feature["
                                + EXPRESSION_FACTORY + "]");
                }
            } catch (Exception e) {
                // do nothing
            }
        }
        if (el == null) {
            this.features.put(EXPRESSION_FACTORY, "com.sun.el.ExpressionFactoryImpl");
            el = (ExpressionFactory) this.featureInstance(EXPRESSION_FACTORY);
        }
        return el;
    }

    private final Object featureInstance(String name) {
        String type = (String) this.features.get(name);
        if (type != null) {
            try {
                return ReflectionUtil.forName(type).newInstance();
            } catch (Throwable t) {
                throw new FaceletException("Could not instantiate feature["
                        + name + "]: " + type);
            }
        }
        return null;
    }

    public final TagLibrary createTagLibrary() {
        if (this.libraries.size() > 0) {
            return new CompositeTagLibrary((TagLibrary[]) this.libraries
                    .toArray(new TagLibrary[this.libraries.size()]));
        }
        return EMPTY_LIBRARY;
    }

    public final void addTagLibrary(TagLibrary library) {
        ParameterCheck.notNull("library", library);
        if (!this.libraries.contains(library)) {
            this.libraries.add(library);
        }
    }

    public final void setFeature(String name, String value) {
        this.features.put(name, value);
    }

    public final String getFeature(String name) {
        return (String) this.features.get(name);
    }

    public final boolean isTrimmingComments() {
        return this.trimmingComments;
    }

    public final void setTrimmingComments(boolean trimmingComments) {
        this.trimmingComments = trimmingComments;
    }

    public final boolean isTrimmingWhitespace() {
        return this.trimmingWhitespace;
    }

    public final void setTrimmingWhitespace(boolean trimmingWhitespace) {
        this.trimmingWhitespace = trimmingWhitespace;
    }

    public final boolean isValidating() {
        return this.validating;
    }

    public final void setValidating(boolean validating) {
        this.validating = validating;
    }
}
