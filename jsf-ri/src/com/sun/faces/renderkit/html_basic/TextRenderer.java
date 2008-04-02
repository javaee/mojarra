/*
 * $Id: TextRenderer.java,v 1.73 2006/03/29 22:38:39 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// TextRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;

/**
 * <B>TextRenderer</B> is a class that renders the current value of
 * <code>UIInput<code> or <code>UIOutput<code> component as a input field or
 * static text.
 */
public class TextRenderer extends HtmlBasicInputRenderer {

    // ------------------------------------------------------------ Constructors


    public TextRenderer() {

        super();

    }

    // ---------------------------------------------------------- Public Methods


    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

    }

    // ------------------------------------------------------- Protected Methods


    protected void getEndTextToRender(FacesContext context,
                                      UIComponent component,
                                      String currentValue)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        boolean
              shouldWriteIdAttribute = false,
              isOutput = false;

        String
              style = (String) component.getAttributes().get("style"),
              styleClass = (String) component.getAttributes().get("styleClass");
        if (component instanceof UIInput) {
            writer.startElement("input", component);
            writeIdAttributeIfNecessary(context, writer, component);
            writer.writeAttribute("type", "text", null);
            writer.writeAttribute("name", (component.getClientId(context)),
                                  "clientId");

            String autoComplete = (String)
                  component.getAttributes().get("autocomplete");
            if (autoComplete != null) {
                // only output the autocomplete attribute if the value
                // is 'off' since its lack of presence will be interpreted
                // as 'on' by the browser
                if ("off".equals(autoComplete)) {
                    writer.writeAttribute("autocomplete",
                                          "off",
                                          "autocomplete");
                }
            }

            // render default text specified
            if (currentValue != null) {
                writer.writeAttribute("value", currentValue, "value");
            }
            if (null != styleClass) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }

            // style is rendered as a passthur attribute
            RenderKitUtils.renderPassThruAttributes(context, writer, component);
            RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

            writer.endElement("input");

        } else if (isOutput = (component instanceof UIOutput)) {
            if (null != styleClass || null != style ||
                RenderKitUtils.hasPassThruAttributes(component) ||
                (shouldWriteIdAttribute = shouldWriteIdAttribute(component))) {
                writer.startElement("span", component);
                writeIdAttributeIfNecessary(context, writer, component);
                if (null != styleClass) {
                    writer.writeAttribute("class", styleClass, "styleClass");
                }
                // style is rendered as a passthru attribute
                RenderKitUtils
                      .renderPassThruAttributes(context, writer, component);

            }
            if (currentValue != null) {
                Object val = null;
                boolean escape = true;
                if (null != (val = component.getAttributes().get("escape"))) {
                    if (val instanceof Boolean) {
                        escape = ((Boolean) val).booleanValue();
                    } else if (val instanceof String) {
                        try {
                            escape =
                                  Boolean.valueOf((String) val).booleanValue();
                        } catch (Throwable e) {
                        }
                    }
                }
                if (escape) {
                    writer.writeText(currentValue, "value");
                } else {
                    writer.write(currentValue);
                }
            }
        }
        if (isOutput && (null != styleClass || null != style ||
                         RenderKitUtils.hasPassThruAttributes(component) ||
                         shouldWriteIdAttribute)) {
            writer.endElement("span");
        }

    }

    // The testcase for this class is TestRenderers_2.java

} // end of class TextRenderer


