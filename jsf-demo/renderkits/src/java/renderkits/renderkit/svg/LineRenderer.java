/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LineRenderer.java

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
 * <B>LineRenderer</B> is a class that renders an <code>SVG</code> 
 * Line.
 */

public class LineRenderer extends BaseRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(LineRenderer.class);

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

        writer.startElement("line", component);
        writeIdAttributeIfNecessary(context, writer, component);
        String x1 = (String)component.getAttributes().get("x1");
        if (x1 != null) {
            writer.writeAttribute("x1", x1, "x1");
        }
        String y1 = (String)component.getAttributes().get("y1");
        if (y1 != null) {
            writer.writeAttribute("y1", y1, "y1");
        }
        String x2 = (String)component.getAttributes().get("x2");
        if (x2 != null) {
            writer.writeAttribute("x2", x2, "x2");
        }
        String y2 = (String)component.getAttributes().get("y2");
        if (y2 != null) {
            writer.writeAttribute("y2", y2, "y2");
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

        writer.endElement("line");
        writer.writeText("\n", null);
    }

    //
    // General Methods
    //

} // end of class LineRenderer
