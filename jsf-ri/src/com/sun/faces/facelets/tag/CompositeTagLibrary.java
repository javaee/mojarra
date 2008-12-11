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

package com.sun.faces.facelets.tag;

import javax.faces.webapp.pdl.facelets.tag.TagHandler;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import com.sun.faces.facelets.tag.jsf.CompositeComponentTagLibrary;
import java.lang.reflect.Method;

import javax.faces.FacesException;

import com.sun.faces.util.Util;

import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

/**
 * A TagLibrary that is composed of 1 or more TagLibrary children. Uses the
 * chain of responsibility pattern to stop searching as soon as one of the
 * children handles the requested method.
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public final class CompositeTagLibrary implements TagLibrary {

    private TagLibrary[] libraries;

    public CompositeTagLibrary(TagLibrary[] libraries) {
        Util.notNull("libraries", libraries);
        this.libraries = libraries;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.tag.TagLibrary#containsNamespace(java.lang.String)
     */
    public boolean containsNamespace(String ns) {
        boolean result = true;
        for (int i = 0; i < this.libraries.length; i++) {
            if (this.libraries[i].containsNamespace(ns)) {
                return true;
            }
        }
        // PENDING: this is a terribly inefficient impl.  Needs refactoring.
        CompositeComponentTagLibrary toTest = new CompositeComponentTagLibrary(ns);
        if (toTest.tagLibraryForNSExists(ns)) {
            TagLibrary [] librariesPlusOne = new TagLibrary[libraries.length+1];
            System.arraycopy(this.libraries, 0, librariesPlusOne, 
                    0, libraries.length);
            librariesPlusOne[libraries.length] = 
                    new CompositeComponentTagLibrary(ns);
            for (int i = 0; i < this.libraries.length; i++) {
                libraries[i] = null;
            }
            libraries = librariesPlusOne;
            return true;
        }
        else {
            FacesContext context = FacesContext.getCurrentInstance();
            if (ProjectStage.Development == context.getApplication().getProjectStage()) {
                if (!ns.equals("http://www.w3.org/1999/xhtml")) {
                    if (!getNamespaceMessageMap(context).containsKey(ns)) {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Warning: This page calls for XML namespace " + ns +
                                " but no taglibrary exists for that namespace.", ""));
                        getNamespaceMessageMap(context).put(ns, Boolean.TRUE);
                    }
                }
            }
        }
        return false;
    }
    
    private Map<String,Boolean> getNamespaceMessageMap(FacesContext context) {
        Map<String, Boolean> result = null;
        
        if (null == (result = (Map<String,Boolean>)
            context.getAttributes().get("facelets.namespaceMessageMap"))) {
            result = new HashMap<String,Boolean>();
            context.getAttributes().put("facelets.namespaceMessageMap", result);
        }
        
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.tag.TagLibrary#containsTagHandler(java.lang.String,
     *      java.lang.String)
     */
    public boolean containsTagHandler(String ns, String localName) {
        for (int i = 0; i < this.libraries.length; i++) {
            if (this.libraries[i].containsTagHandler(ns, localName)) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.tag.TagLibrary#createTagHandler(java.lang.String,
     *      java.lang.String, com.sun.facelets.tag.TagConfig)
     */
    public TagHandler createTagHandler(String ns, String localName,
            TagConfig tag) throws FacesException {
        for (int i = 0; i < this.libraries.length; i++) {
            if (this.libraries[i].containsTagHandler(ns, localName)) {
                return this.libraries[i].createTagHandler(ns, localName, tag);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.tag.TagLibrary#containsFunction(java.lang.String,
     *      java.lang.String)
     */
    public boolean containsFunction(String ns, String name) {
        for (int i = 0; i < this.libraries.length; i++) {
            if (this.libraries[i].containsFunction(ns, name)) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.tag.TagLibrary#createFunction(java.lang.String,
     *      java.lang.String)
     */
    public Method createFunction(String ns, String name) {
        for (int i = 0; i < this.libraries.length; i++) {
            if (this.libraries[i].containsFunction(ns, name)) {
                return this.libraries[i].createFunction(ns, name);
            }
        }
        return null;
    }
}
