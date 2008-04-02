/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RectangleRenderer.java

package renderkits.renderkit.svg;

import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.component.UIForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <B>RectangleRenderer</B> is a class that renders an <code>SVG</code> 
 * Rectangle.
 */

public class RectangleRenderer extends BaseRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(RectangleRenderer.class);

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

    //
    // Class methods
    //

    //
    // General Methods
    //
    
    //
    // Methods From Renderer
    //

    public void decode(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component is null");
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + component.getId());
        }
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component' is null");
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since rendered attribute is set to false ");
            }
            return;
        }
        
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("g", component);
        writer.writeText("\n    ", null);
        writer.startElement("rect", component);
        writeIdAttributeIfNecessary(context, writer, component);
        String width = (String)component.getAttributes().get("width");
        if (width != null) {
            writer.writeAttribute("width", width, "width");
        }
        String height = (String)component.getAttributes().get("height");
        if (height != null) {
            writer.writeAttribute("height", height, "height");
        }
        String x = (String)component.getAttributes().get("x");
        if (x != null) {
            writer.writeAttribute("x", x, "x");
        }
        String y = (String)component.getAttributes().get("y");
        if (y != null) {
            writer.writeAttribute("y", y, "y");
        }
        String rx = (String)component.getAttributes().get("rx");
        if (rx != null) {
            writer.writeAttribute("rx", rx, "rx");
        }
        String ry = (String)component.getAttributes().get("ry");
        if (ry != null) {
            writer.writeAttribute("ry", ry, "ry");
        }
        String style = (String)component.getAttributes().get("style");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeText("\n    ", null);
    }

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component' is null");
        }
        ResponseWriter writer = context.getResponseWriter();

        writer.endElement("rect");
        writer.writeText("\n    ", null);
        writer.endElement("g");
        writer.writeText("\n", null);
    }

    //
    // General Methods
    //

} // end of class RectangleRenderer
