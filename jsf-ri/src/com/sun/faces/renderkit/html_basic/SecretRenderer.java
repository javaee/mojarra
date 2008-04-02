/*
 * $Id: SecretRenderer.java,v 1.50 2004/01/14 17:13:04 eburns Exp $
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
import javax.faces.context.ResponseWriter;

import com.sun.faces.util.Util;

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
        UIComponent component, String currentValue) throws IOException {
	ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null );

        String styleClass = null;
        
        String redisplay = "" + component.getAttributes().get("redisplay");
        if (redisplay == null || !redisplay.equals("true")) {
            currentValue = "";
        }

	writer.startElement("input", component);
	writeIdAttributeIfNecessary(context, writer, component);
	writer.writeAttribute("type", "password", "type");
	writer.writeAttribute("name", component.getClientId(context), "clientId"); 

        // render default text specified
        if (currentValue != null) {
	    writer.writeAttribute("value", currentValue, "value");
        }

        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer,component);

	if (null != (styleClass = (String) 
		     component.getAttributes().get("styleClass"))) {
	    writer.writeAttribute("class", styleClass, "styleClass");
	}
	
	writer.endElement("input");
    }

} // end of class SecretRenderer
