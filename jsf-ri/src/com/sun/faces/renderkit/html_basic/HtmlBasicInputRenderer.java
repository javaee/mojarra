/*
 * $Id: HtmlBasicInputRenderer.java,v 1.2 2003/02/18 23:05:12 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
            String newValue) throws IOException {
        Converter converter = getConverter(component);
	Object result = null;
        if (converter != null) {
            try {
                result = converter.getAsObject(context, component, newValue);
            } catch (ConverterException e) {
                throw new IOException(e.getMessage());
            }
        } else if ( null != newValue && 0 < newValue.length()) {
	    result = newValue;
	}
	return result;
    }
         


} // end of class HtmlBasicInputRenderer
