/*
 * $Id: CheckboxRenderer.java,v 1.78 2006/03/29 22:38:36 rlubke Exp $
 *
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

// CheckboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;


/**
 * <B>CheckboxRenderer</B> is a class that renders the current value of
 * <code>UISelectBoolean<code> as a checkbox.
 */

public class CheckboxRenderer extends HtmlBasicInputRenderer {

    // ------------------------------------------------------------ Constructors


    public CheckboxRenderer() {

        super();

    }

    // ---------------------------------------------------------- Public Methods


    public void decode(FacesContext context, UIComponent component) {

        if (context == null || component == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin decoding component " + component.getId());
        }

        // If the checkbox disabled, nothing would be sent in the
        // request even if the checkbox is checked. So do not change the
        // value of the checkbox, if it is disabled since its state
        // cannot be changed.
        if (Util.componentIsDisabledOrReadonly(component)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("No decoding necessary since the component " +
                            component.getId() + " is disabled");
            }
            return;
        }

        String clientId = component.getClientId(context);
        assert (clientId != null);
        // Convert the new value

        Map<String, String> requestParameterMap = context.getExternalContext()
              .getRequestParameterMap();
        String newValue = requestParameterMap.get(clientId);
        //if there was nothing sent in the request the checkbox wasn't checked
        // if the checkbox is not disabled. 
        if (newValue == null) {
            newValue = "false";
            // Otherwise, if the checkbox was checked, the value
            // coming in could be "on", "yes" or "true".
        } else if (newValue.equalsIgnoreCase("on") ||
                   newValue.equalsIgnoreCase("yes") ||
                   newValue.equalsIgnoreCase("true")) {
            newValue = "true";
        }

        setSubmittedValue(component, newValue);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("new value after decoding" + newValue);
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End decoding component " + component.getId());
        }

    }


    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

    }


    public Object getConvertedValue(FacesContext context, UIComponent component,
                                    Object submittedValue)
          throws ConverterException {

        String newValue = (String) submittedValue;
        return Boolean.valueOf(newValue);

    }

    // ------------------------------------------------------- Protected Methods


    protected void getEndTextToRender(FacesContext context,
                                      UIComponent component,
                                      String currentValue) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        String styleClass = null;

        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("type", "checkbox", "type");
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");

        if ("true".equals(currentValue)) {
            writer.writeAttribute("checked", Boolean.TRUE, "value");
        }
        if (null != (styleClass = (String)
              component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        RenderKitUtils.renderPassThruAttributes(context, writer, component);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        writer.endElement("input");

    }

} // end of class CheckboxRenderer
