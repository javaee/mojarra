/*
 * $Id: HiddenRenderer.java,v 1.9 2003/07/29 18:23:22 jvisvanathan Exp $
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

import java.io.IOException;

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
        UIComponent component, String currentValue, StringBuffer buffer ) {

        buffer.append("<input type=\"hidden\"");
        buffer.append(" name=\"");
        buffer.append(component.getClientId(context));
        buffer.append("\"");

        // render default text specified
        if (currentValue != null) {
            buffer.append(" value=\"");
            buffer.append(currentValue);
            buffer.append("\"");
        }
        buffer.append(">");
    }
    
    // The testcase for this class is TestRenderers_3.java 

} // end of class TextRenderer


