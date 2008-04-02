/*
 * $Id: GroupRenderer.java,v 1.25 2005/06/09 22:37:47 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
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
 * @version $Id: GroupRenderer.java,v 1.25 2005/06/09 22:37:47 jayashri Exp $
 */
public class GroupRenderer extends HtmlBasicRenderer {

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

        // Render a span around this group if necessary
        String
            style = (String) component.getAttributes().get("style"),
            styleClass = (String) component.getAttributes().get("styleClass"),
            layout = (String) component.getAttributes().get("layout");
        ResponseWriter writer = context.getResponseWriter();

        if (divOrSpan(component)) {
            if ((layout != null) && (layout.equals("block"))) {
                writer.startElement("div", component);
            } else {
                writer.startElement("span", component);
            }
            writeIdAttributeIfNecessary(context, writer, component);
            if (styleClass != null) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            if (style != null) {
                writer.writeAttribute("style", style, "style");
            }
        }

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
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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

        // Close our span element if necessary
        ResponseWriter writer = context.getResponseWriter();
        String layout = (String)component.getAttributes().get("layout");
        if (divOrSpan(component)) {
            if ((layout != null) && (layout.equals("block"))) {
                writer.endElement("div");
            } else {
                writer.endElement("span");
            }
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"End encoding component " +
                      component.getId());
        }

    }


    /**
     * <p>Return true if we need to render a div or span element around this group.
     *
     * @param component <code>UIComponent</code> for this group
     */
    private boolean divOrSpan(UIComponent component) {
        if (shouldWriteIdAttribute(component) ||
            (component.getAttributes().get("style") != null) ||
            (component.getAttributes().get("styleClass") != null)) {
            return (true);
        } else {
            return (false);
        }

    }


}
