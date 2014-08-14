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

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import java.io.IOException;
import javax.faces.application.NavigationCase;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class OutcomeTargetButtonRenderer extends OutcomeTargetRenderer {

    private static final Attribute[] ATTRIBUTES =
        AttributeManager.getAttributes(AttributeManager.Key.COMMANDBUTTON);


    // --------------------------------------------------- Methods from Renderer


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {

        rendererParamsNotNull(context, component);
        if (!shouldEncode(component)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);

        String imageSrc = (String) component.getAttributes().get("image");
        if (imageSrc != null) {
            writer.writeAttribute("type", "image", "type");
            writer.writeURIAttribute("src", RenderKitUtils.getImageSource(context, component, "image"), "image");
        }
        else {
            writer.writeAttribute("type", "button", "type");
        }

        String label = getLabel(component);

        if (!Util.componentIsDisabled(component)) {
            NavigationCase navCase = getNavigationCase(context, component);

            if (navCase == null) {
                // QUESTION should this only be added in development mode?
                label += MessageUtils.getExceptionMessageString(MessageUtils.OUTCOME_TARGET_BUTTON_NO_MATCH);
                writer.writeAttribute("disabled", "true", "disabled");
            }
            else {
                String hrefVal = getEncodedTargetURL(context, component, navCase);
                hrefVal += getFragment(component);
                writer.writeAttribute("onclick", getOnclick(component, hrefVal), "onclick");
            }
        }

        // value should be used even for image type for accessibility (e.g., images disabled in browser)
        writer.writeAttribute("value", label, "value");

        String styleClass = (String) component.getAttributes().get("styleClass");
        if (styleClass != null && styleClass.length() > 0) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        renderPassThruAttributes(context, writer, component, ATTRIBUTES, null);

        if(component.getChildCount() == 0) {
            writer.endElement("input");
        }

    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {

        rendererParamsNotNull(context, component);

        if(component.getChildCount() > 0) {
            context.getResponseWriter().endElement("input");
        }

    }


    // ------------------------------------------------------- Protected Methods


    protected String getOnclick(UIComponent component, String targetURI) {

        String onclick = (String) component.getAttributes().get("onclick");

        if (onclick != null) {
            onclick = onclick.trim();
            if (onclick.length() > 0 && !onclick.endsWith(";")) {
                onclick += "; ";
            }
        }
        else {
            onclick = "";
        }

        if (targetURI != null) {
            onclick += "window.location.href='" + targetURI + "'; ";
        }

        onclick += "return false;";

        return onclick;
        
    }

}
