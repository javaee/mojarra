/*
 * $Id: TextRenderer.java,v 1.46 2003/08/08 16:20:23 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;

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
        UIComponent component, String currentValue) throws IOException {
        
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

	String styleClass = null;
        if ((null != (styleClass = (String) 
		      component.getAttribute("inputClass"))) || 
	    (null != (styleClass = (String) 
		      component.getAttribute("outputClass")))) {
	    writer.startElement("span");
	    writer.writeAttribute("class", styleClass);
	}
        if (component instanceof UIInput) {
	    writer.startElement("input");
	    writer.writeAttribute("type", "text");
	    writer.writeAttribute("name", (component.getClientId(context)));

            // render default text specified
            if (currentValue != null) {
	        writer.writeAttribute("value", currentValue);
            }

            Util.renderPassThruAttributes(writer, component);
            Util.renderBooleanPassThruAttributes(writer, component);

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
		writer.writeText(currentValue);
            }
        }
	if (null != styleClass) {
	    writer.endElement("span");
	}
    }
    
   // The testcase for this class is TestRenderers_2.java 

} // end of class TextRenderer


