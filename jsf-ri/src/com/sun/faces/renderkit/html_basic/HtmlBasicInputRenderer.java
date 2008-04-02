/*
 * $Id: HtmlBasicInputRenderer.java,v 1.10 2003/09/08 20:10:09 jvisvanathan Exp $
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
import javax.faces.component.ValueHolder;
import javax.faces.component.UIInput;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.context.FacesContext;
import javax.faces.FactoryFinder;

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

    public void setPreviousValue(UIComponent component, Object value) {
        if (component instanceof UIInput) {
            ((UIInput)component).setPrevious(value);
        }
    }

    public Object getConvertedValue(FacesContext context, UIComponent component,
            String newValue) throws ConverterException {
        
        ValueHolder valueHolder = (ValueHolder) component;
        String valueRef = valueHolder.getValueRef();
        
        Converter converter = null;
        Object result = null;
        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
        
        converter = valueHolder.getConverter();
        
	if (converter == null && valueRef != null) {
            Class converterType = 
                    (Util.getValueBinding(valueRef)).getType(context);
            // if converterType is null, assume the modelType is "String".
            if ( converterType == null || converterType == String.class) {
                return newValue;
            }
            // if getType returns a type for which we support a default
            // conversion, acquire an appropriate converter instance.
        
            try {
                ApplicationFactory aFactory =
                    (ApplicationFactory)FactoryFinder.getFactory
                     (FactoryFinder.APPLICATION_FACTORY);
                Application application = aFactory.getApplication();
                converter = application.createConverter(converterType);
            } catch (Exception e) {
                return (null);
            }
	} else if ( converter == null && valueRef == null ) {
            // if there is no valueRef and converter attribute set, assume the 
            // modelType as "String" since we have no way of figuring out the
            // type. So for the selectOne and selectMany, converter has to be
            // set if there is no valueRef attribute set on the component.
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
