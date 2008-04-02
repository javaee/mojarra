/*
 * $Id: NumberRenderer.java,v 1.26 2003/08/22 21:03:01 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// NumberRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import com.sun.faces.renderkit.FormatPool;
import com.sun.faces.RIConstants;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 *  <B>DateRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: NumberRenderer.java,v 1.26 2003/08/22 21:03:01 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class NumberRenderer extends HtmlBasicInputRenderer {
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

    public Object getConvertedValue(FacesContext context, UIComponent component,
            String newValue) throws ConverterException {
	
        Object convertedValue = null;
        Number parsedValue = null;
        Class valueType = null;
       
        if ( newValue == null || newValue.length() == 0) {
            return null;
        }
        
        newValue = newValue.trim();
        // get FormatPool Instance from ServletContext
        Map applicationMap = context.getExternalContext().getApplicationMap();
        FormatPool formatPool = (FormatPool)applicationMap.get(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
        
        try {
            parsedValue = formatPool.numberFormat_parse(context, component, newValue);
        } catch (ParseException pe ) {
            throw new ConverterException(pe.getMessage());
        }
        
        // if valueReference is null, store value as Number.
        String valueRef = ((UIInput)component).getValueRef();
        if ( valueRef == null ) {
             return parsedValue;
        }    
        // convert the parsed value to model property type.
        try {
           valueType = (Util.getValueBinding(valueRef)).getType(context);
	} catch (FacesException fe ) {
            throw new ConverterException(Util.getExceptionMessage(
                Util.CONVERSION_ERROR_MESSAGE_ID));
	}    
        Assert.assert_it(valueType != null);
	Assert.assert_it(parsedValue != null);
        
        if ( (valueType.getName()).equals("java.lang.Character") ||
                (valueType.getName()).equals("char")) {
            // 36 is the maximum value allowed for radix.
            char charvalue = Character.forDigit(parsedValue.intValue(),36);
            convertedValue = (new Character(charvalue));
        } else {
            convertedValue = convertToModelType(valueType, parsedValue);
        }    
        return convertedValue;
    }
    
    protected Number convertToModelType(Class valueType, Number parsedValue) {
      
        Assert.assert_it(parsedValue != null);
        
        // PENDING (visvan) If it comes to rounding should we throw
        // an exception
        if ( (valueType.getName()).equals("java.lang.Byte") || 
                (valueType.getName()).equals("byte")) {
            return (new Byte(parsedValue.byteValue()));
        } else if ( (valueType.getName()).equals("java.lang.Double") ||
                (valueType.getName()).equals("double")) {
            return (new Double(parsedValue.doubleValue()));  
        } else if ( (valueType.getName()).equals("java.lang.Float") || 
                (valueType.getName()).equals("float") ) {
            return (new Float(parsedValue.floatValue()));
        } else if ( (valueType.getName()).equals("java.lang.Integer") || 
               (valueType.getName()).equals("int")) {
            return (new Integer(parsedValue.intValue()));
        } else if ( (valueType.getName()).equals("java.lang.Short") || 
                (valueType.getName()).equals("short") ) {
            return (new Short(parsedValue.shortValue()));
        } else if ( (valueType.getName()).equals("java.lang.Long") || 
                (valueType.getName()).equals("long")) {
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

    protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue) throws IOException {
                
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        boolean isInput = false;
        if (component instanceof UIInput) {
            isInput = true;
        }
        String styleClass = null;
        
	if (null != (styleClass = (String)component.getAttribute("inputClass"))) {
	    writer.writeAttribute("class", styleClass, "inputClass");
	    writer.startElement("span", component);
	} else if (null != (styleClass = (String)component.getAttribute("outputClass"))) {
	    writer.writeAttribute("class", styleClass, "outputClass");
	    writer.startElement("span", component);
	}
        if (isInput) {
	    writer.startElement("input", component);
	    writer.writeAttribute("type", "text", "type"); 
	    writer.writeAttribute("name", component.getClientId(context), "clientId"); 	
	    // deal with HTML 4.0 LABEL element
	    //PENDING(rogerk)clientId 3rd arg?
            writer.writeAttribute("id", component.getClientId(context), "clientId");
	    
	    // render default text specified
	    if ( currentValue != null ) {
                writer.writeAttribute("value", currentValue, "value");
	    }

            Util.renderPassThruAttributes(writer, component);
            Util.renderBooleanPassThruAttributes(writer, component);
	    writer.endElement("input");
	}else {
	    writer.writeText(currentValue, "value");
        }  
        if (null != styleClass) {
	    writer.endElement("span");
	}
    }
    
    protected String getFormattedValue(FacesContext context, UIComponent component,
            Object currentObj ) {
        String currentValue = null;
        // if we hit this method, then value is not string type.
        Map applicationMap = context.getExternalContext().getApplicationMap();
        FormatPool formatPool = (FormatPool)applicationMap.get(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);      
        
        if ( currentObj instanceof Number) {
            currentValue = formatPool.numberFormat_format(context, component,
	                (Number) currentObj);
        } else {
            // if currentObj is of type Character
            currentValue = currentObj.toString();
        }    
        return currentValue;
    }            
    
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class NumberRenderer


