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

package com.sun.faces.facelets.tag.ui;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.facelets.FaceletContextImplBase;
import com.sun.faces.facelets.TemplateClient;
import com.sun.faces.facelets.el.VariableMapperWrapper;
import com.sun.faces.facelets.tag.TagHandlerImpl;
import com.sun.faces.util.FacesLogger;

import javax.el.VariableMapper;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;

/**
 * @author Jacob Hookom
 */
public final class CompositionHandler extends TagHandlerImpl implements
        TemplateClient {

    private static final Logger log = FacesLogger.FACELETS_COMPOSITION.getLogger();


    public final static String Name = "composition";

    protected final TagAttribute template;

    protected final Map handlers;

    protected final ParamHandler[] params;

    /**
     * @param config
     */
    public CompositionHandler(TagConfig config) {
        super(config);
        this.template = this.getAttribute("template");
        if (this.template != null) {
            this.handlers = new HashMap();
            Iterator itr = this.findNextByType(DefineHandler.class);
            DefineHandler d = null;
            while (itr.hasNext()) {
                d = (DefineHandler) itr.next();
                this.handlers.put(d.getName(), d);
                if (log.isLoggable(Level.FINE)) {
                    log.fine(tag + " found Define[" + d.getName() + "]");
                }
            }
            List paramC = new ArrayList();
            itr = this.findNextByType(ParamHandler.class);
            while (itr.hasNext()) {
                paramC.add(itr.next());
            }
            if (paramC.size() > 0) {
                this.params = new ParamHandler[paramC.size()];
                for (int i = 0; i < this.params.length; i++) {
                    this.params[i] = (ParamHandler) paramC.get(i);
                }
            } else {
                this.params = null;
            }
        } else {
            this.params = null;
            this.handlers = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext,
     *      javax.faces.component.UIComponent)
     */
    public void apply(FaceletContext ctxObj, UIComponent parent)
            throws IOException {
        FaceletContextImplBase ctx = (FaceletContextImplBase) ctxObj;

        if (this.template != null) {

            FacesContext facesContext = ctx.getFacesContext();
            Integer compositionCount = (Integer) facesContext.getAttributes().get("com.sun.faces.uiCompositionCount");
            if (compositionCount == null) {
                compositionCount = 1;
            } else {
                compositionCount++;
            }
            facesContext.getAttributes().put("com.sun.faces.uiCompositionCount", compositionCount);
            
            VariableMapper orig = ctx.getVariableMapper();
            if (this.params != null) {
                VariableMapper vm = new VariableMapperWrapper(orig);
                ctx.setVariableMapper(vm);
                for (int i = 0; i < this.params.length; i++) {
                    this.params[i].apply(ctx, parent);
                }
            }

            ctx.extendClient(this);
            String path = null;
            try {
                path = this.template.getValue(ctx);
                if (path.trim().length() == 0) {
                    throw new TagAttributeException(this.tag, this.template, "Invalid path : " + path);
                }
                WebConfiguration webConfig = WebConfiguration.getInstance(); 
                if (path.startsWith(webConfig.getOptionValue(WebConfiguration.WebContextInitParameter.WebAppContractsDirectory))) {
                    throw new TagAttributeException(this.tag, this.template, "Invalid path, contract resources cannot be accessed this way : " + path);
                }
                ctx.includeFacelet(parent, path);
            } catch (IOException e) {
                if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, e.toString(), e);
                }
                throw new TagAttributeException(this.tag, this.template, "Invalid path : " + path);
            } finally {
                ctx.popClient(this);
                ctx.setVariableMapper(orig);
                
                compositionCount = (Integer) facesContext.getAttributes().get("com.sun.faces.uiCompositionCount");
                compositionCount--;
                
                if (compositionCount == 0) {
                    facesContext.getAttributes().remove("com.sun.faces.uiCompositionCount");
                } else {
                    facesContext.getAttributes().put("com.sun.faces.uiCompositionCount", compositionCount);
                }
            }
        } else {
            this.nextHandler.apply(ctx, parent);
        }
    }

    public boolean apply(FaceletContext ctx, UIComponent parent, String name)
          throws IOException {
        if (name != null) {
            if (this.handlers == null) {
                return false;
            }
            DefineHandler handler = (DefineHandler) this.handlers.get(name);
            if (handler != null) {
                handler.applyDefinition(ctx, parent);
                return true;
            } else {
                return false;
            }
        } else {
            this.nextHandler.apply(ctx, parent);
            return true;
        }
    }

}
