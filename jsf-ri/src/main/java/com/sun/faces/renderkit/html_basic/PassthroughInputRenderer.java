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

 */
package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.FacesLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class PassthroughInputRenderer extends HtmlBasicInputRenderer {
    
    private static final Logger LOGGER = FacesLogger.RENDERKIT.getLogger();
    
    private Map<String, PassthroughElementInfo> localNameToSubmittedValueAttributeName;
    
    public PassthroughInputRenderer() {
        localNameToSubmittedValueAttributeName = new HashMap<String,PassthroughElementInfo>();
        localNameToSubmittedValueAttributeName.put("keygen", new PassthroughElementInfo("name", false));
    }
    
    private static class PassthroughElementInfo {
        
        private boolean renderValueAttribute;
        private String submittedValueAttributeName;

        public PassthroughElementInfo(String submittedValueAttributeName, boolean renderValueAttribute) {
            this.renderValueAttribute = renderValueAttribute;
            this.submittedValueAttributeName = submittedValueAttributeName;
        }
        
        
        
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (!(component instanceof UIInput)) {
            // decode needs to be invoked only for components that are
            // instances or subclasses of UIInput.
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                           "No decoding necessary since the component {0} is not an instance or a sub class of UIInput",
                           component.getId());
            }
            return;
        }

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        assert(clientId != null);
        Map<String, String> requestMap =
              context.getExternalContext().getRequestParameterMap();
        
        Map<String, Object> attrs = component.getPassThroughAttributes();
        String localName = (String) attrs.get(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY);
        assert(null != localName);
        if (null == localName) {
            throw new FacesException("Unable to determine localName for component with clientId " + clientId);
        }
        PassthroughElementInfo info = localNameToSubmittedValueAttributeName.get(localName);

        if (null != info) {
            String submittedValueAttrName = info.submittedValueAttributeName;
            String submittedValueAttributeValue = (String) attrs.get(submittedValueAttrName);
            if (null != submittedValueAttributeValue) {
                String newValue = requestMap.get(submittedValueAttributeValue);
                if (newValue != null) {
                    setSubmittedValue(component, newValue);
                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE,
                                "new value after decoding {0}",
                                newValue);
                    }
                }
            }
            
        }
        
    }
    
    

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        LOGGER.info("encodeBegin called");
        Map<String, Object> attrs = component.getPassThroughAttributes();
        String localName = (String) attrs.get(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(localName, component);

        writeIdAttributeIfNecessary(context, writer, component);
        
        writer.writeAttribute("name", (component.getClientId(context)),
                "clientId");

        boolean doWriteValue = true;
        PassthroughElementInfo info = localNameToSubmittedValueAttributeName.get(localName);
        
        if (null != info) {
            doWriteValue = info.renderValueAttribute;
        }
        
        if (doWriteValue) {

            String currentValue = getCurrentValue(context, component);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                        "Value to be rendered {0}",
                        currentValue);
            }
            if (currentValue != null) {
                writer.writeAttribute("value", currentValue, "value");
            }
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        LOGGER.info("encodeChildren called");
        super.encodeChildren(context, component);
    }
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {
        LOGGER.info("encodeEnd called");
        Map<String, Object> attrs = component.getPassThroughAttributes();
        String localName = (String) attrs.get(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY);
        context.getResponseWriter().endElement(localName);
    }
    
    
    
}
