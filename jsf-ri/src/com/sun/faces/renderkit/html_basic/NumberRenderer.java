/*
 * $Id: NumberRenderer.java,v 1.3 2002/08/17 00:57:03 jvisvanathan Exp $
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
 * @version $Id: NumberRenderer.java,v 1.3 2002/08/17 00:57:03 jvisvanathan Exp $
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

    public void decode(FacesContext context, UIComponent component) 
            throws IOException {
        
        if (!(component instanceof UIInput)) {
            return;
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
        if ( newValue == null ) {
            component.setValue(newValue);
            return;
        }
        
        // get FormatPool Instance from ServletContext
        FormatPool formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
        
        try {
            parsedValue = formatPool.numberFormat_parse(context, component, newValue);
            component.setValid(true);
        } catch (ParseException pe ) {
            component.setValue(newValue);
            component.setValid(false);
            addConversionErrorMessage( context, component, pe.getMessage());
            return;
        }
        // if modelReference is null, store value as Number.
        String modelRef = component.getModelReference();
        if ( modelRef == null ) {
             component.setValue(parsedValue);
             return;
             
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
            component.setValue(new Character((char)parsedValue.intValue()));
        } else {
            convertedValue = convertToModelType(modelType, parsedValue);
            component.setValue(convertedValue);
        }    
        
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
        ResponseWriter writer = null;
	FormatPool formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
        
        if (context == null || component == null) {
            throw new NullPointerException(
            Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
       
	Object curValue = null;

	if (null != (curValue = component.currentValue(context))) {
	    if (curValue instanceof Number) {
		currentValue = formatPool.numberFormat_format(context, component,
	                (Number) curValue);
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
        
        if (component instanceof UIOutput ) {
            writer.write(currentValue);
            return;
        }    
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
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class NumberRenderer


