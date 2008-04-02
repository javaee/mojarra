/*
 * $Id: ImageRenderer.java,v 1.28 2004/01/30 21:49:20 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ImageRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.util.Util;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *  <B>ImageRenderer</B> is a class that handles the rendering of the graphic
 *  ImageTag
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ImageRenderer.java,v 1.28 2004/01/30 21:49:20 craigmcc Exp $
 * 
 *
 */

public class ImageRenderer extends HtmlBasicRenderer {
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

    public ImageRenderer() {
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

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
        ResponseWriter writer = null;
	String styleClass = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
       
        writer = context.getResponseWriter();
        Util.doAssert(writer != null );
        
	writer.startElement("img", component);
	writeIdAttributeIfNecessary(context, writer, component);
	writer.writeAttribute("src", src(context,component), "value");

        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);
	if (null != (styleClass = (String) 
		     component.getAttributes().get("styleClass"))) {
	    writer.writeAttribute("class", styleClass, "styleClass");
	}
	String altText = (String)component.getAttributes().get("alt");
	if (altText == null) {
	    writer.writeAttribute("alt", "", "alt");
	}
	writer.endElement("img");
    }

    private String src(FacesContext context, UIComponent component) {
        String value = (String)((UIGraphic)component).getValue(); 
        if (value == null ) {
            return "";
        }
        value = context.getApplication().getViewHandler().
            getResourceURL(context, value);
        return (context.getExternalContext().encodeResourceURL(value));
    }
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class ImageRenderer


