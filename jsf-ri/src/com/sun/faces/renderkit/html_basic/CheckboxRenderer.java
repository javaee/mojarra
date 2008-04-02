/*
 * $Id: CheckboxRenderer.java,v 1.70 2004/12/16 17:56:37 edburns Exp $
 *
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// CheckboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import java.io.IOException;
import java.util.Map;

/**
 * <B>CheckboxRenderer</B> is a class that renders the current value of
 * <code>UISelectBoolean<code> as a checkbox.
 */

public class CheckboxRenderer extends HtmlBasicInputRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(CheckboxRenderer.class);

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

    public CheckboxRenderer() {
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

    public void decode(FacesContext context, UIComponent component) {

        Object convertedValue = null;

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + component.getId());
        }

        // If the checkbox disabled, nothing would be sent in the
        // request even if the checkbox is checked. So do not change the
        // value of the checkbox, if it is disabled since its state
        // cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
            if (log.isTraceEnabled()) {
                log.trace("No decoding necessary since the component " +
                          component.getId() + " is disabled");
            }
            return;
        }

        String clientId = component.getClientId(context);
        assert (clientId != null);
        // Convert the new value
        UIInput uiInput = (UIInput) component;
        Map requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        String newValue = (String) requestParameterMap.get(clientId);
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
        if (log.isTraceEnabled()) {
            log.trace("new value after decoding" + newValue);
        }
        if (log.isTraceEnabled()) {
            log.trace("End decoding component " + component.getId());
        }
    }


    public Object getConvertedValue(FacesContext context, UIComponent component,
                                    Object submittedValue)
        throws ConverterException {

        String newValue = (String) submittedValue;
        Object convertedValue = Boolean.valueOf(newValue);
        return convertedValue;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

    }


    protected void getEndTextToRender(FacesContext context, UIComponent component,
                                      String currentValue) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        String styleClass = null;

        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("type", "checkbox", "type");
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");

        if (currentValue != null && currentValue.equals("true")) {
            writer.writeAttribute("checked", Boolean.TRUE, "value");
        }
        if (null != (styleClass = (String)
            component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        Util.renderPassThruAttributes(context, writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);

        writer.endElement("input");
    }

} // end of class CheckboxRenderer
