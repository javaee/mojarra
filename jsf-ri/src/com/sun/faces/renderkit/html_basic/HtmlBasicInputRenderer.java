/*
 * $Id: HtmlBasicInputRenderer.java,v 1.6 2003/07/29 18:23:22 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicInputRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIInput;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.sun.faces.util.Util;
import java.io.IOException;

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
        
        UIOutput uiOutput = (UIOutput) component;
        String valueRef = uiOutput.getValueRef();
        
        Converter converter = null;
        Object result = null;
        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
        String converterId = component.getConverter();
        converter = getConverter(converterId);
	if (converter == null && valueRef != null) {
            Class converterType = 
                    (Util.getValueBinding(valueRef)).getType(context);
            // if converterType is null, assume the modelType is "String".
            if ( converterType == null) {
                return newValue;
            }
            // if getType returns a type for which we support a default
            // conversion, acquire an appropriate converter instance.
            converterId = Util.getDefaultConverterForType(
                     (converterType.getName()));
            converter = getConverter(converterId);
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
