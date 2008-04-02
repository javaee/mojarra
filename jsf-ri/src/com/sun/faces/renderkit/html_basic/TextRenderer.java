/*
 * $Id: TextRenderer.java,v 1.45 2003/07/29 18:23:25 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import java.io.IOException;
import java.util.Iterator;

/**
 * <B>TextRenderer</B> is a class that renders the current value of 
 * <code>UIInput<code> or <code>UIOutput<code> component as a input field or
 * static text.
 */
public class TextRenderer extends HtmlBasicInputRenderer {
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

    public TextRenderer() {
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

    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    protected void getEndTextToRender(FacesContext context, 
        UIComponent component, String currentValue, StringBuffer buffer ) {
        
	String styleClass = null;
        if ((null != (styleClass = (String) 
		      component.getAttribute("inputClass"))) || 
	    (null != (styleClass = (String) 
		      component.getAttribute("outputClass")))) {
	    buffer.append("<span class=\"" + styleClass + "\">");
	}
        if (component instanceof UIInput) {
            buffer.append("<input type=\"text\"");
            buffer.append(" name=\"");
            buffer.append(component.getClientId(context));
            buffer.append("\"");

            // render default text specified
            if (currentValue != null) {
                buffer.append(" value=\"");
                buffer.append(currentValue);
                buffer.append("\"");
            }
            buffer.append(Util.renderPassthruAttributes(context, component));
            buffer.append(Util.renderBooleanPassthruAttributes(context, 
                component));
            buffer.append(">");            
        } else if (component instanceof UIOutput) {
            if (currentValue == null || currentValue == "") {
                try {
                    currentValue = getKeyAndLookupInBundle(context, component,
                                                       "key");
                } catch (java.util.MissingResourceException e) {
                    // Do nothing since the absence of a resource is not an
                    // error.
                    return;
                }
            }
            if (currentValue != null) {
                buffer.append(currentValue);
            }
        }
	if (null != styleClass) {
	    buffer.append("</span>");
	}
    }
    
   // The testcase for this class is TestRenderers_2.java 

} // end of class TextRenderer


