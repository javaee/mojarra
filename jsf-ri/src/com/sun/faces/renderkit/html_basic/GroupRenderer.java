/*
 * $Id: GroupRenderer.java,v 1.24 2005/05/16 20:16:26 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;

/**
 * Arbitrary grouping "renderer" that simply renders its children
 * recursively in the <code>encodeEnd()</code> method.
 *
 * @version $Id: GroupRenderer.java,v 1.24 2005/05/16 20:16:26 rlubke Exp $
 */
public class GroupRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    private static final Log log = LogFactory.getLog(GroupRenderer.class);

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
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " +
                      component.getId());
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
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding children " + component.getId());
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

        // Render our children recursively
        Iterator kids = getChildren(component);
        while (kids.hasNext()) {
            encodeRecursive(context, (UIComponent) kids.next());
        }
        if (log.isTraceEnabled()) {
            log.trace("End encoding children " + component.getId());
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
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
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
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " +
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
