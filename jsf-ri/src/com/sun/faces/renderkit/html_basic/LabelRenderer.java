/*
 * $Id: LabelRenderer.java,v 1.11 2003/08/08 16:20:21 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LabelRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;

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

/**
 *
 *  <B>LabelRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: LabelRenderer.java,v 1.11 2003/08/08 16:20:21 rkitain Exp $
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

    public void encodeBegin(FacesContext context, UIComponent component) 
           throws IOException {
               
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
	ResponseWriter writer = null;
	String forValue = null;
	String outputClass = null;

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

	forValue = (String) component.getAttribute("for");
	Assert.assert_it(null != forValue);

	writer.startElement("label");
	writer.writeAttribute("for", forValue);

        Util.renderPassThruAttributes(writer, component);

	if (null != (outputClass = (String) 
		     component.getAttribute("outputClass"))) {
	    writer.writeAttribute("class", outputClass);
	}
        writer.writeText('\n');
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

        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

	writer.endElement("label");
    }

    // The testcase for this class is TestRenderResponsePhase.java

} // end of class LabelRenderer
