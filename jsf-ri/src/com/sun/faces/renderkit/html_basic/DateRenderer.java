/*
 * $Id: DateRenderer.java,v 1.20 2003/03/19 21:16:33 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// DateRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;
import java.util.TimeZone;
import java.util.Date;
import java.lang.Long;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.FacesException;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;


import com.sun.faces.renderkit.FormatPool;
import com.sun.faces.RIConstants;

/**
 *
 *  <B>DateRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: DateRenderer.java,v 1.20 2003/03/19 21:16:33 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class DateRenderer extends HtmlBasicInputRenderer {
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

    protected DateFormat formatter = null;

    //
    // Constructors and Initializers    
    //

    public DateRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    protected Date parseDate(FacesContext context, 
			  UIComponent component, String newValue) throws ParseException {
	FormatPool formatPool = null;
	formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
	return formatPool.dateFormat_parse(context, component, newValue);
    }

    protected String formatDate(FacesContext context, 
			     UIComponent component, Date dateValue) {
	FormatPool formatPool = null;
	formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
	return formatPool.dateFormat_format(context, component, dateValue);
    }
	

    //
    // Methods From Renderer
    //
    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    
        return (componentType.equals(UIInput.TYPE) || 
		componentType.equals(UIOutput.TYPE));
    }

     public Object getConvertedValue(FacesContext context, UIComponent component,
            String newValue) throws ConverterException {
	
        Object convertedValue = null;
        Class valueType = null;
        String valueRef = null;
	Date newDateValue = null;
        
        if ( newValue == null || newValue.length() == 0) {
            return null;
        }
      
	// Try to get the newValue as a Date
	try {
	    newDateValue = this.parseDate(context, component, newValue);
	}
	catch (ParseException pe) {
	    throw new ConverterException(pe.getMessage());
	}
        valueRef = ((UIInput)component).getValueRef();
	if (null != valueRef) {
	    try {
		valueType = (Util.getValueBinding(valueRef)).getType(context);
	    } catch (FacesException fe ) {
		throw new ConverterException(Util.getExceptionMessage(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
	    }    
	    Assert.assert_it(valueType != null );
	    
	    // Verify the modelType is one of the supported types
	    if (valueType.isAssignableFrom(Date.class)) {
		convertedValue = newDateValue;
	    }	    
	    else if (valueType.isAssignableFrom(Long.class)) {
		convertedValue = (new Long(newDateValue.getTime()));
	    }
	    else {
		throw new ConverterException(Util.getExceptionMessage(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
	    }
	}
	else {
	    convertedValue = newDateValue;
	}
	return convertedValue;
    }
    
    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

     protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue, StringBuffer buffer ) {
                
        boolean isInput = false;
        if ((UIInput.TYPE.equals(component.getComponentType())) ||
            (component instanceof UIInput)) {
            isInput = true;
        }
        String styleClass = null;
 
	if ((null != (styleClass = (String) 
		      component.getAttribute("inputClass"))) || 
	    (null != (styleClass = (String) 
		      component.getAttribute("outputClass")))) {
	    buffer.append("<span class=\"" + styleClass + "\">");
	}
        
	if (isInput) {
	    buffer.append("<input type=\"text\"");
	    buffer.append(" name=\"");
	    buffer.append(component.getClientId(context));
	    buffer.append("\"");
	    // deal with HTML 4.0 LABEL element
	    buffer.append(" id=\"");
	    buffer.append(component.getComponentId());
	    buffer.append("\"");
	    // render default text specified
	    if ( currentValue != null ) {
		buffer.append(" value=\"");
		buffer.append(currentValue);
		buffer.append("\"");
            }    
            buffer.append(Util.renderPassthruAttributes(context, component));
            buffer.append(Util.renderBooleanPassthruAttributes(context, 
                        component));
            buffer.append(">");    
	} else {
            buffer.append(currentValue);
        }  
        if (null != styleClass) {
	    buffer.append("</span>");
	}
    }
    
   protected String getFormattedValue(FacesContext context, UIComponent component,
            Object currentObj ) {
       String currentValue = null;         
       // if the currentValue is of type String, no formatting
       // is necessary. This would be the case when the page is
       // rendered for the first time.
       if (currentObj instanceof String) {
            return (String)currentObj;
       }
       if (currentObj instanceof Date) {
           currentValue = formatDate(context, component, (Date) currentObj);
       }   
       return currentValue;
    }
		   
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class DateRenderer


