/*
 * $Id: DateRenderer.java,v 1.2 2002/08/12 23:15:36 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// DateRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;
import java.util.TimeZone;
import java.util.Date;
import java.util.Calendar;
import java.lang.Long;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

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
 * @version $Id: DateRenderer.java,v 1.2 2002/08/12 23:15:36 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class DateRenderer extends HtmlBasicRenderer {
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

    public void decode(FacesContext context, UIComponent component) 
            throws IOException {
        Object convertedValue = null;
        Class modelType = null;
        String newValue = null;
        String modelRef = null;
        String compoundId = null;
	FormatPool formatPool = null;
	Date newDateValue = null;

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	if (!(component instanceof UIInput)) {
	    // do nothing in output case
	    return;
	}
	
        compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null );
        
	if (null == (newValue = 
		     context.getServletRequest().getParameter(compoundId))) {
	    return;
	}
	
        modelRef = component.getModelReference();
	formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
	
	// Try to get the newValue as a Date
	try {
	    newDateValue = formatPool.dateFormat_parse(context, component, 
						       newValue);
	}
	catch (ParseException e) {
	    component.setValue(newValue);
	    component.setValid(false);
	    return;
	}
	
	if (null != modelRef) {
	    try {
		modelType = context.getModelType(modelRef);
	    } catch (FacesException fe ) {
		throw new IOException(Util.getExceptionMessage(Util.CONVERSION_ERROR_MESSAGE_ID));
	    }    
	    Assert.assert_it(modelType != null );
	    
	    // Verify the modelType is one of the supported types
	    if (modelType.isAssignableFrom(Date.class)) {
		component.setValue(newDateValue);
	    }	    
	    else if (modelType.isAssignableFrom(Long.class)) {
		component.setValue(new Long(newDateValue.getTime()));
	    }
	    else if (modelType.isAssignableFrom(Calendar.class)) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDateValue);
		component.setValue(calendar);
	    }
	    else {
		throw new IOException(Util.getExceptionMessage(Util.CONVERSION_ERROR_MESSAGE_ID));
	    }
	}
	else {
	    component.setValue(newDateValue);
	}
	component.setValid(true);
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

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
	boolean isInput = component instanceof UIInput;
        String currentValue = null;
        ResponseWriter writer = null;
	FormatPool formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
       
	Object curValue = null;

	if (null != (curValue = component.currentValue(context))) {
	    if (curValue instanceof Date) {
		currentValue = formatPool.dateFormat_format(context, component,
							    (Date) curValue);
	    }
	    else if (curValue instanceof String) {
		currentValue = (String) curValue;
	    }
	}
	else {
	    currentValue = "";
	}
        
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

	if (isInput) {
	    writer.write("<input type=\"text\"");
	    writer.write(" name=\"");
	    writer.write(component.getCompoundId());
	    writer.write("\"");
	    
	    // render default text specified
	    if ( currentValue != null ) {
		writer.write(" value=\"");
		writer.write(currentValue);
		writer.write("\"");
	    }
	    writer.write(Util.renderPassthruAttributes(context, component));
	    writer.write(Util.renderBooleanPassthruAttributes(context, component));
	    writer.write(">");            
	}
	else {
	    writer.write(currentValue);
	}
    }
		   
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class DateRenderer


