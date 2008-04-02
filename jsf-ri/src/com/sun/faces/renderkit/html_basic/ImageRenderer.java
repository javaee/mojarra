/*
 * $Id: ImageRenderer.java,v 1.38 2005/05/16 20:16:27 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ImageRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;

/**
 * <B>ImageRenderer</B> is a class that handles the rendering of the graphic
 * ImageTag
 *
 * @version $Id: ImageRenderer.java,v 1.38 2005/05/16 20:16:27 rlubke Exp $
 */

public class ImageRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    private static final Log log = LogFactory.getLog(ImageRenderer.class);
    
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
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        ResponseWriter writer = null;
        String styleClass = null;

        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        writer = context.getResponseWriter();
        assert (writer != null);

        writer.startElement("img", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("src", src(context, component), "value");
	// if we're writing XHTML and we have a null alt attribute
	if (null != context.getExternalContext().getRequestMap().get(RIConstants.CONTENT_TYPE_IS_XHTML) && 
	    null == component.getAttributes().get("alt")) {
	    // write out an empty alt
	    writer.writeAttribute("alt", "", "alt");
	}

        Util.renderPassThruAttributes(context, writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);
        if (null != (styleClass = (String)
            component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        writer.endElement("img");
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }
    }


    private String src(FacesContext context, UIComponent component) {
        String value = (String) ((UIGraphic) component).getValue();
        if (value == null) {
            return "";
        }
        value = context.getApplication().getViewHandler().
            getResourceURL(context, value);
        return (context.getExternalContext().encodeResourceURL(value));
    }
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class ImageRenderer


