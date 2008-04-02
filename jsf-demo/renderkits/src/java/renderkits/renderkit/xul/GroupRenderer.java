/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package renderkits.renderkit.xul;

import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;

/**
 * Arbitrary grouping "renderer" that simply renders its children
 * recursively in the <code>encodeEnd()</code> method.
 *
 * @version $Id: GroupRenderer.java,v 1.1 2005/06/24 21:04:16 rogerk Exp $
 */
public class GroupRenderer extends BaseRenderer {

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

    public GroupRenderer() {
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

    public boolean getRendersChildren() {
        return true;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Begin encoding component " +
                      component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        String
            style = (String) component.getAttributes().get("style"),
            styleClass = (String) component.getAttributes().get("styleClass"),
            captionClass = (String) component.getAttributes().get("captionClass"),
            captionLabel = (String) component.getAttributes().get("captionLabel");
        ResponseWriter writer = context.getResponseWriter();

        writer.writeText("\n", null);
        writer.startElement("groupbox", component);
        writeIdAttributeIfNecessary(context, writer, component);
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        if (captionLabel != null) {
            writer.writeText("\n", null);
            writer.startElement("caption", component);
            writer.writeAttribute("label", captionLabel, "captionLabel");
            if (captionClass != null) {
                writer.writeAttribute("class", captionClass, "captionClass");
            }
            writer.endElement("caption");
        }
        writer.writeText("\n", null);
    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
       if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        // Render our children recursively
        Iterator kids = getChildren(component);
        while (kids.hasNext()) {
            encodeRecursive(context, (UIComponent) kids.next());
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"End encoding children " + component.getId());
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component is null");
        }

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("groupbox");
        writer.writeText("\n", null);

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"End encoding component " +
                      component.getId());
        }

    }
}
