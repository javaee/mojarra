/*
 * $Id: TextRenderer.java,v 1.3 2005/12/14 22:27:41 rlubke Exp $
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

package renderkits.renderkit.xul;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;

/**
 * <B>TextRenderer</B> is a class that renders the current value of
 * <code>UIInput<code> or <code>UIOutput<code> component as a input field or
 * static text.
 */
public class TextRenderer extends BaseRenderer {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public TextRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component is null");
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        boolean
              shouldWriteIdAttribute = false,
              isOutput = false;

        String
              style = (String) component.getAttributes().get("style"),
              styleClass = (String) component.getAttributes().get("styleClass");
        if (component instanceof UIInput) {
            writer.startElement("textbox", component);
            writeIdAttributeIfNecessary(context, writer, component);
            writer.writeAttribute("type", "text", null);
            writer.writeAttribute("name", (component.getClientId(context)),
                                  "clientId");

            // render default text specified
            String currentValue = getCurrentValue(context, component);
            if (currentValue != null) {
                writer.writeAttribute("value", currentValue, "value");
            }
            if (null != styleClass) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            if (null != style) {
                writer.writeAttribute("style", style, "style");
            }

            writer.endElement("textbox");

        } else if (isOutput = (component instanceof UIOutput)) {
            writer.writeText("\n", null);
            writer.startElement("description", component);
            writeIdAttributeIfNecessary(context, writer, component);
            if (null != styleClass) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            if (null != style) {
                writer.writeAttribute("style", style, "style");
            }
            String currentValue = getCurrentValue(context, component);
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
            writer.endElement("description");
        }
    }
} // end of class TextRenderer


