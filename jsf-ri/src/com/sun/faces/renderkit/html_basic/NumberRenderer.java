/*
 * $Id: NumberRenderer.java,v 1.9 2002/09/11 20:02:27 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// NumberRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import com.sun.faces.renderkit.FormatPool;
import com.sun.faces.RIConstants;

import java.util.Iterator;
import java.util.TimeZone;
import java.util.Date;

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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 *  <B>DateRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: NumberRenderer.java,v 1.9 2002/09/11 20:02:27 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class NumberRenderer extends HtmlBasicRenderer {
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

    public NumberRenderer() {
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

    public boolean decode(FacesContext context, UIComponent component) 
            throws IOException {
	boolean result = true;
        
        if (!(component.getComponentType() == UIInput.TYPE)) {
            return result;
        }
                 
        Number convertedValue = null;
        Number parsedValue = null;
        Class modelType = null;
       
        if (context == null || component == null) {
            throw new NullPointerException(
               Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
	
        String compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null );
        
        String newValue = context.getServletRequest().getParameter(compoundId);
        if ( newValue != null ) {
            newValue = newValue.trim();
        }    
        if ( newValue == null || newValue.length() == 0) {
            component.setValue(newValue);
            return result;
        }
        
        // get FormatPool Instance from ServletContext
        FormatPool formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
        
        try {
            parsedValue = formatPool.numberFormat_parse(context, component, newValue);
        } catch (ParseException pe ) {
            component.setValue(newValue);
            addConversionErrorMessage( context, component, pe.getMessage());
            return false;
        }
        // if modelReference is null, store value as Number.
        String modelRef = component.getModelReference();
        if ( modelRef == null ) {
             component.setValue(parsedValue);
             return result;
             
        }    
        // convert the parsed value to model property type.
        try {
            modelType = context.getModelType(modelRef);
	} catch (FacesException fe ) {
            throw new IOException(Util.getExceptionMessage(
                Util.CONVERSION_ERROR_MESSAGE_ID));
	}    
        Assert.assert_it(modelType != null);
	Assert.assert_it(parsedValue != null);
        
        if ( (modelType.getName()).equals("java.lang.Character") ||
                (modelType.getName()).equals("char")) {
            // 36 is the maximum value allowed for radix.
            char charvalue = Character.forDigit(parsedValue.intValue(),36);
            component.setValue(new Character(charvalue));
        } else {
            convertedValue = convertToModelType(modelType, parsedValue);
            component.setValue(convertedValue);
        }    
        return result;
    }
    
    protected Number convertToModelType(Class modelType, Number parsedValue) {
      
        Assert.assert_it(parsedValue != null);
        
        // PENDING (visvan) If it comes to rounding should we throw
        // an exception
        if ( (modelType.getName()).equals("java.lang.Byte") || 
                (modelType.getName()).equals("byte")) {
            return (new Byte(parsedValue.byteValue()));
        } else if ( (modelType.getName()).equals("java.lang.Double") ||
                (modelType.getName()).equals("double")) {
            return (new Double(parsedValue.doubleValue()));  
        } else if ( (modelType.getName()).equals("java.lang.Float") || 
                (modelType.getName()).equals("float") ) {
            return (new Float(parsedValue.floatValue()));
        } else if ( (modelType.getName()).equals("java.lang.Integer") || 
               (modelType.getName()).equals("int")) {
            return (new Integer(parsedValue.intValue()));
        } else if ( (modelType.getName()).equals("java.lang.Short") || 
                (modelType.getName()).equals("short") ) {
            return (new Short(parsedValue.shortValue()));
        } else if ( (modelType.getName()).equals("java.lang.Long") || 
                (modelType.getName()).equals("long")) {
            return (new Long(parsedValue.longValue()));
        }     
        return parsedValue;
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
                
        String currentValue = null;
        boolean isInput = UIInput.TYPE == component.getComponentType();
       
        FormatPool formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
        
        if (context == null || component == null) {
            throw new NullPointerException(
            Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
       
	Object curValue = null;
	String styleClass = null;

	if (null != (curValue = component.currentValue(context))) {
	    if (curValue instanceof Number) {
		currentValue = formatPool.numberFormat_format(context, component,
	                (Number) curValue);
	    }
	    else  {
                // if it is "Character" type, just convert it to string.
		currentValue = curValue.toString();
	    }
	}
	else {
	    currentValue = "";
	}

        ResponseWriter writer = null;
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

	if ((null != (styleClass = (String) 
		      component.getAttribute("inputClass"))) || 
	    (null != (styleClass = (String) 
		      component.getAttribute("outputClass")))) {
	    writer.write("<span class=\"" + styleClass + "\">");
	}
        if (isInput) {
	    StringBuffer buffer = new StringBuffer();
	    
	    buffer.append("<input type=\"text\"");
	    buffer.append(" name=\"");
	    buffer.append(component.getCompoundId());
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
	    buffer.append(Util.renderBooleanPassthruAttributes(context, component));
	    buffer.append(">");
	    // overwrite currentValue
	    currentValue = buffer.toString();
	}

	writer.write(currentValue);                 
	if (null != styleClass) {
	    writer.write("</span>");
	}
    }
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class NumberRenderer


