/*
 * $Id: TextRenderer.java,v 1.57 2003/11/10 01:08:55 jvisvanathan Exp $
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
	boolean isOutput = false;

	String 
	    style = (String) component.getAttributes().get("style"),
	    styleClass = (String) component.getAttributes().get("styleClass");
        if (component instanceof UIInput) {
	    writer.startElement("input", component);
	    writer.writeAttribute("type", "text", "type");
	    writer.writeAttribute("name", (component.getClientId(context)), "clientId");

            // render default text specified
            if (currentValue != null) {
	        writer.writeAttribute("value", currentValue, "value");
            }
	    if (null != styleClass) {
		writer.writeAttribute("class", styleClass, "styleClass");
	    }

	    // style is rendered as a passthur attribute
            Util.renderPassThruAttributes(writer, component);
            Util.renderBooleanPassThruAttributes(writer, component);

	    writer.endElement("input");

        } else if (isOutput = (component instanceof UIOutput)) {
	    if (null != styleClass || null != style) {
		writer.startElement("span", component);
		if (null != styleClass) {
		    writer.writeAttribute("class", styleClass, "styleClass");
		}
		if (null != style) {
		    writer.writeAttribute("style", style, "style");
		}
	    } 
            if (currentValue != null) {
		Object val = null; 
		boolean escape = true;
		if (null != (val = component.getAttributes().get("escape"))) {
		    if (val instanceof Boolean) {
			escape = ((Boolean)val).booleanValue();
		    }
		    else if (val instanceof String) {
			try {
			    escape = Boolean.valueOf((String) val).booleanValue();
			}
			catch (Throwable e) {
			}
		    }
		}
		if (escape) {
		    writer.writeText(currentValue, "value");
		}
		else {
		    writer.write(currentValue);
		}
            }
        }
	if (isOutput && (null != styleClass || null != style)) {
	    writer.endElement("span");
	}
    }
    
   // The testcase for this class is TestRenderers_2.java 

} // end of class TextRenderer


