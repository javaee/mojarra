/*
 * $Id: HiddenRenderer.java,v 1.15 2004/01/15 07:34:05 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HiddenRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;

import com.sun.faces.util.Util;

/**
 * <B>HiddenRenderer</B> is a class that renders the current value of 
 * <code>UIInput<code> component as a HTML hidden variable.
 */
public class HiddenRenderer extends HtmlBasicInputRenderer {
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

    public HiddenRenderer() {
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
        UIComponent component, String currentValue) throws IOException {

	ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null );

	writer.startElement("input", component);
	writeIdAttributeIfNecessary(context, writer, component);
	writer.writeAttribute("type", "hidden", "type");
        String clientId = component.getClientId(context);
	writer.writeAttribute("name", clientId, "clientId");

        // render default text specified
        if (currentValue != null) {
            writer.writeAttribute("value", currentValue, "value");
        }
        writer.endElement("input");
        
        FormRenderer.addRenderedHiddenField(context, clientId);
    }
    
    // The testcase for this class is TestRenderers_3.java 

} // end of class HiddenRenderer


