/*
 * $Id: GroupRenderer.java,v 1.15 2004/01/20 03:04:09 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * Arbitrary grouping "renderer" that simply renders its children
 * recursively in the <code>encodeEnd()</code> method. 
 *
 * @version $Id: GroupRenderer.java,v 1.15 2004/01/20 03:04:09 craigmcc Exp $
 *  
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

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

	// Render a span around this group if necessary
	String 
	    style = (String) component.getAttributes().get("style"),
	    styleClass = (String) component.getAttributes().get("styleClass");
	ResponseWriter writer = context.getResponseWriter();

	if (spanned(component)) {
	    writer.startElement("span", component);
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

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

	// Render our children recursively
	Iterator kids = getChildren(component);
	while (kids.hasNext()) {
	    encodeRecursive(context, (UIComponent) kids.next());
	}

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

	// Close our span element if necessary
	ResponseWriter writer = context.getResponseWriter();
	if (spanned(component)) {
	    writer.endElement("span");
	}

    }

    /**
     * <p>Return true if we need to render a span element around this group.
     *
     * @param component <code>UIComponent</code> for this group
     */
    private boolean spanned(UIComponent component) {
	if (shouldWriteIdAttribute(component) ||
	    (component.getAttributes().get("style") != null) ||
	    (component.getAttributes().get("styleClass") != null)) {
	    return (true);
	} else {
	    return (false);
	}

    }


}
