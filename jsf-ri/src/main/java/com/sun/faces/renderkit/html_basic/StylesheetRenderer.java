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
 */

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.config.WebConfiguration;
import java.io.IOException;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * <p>This <code>Renderer</code> handles the rendering of external <code>stylesheet</code>
 * references.</p>
 */
public class StylesheetRenderer extends ScriptStyleBaseRenderer {


    @Override
    protected void startElement(ResponseWriter writer, UIComponent component) throws IOException {
        writer.startElement("style", component);
        writer.writeAttribute("type", "text/css", "type");
    }
    
    @Override
    protected void endElement(ResponseWriter writer) throws IOException {
        writer.endElement("style");
    }

    @Override
    protected String verifyTarget(String toVerify) {
        return "head";
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        Map<String,Object> attributes = component.getAttributes();
        Map<Object, Object> contextMap = context.getAttributes();

        String name = (String) attributes.get("name");
        String library = (String) attributes.get("library");
        String key = name + library;

        String media = (String) attributes.get("media");
        
        if (null == name) {
            return;
        }
        
        // Ensure this stylesheet is not rendered more than once per request
        if (contextMap.containsKey(key)) {
            return;
        }
        contextMap.put(key, Boolean.TRUE);
        
        Resource resource = context.getApplication().getResourceHandler()
              .createResource(name, library);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("link", component);
        writer.writeAttribute("type", "text/css", "type");
        writer.writeAttribute("rel", "stylesheet", "rel");

        String resourceUrl = "RES_NOT_FOUND";

        WebConfiguration webConfig = WebConfiguration.getInstance();

        if (library == null 
                && name.startsWith(webConfig.getOptionValue(WebConfiguration.WebContextInitParameter.WebAppContractsDirectory))
                && context.isProjectStage(ProjectStage.Development)) {
            String msg = "Illegal path, direct contract references are not allowed: " + name;
            context.addMessage(component.getClientId(context),
                               new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                msg,
                                                msg));                
            resource = null;
        }
        
        if (resource != null) {
        	resourceUrl = context.getExternalContext().encodeResourceURL(resource.getRequestPath());
        } else if (context.isProjectStage(ProjectStage.Development)) {
            String msg = "Unable to find resource " + (library == null ? "" : library + ", ") + name;
            context.addMessage(component.getClientId(context),
                               new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                msg,
                                                msg));
        }
        
        writer.writeURIAttribute("href", resourceUrl, "href");
        if (media != null) {
            writer.writeAttribute("media", media, "media");
        }
        writer.endElement("link");
        super.encodeEnd(context, component);
    }
}
