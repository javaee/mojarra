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


import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class PassthroughRenderer extends HtmlBasicRenderer {

// We are purposely piggy backing off the PANELGROUP attributes since they are 
// identical for this renderer.
private static final Attribute[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.PANELGROUP);

    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        Map<String, Object> attrs = component.getPassThroughAttributes();
        String localName = (String) attrs.get(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY);
        if (null == localName) {
            String clientId = component.getClientId(context);
            throw new FacesException("Unable to determine localName for component with clientId " + clientId);
        }

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(localName, component);

        writeIdAttributeIfNecessary(context, writer, component);
        
        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                component,
                                                ATTRIBUTES);

    }


    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncodeChildren(component)) {
            return;
        }

        // Render our children recursively
        Iterator<UIComponent> kids = getChildren(component);
        while (kids.hasNext()) {
            encodeRecursive(context, kids.next());
        }

    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        Map<String, Object> attrs = component.getPassThroughAttributes();
        String localName = (String) attrs.get(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY);
        context.getResponseWriter().endElement(localName);
    }


    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
