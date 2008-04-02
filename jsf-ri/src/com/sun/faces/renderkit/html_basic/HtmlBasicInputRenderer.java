/*
 * $Id: HtmlBasicInputRenderer.java,v 1.4 2003/03/11 01:20:23 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicInputRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.context.FacesContext;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import java.io.IOException;

/**
 *
 *  <B>HtmlBasicInputRenderer</B> 
 * @version
 * 
 * @see	Blah
 * @see	Bloo
 *
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
            component.setAttribute(UIInput.PREVIOUS_VALUE, value);
        }
    }

    public Object getConvertedValue(FacesContext context, UIComponent component,
            String newValue) throws ConverterException {
        Converter converter = getConverter(component);
	Object result = null;
        if (converter != null) {
            result = converter.getAsObject(context, component, newValue);
        } else if ( null != newValue && 0 < newValue.length()) {
	    result = newValue;
	}
	return result;
    }
         


} // end of class HtmlBasicInputRenderer
