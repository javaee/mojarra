/*
 * $Id: HtmlBasicInputRenderer.java,v 1.16 2004/01/06 14:53:20 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicInputRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.ConvertibleValueHolder;
import javax.faces.component.ValueHolder;
import javax.faces.component.UIInput;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.context.FacesContext;
import javax.faces.FactoryFinder;
import javax.faces.el.ValueBinding;

import com.sun.faces.util.Util;

/**
 *
 *  <B>HtmlBasicInputRenderer</B> is a base class for implementing renderers 
 *  that support UIInput type components
 */

public abstract class HtmlBasicInputRenderer extends HtmlBasicRenderer {
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

    public HtmlBasicInputRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public void setSubmittedValue(UIComponent component, Object value) {
        if (component instanceof UIInput) {
            ((UIInput)component).setSubmittedValue(value);
        }
    }

    public Object getConvertedValue(FacesContext context, UIComponent component,
            Object submittedValue) throws ConverterException {
        
        String newValue = (String) submittedValue;
        ValueHolder valueHolder = (ValueHolder) component;
	// if we have no local value, try to get the valueBinding.
	ValueBinding valueBinding = component.getValueBinding("value");
        
        Converter converter = null;
        Object result = null;
        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
        
        if (component instanceof ConvertibleValueHolder) {
            converter = ((ConvertibleValueHolder) component).getConverter();
        }
        
	if (null == converter && null != valueBinding) {
            Class converterType = valueBinding.getType(context);
            // if converterType is null, assume the modelType is "String".
            if ( converterType == null || 
                 converterType == String.class ||
                 converterType == Object.class) {
                return newValue;
            }
            // if getType returns a type for which we support a default
            // conversion, acquire an appropriate converter instance.
        
            try {
                Application application = context.getApplication();
                converter = application.createConverter(converterType);
            } catch (Exception e) {
                return (null);
            }
	} else if ( converter == null && valueBinding == null ) {
            // if there is no valueBinding and converter attribute set,
            // assume the modelType as "String" since we have no way of
            // figuring out the type. So for the selectOne and
            // selectMany, converter has to be set if there is no
            // valueBinding attribute set on the component.
            return newValue;
        }
        
        if ( converter != null) {
            result = converter.getAsObject(context, component, newValue);
	    return result;
        } else {
            // throw converter exception.
             throw new ConverterException(Util.getExceptionMessage(
                Util.CONVERSION_ERROR_MESSAGE_ID));
        }
    }
} // end of class HtmlBasicInputRenderer
