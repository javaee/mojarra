/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.facelets.tag.composite.CompositeLibrary;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableMissingResourceLibraryDetection;
import com.sun.faces.util.FacesLogger;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CompositeComponentTagLibrary extends LazyTagLibrary {
    
    private static final Logger LOGGER = FacesLogger.FACELETS_COMPONENT.getLogger();

    public CompositeComponentTagLibrary(String ns) {
        super(ns);
        if (null == ns) {
            throw new NullPointerException();
        }
        this.ns = ns;
        this.init();
    }
    
    public CompositeComponentTagLibrary(String ns, String compositeLibraryName) {
        super(ns);
        if (null == ns) {
            throw new NullPointerException();
        }
        this.ns = ns;
        if (null == compositeLibraryName) {
            throw new NullPointerException();
        }
        this.compositeLibraryName = compositeLibraryName;
        this.init();
        
    }

    private void init() {
        WebConfiguration webconfig = WebConfiguration.getInstance();
        enableMissingResourceLibraryDetection =
                webconfig.isOptionEnabled(EnableMissingResourceLibraryDetection);
    }
    
    private String ns = null;
    private String compositeLibraryName;
    private boolean enableMissingResourceLibraryDetection;

    public boolean containsTagHandler(String ns, String localName) {
        boolean result = false;

        Resource ccResource = null;
                        
        if (null != (ccResource = 
                getCompositeComponentResource(ns, localName))) {
            InputStream componentStream = null;
            try {
                componentStream = ccResource.getInputStream();
                result = (componentStream != null);
            } catch (IOException ex) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, ex.toString(), ex);
                }
            } finally {
                try {
                    if (result) {
                        componentStream.close();
                    }
                } catch (IOException ex) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, ex.toString(), ex);
                    }
                } 
            }
        }
        return result || super.containsTagHandler(ns, localName);
    }
    
    private Resource getCompositeComponentResource(String ns, String localName) {
        Resource ccResource = null;
        if (ns.equals(this.ns)) {
            FacesContext context = FacesContext.getCurrentInstance();
            String libraryName = getCompositeComponentLibraryName(this.ns);
            if (null != libraryName) {
                String ccName = localName + ".xhtml";
                // PENDING: there has to be a cheaper way to test for existence
                ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
                ccResource = resourceHandler.
                        createResource(ccName, libraryName);
            }
        }
        return ccResource;
    }


    public TagHandler createTagHandler(String ns, String localName, TagConfig tag) throws FacesException {

        TagHandler result = super.createTagHandler(ns, localName, tag);

        if (result == null) {
            ComponentConfig componentConfig =
                  new ComponentConfigWrapper(tag, CompositeComponentImpl.TYPE, null);
            result = new CompositeComponentTagHandler(
                  getCompositeComponentResource(ns, localName),
                  componentConfig);
        }

        return result;
    }
    
    private static final String NS_COMPOSITE_COMPONENT_PREFIX = 
            CompositeLibrary.Namespace + "/";
    private static final String XMLNS_COMPOSITE_COMPONENT_PREFIX = 
            CompositeLibrary.XMLNSNamespace + "/";
    
    @Override
    public boolean tagLibraryForNSExists(String toTest) {
        boolean result = false;
        
        String resourceId = null;
        if (null != (resourceId = getCompositeComponentLibraryName(toTest))) {
            if (enableMissingResourceLibraryDetection) {
                result = FacesContext.getCurrentInstance().getApplication().
                        getResourceHandler().libraryExists(resourceId);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Skipping call to libraryExists().  Please set context-param {0} to true to verify if library {1} actually exists", new Object[]{EnableMissingResourceLibraryDetection.getQualifiedName(), toTest});
                }
                result = true;
            }
        }
        
        return result;
    }
    
    public static boolean scriptComponentForResourceExists(FacesContext context,
            Resource componentResource) {
        boolean result = false;

        Resource scriptComponentResource = context.getApplication().getViewHandler().getViewDeclarationLanguage(context, context.getViewRoot().getViewId()).getScriptComponentResource(context, 
                componentResource);
        InputStream is = null;
        try {
            is = scriptComponentResource.getInputStream();
            result = (null != scriptComponentResource) && (null != is);
        } catch (IOException ex) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, ex.toString(), ex);
            }
        } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (IOException ex) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, ex.toString(), ex);
                    }
                } 
            }
        
        return result;
    }
    
    private String getCompositeComponentLibraryName(String toTest) {
        String resourceId = null;
        if (null != compositeLibraryName) {
            resourceId = compositeLibraryName;
        }
        else {
            int resourceIdIndex;
            if (-1 != (resourceIdIndex = toTest.indexOf(NS_COMPOSITE_COMPONENT_PREFIX))) {
                resourceIdIndex += NS_COMPOSITE_COMPONENT_PREFIX.length();
                if (resourceIdIndex < toTest.length()) {
                    resourceId = toTest.substring(resourceIdIndex);
                }
            }
            if (-1 != (resourceIdIndex = toTest.indexOf(XMLNS_COMPOSITE_COMPONENT_PREFIX))) {
                resourceIdIndex += XMLNS_COMPOSITE_COMPONENT_PREFIX.length();
                if (resourceIdIndex < toTest.length()) {
                    resourceId = toTest.substring(resourceIdIndex);
                }
            }
        }
        
        return resourceId;
    }

}
