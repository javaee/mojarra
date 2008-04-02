/*
 * $Id: SecretRenderer.java,v 1.61 2006/01/11 15:28:10 rlubke Exp $
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

// SecretRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.renderkit.RenderKitUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;

/**
 * <B>SecretRenderer</B> is a class that renders the current value of
 * <code>UIInput<code> component as a password field.
 */

public class SecretRenderer extends HtmlBasicInputRenderer {

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

    public SecretRenderer() {
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
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    protected void getEndTextToRender(FacesContext context,
                                      UIComponent component, String currentValue)
        throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        String styleClass = null;

        String redisplay = "" + component.getAttributes().get("redisplay");
        if (redisplay == null || !redisplay.equals("true")) {
            currentValue = "";
        }

        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("type", "password", "type");
        writer.writeAttribute("name", component.getClientId(context),
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

        RenderKitUtils.renderPassThruAttributes(context, writer, component);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        if (null != (styleClass = (String)
            component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        writer.endElement("input");
    }

} // end of class SecretRenderer
