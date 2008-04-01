/*
 * $Id: LabelRenderer.java,v 1.1 2002/09/05 18:54:12 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LabelRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 *
 *  <B>LabelRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: LabelRenderer.java,v 1.1 2002/09/05 18:54:12 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class LabelRenderer extends HtmlBasicRenderer {
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

    public LabelRenderer() {
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
        return (componentType.equals(UIOutput.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
	// NO-OP decode
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
           throws IOException {
               
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
	ResponseWriter writer = null;
	String forValue = null;

        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

	forValue = (String) component.getAttribute("for");
	Assert.assert_it(null != forValue);

	// PENDING(edburns): There has to be a better way to do this
	if (!forValue.startsWith("/")) {
	    try {
		java.net.URL base = new java.net.URL("http://ha" + 
						    component.getCompoundId());
		java.net.URL relative = new java.net.URL(base, forValue);
		forValue = relative.getPath();
	    }
	    catch (Throwable e) {
		// PENDING(edburns): log error
	    }
	}
	
	writer.write("<label for=\"" + forValue + "\">\n");
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
                
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        ResponseWriter writer = null;

        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        writer.write("</label>");
    }

    // The testcase for this class is TestRenderResponsePhase.java

} // end of class LabelRenderer
