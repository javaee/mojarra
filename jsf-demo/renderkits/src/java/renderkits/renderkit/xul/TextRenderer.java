/*
 * $Id: TextRenderer.java,v 1.1 2005/06/24 21:04:17 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextRenderer.java

package renderkits.renderkit.xul;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

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


