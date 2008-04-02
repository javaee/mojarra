/*
 * $Id: SecretRenderer.java,v 1.42 2003/07/29 18:23:25 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SecretRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <B>SecretRenderer</B> is a class that renders the current value of 
 * <code>UIInput<code> component as a password field.
 */

public class SecretRenderer extends HtmlBasicInputRenderer {
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

    public SecretRenderer() {
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
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    protected void getEndTextToRender(FacesContext context, 
        UIComponent component, String currentValue, StringBuffer buffer ) {

        String inputClass = null;
        
        String redisplay = (String)component.getAttribute("redisplay");
        if (redisplay == null || !redisplay.equals("true")) {
            currentValue = "";
        }

        buffer.append("<input type=\"password\"");
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
        buffer.append(Util.renderBooleanPassthruAttributes(context, component));
	if (null != (inputClass = (String) 
		     component.getAttribute("inputClass"))) {
	    buffer.append(" class=\"" + inputClass + "\" ");
	}
	
        buffer.append(">");         
    }

} // end of class SecretRenderer
