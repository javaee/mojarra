/*
 * $Id: CheckboxRenderer.java,v 1.56 2003/09/24 23:16:31 horwat Exp $
 *
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// CheckboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.FacesException;
import javax.faces.convert.ConverterException;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>CheckboxRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CheckboxRenderer.java,v 1.56 2003/09/24 23:16:31 horwat Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CheckboxRenderer extends HtmlBasicInputRenderer {
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
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // If the checkbox disabled, nothing would be sent in the
        // request even if the checkbox is checked. So do not change the
        // value of the checkbox, if it is disabled since its state
        // cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
            return;
        } 

        String clientId = component.getClientId(context);
        Assert.assert_it(clientId != null );

        Object curValue = ((UIInput)component).currentValue(context);
        if (curValue == null) {
            setPreviousValue(component, Boolean.FALSE);
        }

        // Convert the old (current value)

        if (curValue instanceof String) {
            try {
                Object convertedCurrentValue = 
                    getConvertedValue(context, component, (String)curValue);
                setPreviousValue(component, convertedCurrentValue);
            } catch (ConverterException e) {
                setPreviousValue(component, Boolean.FALSE);
            }
        } else {
            // current value is already of the type Boolean.
            setPreviousValue(component, curValue);
        }    

        // Convert the new value
        UIInput uiInput = (UIInput) component;
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String newValue = (String)requestParameterMap.get(clientId);
        try {
            convertedValue = getConvertedValue(context, component, newValue);
        } catch (ConverterException ce) {
            ((UIInput)component).setValue(newValue);
            addConversionErrorMessage(context, component, ce.getMessage());
            uiInput.setValid(false);
            return;
        }
        uiInput.setValue(convertedValue);
    }

    public Object getConvertedValue(FacesContext context, UIComponent component,
            String newValue) throws ConverterException {
     
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
        Object convertedValue = Boolean.valueOf(newValue);
	return convertedValue;
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

    }

    protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue) throws IOException {
 
	ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
	String styleClass = null;

	writer.startElement("input", component);
	writer.writeAttribute("type", "checkbox", "type");
	writer.writeAttribute("name", component.getClientId(context), "clientId"); 

        if (currentValue != null && currentValue.equals("true")) {
	    writer.writeAttribute("checked", new Boolean("true"), "value");
        }
        if (null != (styleClass = (String) 
		     component.getAttributes().get("styleClass"))) {
	    writer.writeAttribute("class", styleClass, "styleClass");
	}
        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);

	writer.endElement("input");
    }

} // end of class CheckboxRenderer
