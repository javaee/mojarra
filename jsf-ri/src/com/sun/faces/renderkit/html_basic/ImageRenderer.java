/*
 * $Id: ImageRenderer.java,v 1.3 2002/09/07 16:35:58 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ImageRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 *  <B>ImageRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ImageRenderer.java,v 1.3 2002/09/07 16:35:58 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
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
    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    
        return (componentType.equals(UIGraphic.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
        throws IOException {
        return;
    }

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
	String graphicClass = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
       
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        
        writer.write("<img src=\"");
        writer.write(src(context, component) + "\"");
	writer.write(Util.renderPassthruAttributes(context, component));
	writer.write(Util.renderBooleanPassthruAttributes(context, component));
	if (null != (graphicClass = (String) 
		     component.getAttribute("graphicClass"))) {
	    writer.write(" class=\"" + graphicClass + "\"");
	}
        writer.write(">");
    }

    private String src(FacesContext context, UIComponent component) {

        String value = (String) component.currentValue(context);
        if (value == null) {
            value = "";
        }

        if (value == "") {
            try {
                value = getKeyAndLookupInBundle(context, component, "key");
            } catch (java.util.MissingResourceException e) {
                // Do nothing since the absence of a resource is not an
                // error.
            }
        }
                              
        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        HttpServletResponse response =
            (HttpServletResponse) context.getServletResponse();
        StringBuffer sb = new StringBuffer();
        if (value.startsWith("/")) {
            sb.append(request.getContextPath());
        }
        sb.append(value);
        return (response.encodeURL(sb.toString()));
    }
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class ImageRenderer


