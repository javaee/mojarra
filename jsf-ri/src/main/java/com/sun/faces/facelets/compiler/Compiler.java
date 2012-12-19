/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
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

import com.sun.faces.facelets.tag.CompositeTagDecorator;
import com.sun.faces.facelets.tag.CompositeTagLibrary;
import com.sun.faces.facelets.tag.TagLibrary;
import com.sun.faces.facelets.util.ReflectionUtil;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.TagDecorator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Compiler instance may handle compiling multiple sources
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public abstract class Compiler {

    protected final static Logger log = FacesLogger.FACELETS_COMPILER.getLogger();

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


    /**
     * 
     */
    public Compiler() {
        
    }

    public final FaceletHandler compile(URL src, String alias)
    throws IOException {
        //if (!this.initialized)
        //    this.initialize();
        return this.doCompile(src, alias);
    }

    public final FaceletHandler metadataCompile(URL src, String alias)
    throws IOException {

        return this.doMetadataCompile(src, alias);
    }

    protected abstract FaceletHandler doMetadataCompile(URL src, String alias)
    throws IOException;

    protected abstract FaceletHandler doCompile(URL src, String alias)
    throws IOException;

    public final TagDecorator createTagDecorator() {
        if (this.decorators.size() > 0) {
            return new CompositeTagDecorator((TagDecorator[]) this.decorators
                    .toArray(new TagDecorator[this.decorators.size()]));
        }
        return EMPTY_DECORATOR;
    }

    public final void addTagDecorator(TagDecorator decorator) {
        Util.notNull("decorator", decorator);
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
                	if (log.isLoggable(Level.WARNING)) {
	                    log.warning("No default ExpressionFactory from Faces Implementation, attempting to load from Feature["
	                                + EXPRESSION_FACTORY + "]");
                	}
                }
            } catch (Exception e) {
                if (log.isLoggable(Level.FINEST)) {
                    log.log(Level.FINEST, "Unable to get ExpressionFactory because of: ", e);
                }
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

    public final TagLibrary createTagLibrary(CompilationMessageHolder unit) {
        if (this.libraries.size() > 0) {
            return new CompositeTagLibrary((TagLibrary[]) this.libraries
                    .toArray(new TagLibrary[this.libraries.size()]), unit);
        }
        return EMPTY_LIBRARY;
    }

    public final void addTagLibrary(TagLibrary library) {
        Util.notNull("library", library);
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
