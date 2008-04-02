/*
 * $Id: OutputMessageRenderer.java,v 1.1 2003/11/10 21:28:38 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// OutputMessageRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.UIParameter;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;

import org.mozilla.util.Assert;

/**
 *
 *  <B>OutputMessageRenderer</B> is a class that renderes UIOutput
 *
 *
 * @version $Id
 * 
 */

public class OutputMessageRenderer extends HtmlBasicRenderer {
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

    public OutputMessageRenderer() {
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
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        String 
	    currentValue = null,
	    style = (String) component.getAttributes().get("style"),
	    styleClass = (String) component.getAttributes().get("styleClass");

        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        Object currentObj = ((ValueHolder)component).getValue();
        if ( currentObj != null) {
            if (currentObj instanceof String) {
                currentValue = (String)currentObj;
            } else {
                currentValue = currentObj.toString();
            }
        }

        ArrayList parameterList = new ArrayList();

        // get UIParameter children...

        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();

            //PENDING(rogerk) ignore if child is not UIParameter?

            if (!(kid instanceof UIParameter)) {
                continue;
            }

            parameterList.add(((ValueHolder)kid).getValue());
        }

        String message = null;

        //PENDING(rogerk) if string contains "{" char and enclosing "}"
        // two char positions later (ex: "{0}") assume it has
        // something like "{0}", in which case do the message format.

        int i = 0;
        if ((-1 != (i = currentValue.indexOf('{'))) && 
            (currentValue.charAt(i + 2) == '}') && 
            (parameterList.size() > 0)) {
            Object[] params = parameterList.toArray();
            message = MessageFormat.format(currentValue, params);
        } else {
            message = currentValue;
        }
                
	if (null != styleClass || null != style) {
	    writer.startElement("span", component);
	    if (null != styleClass) {
		writer.writeAttribute("class", styleClass, "styleClass");
	    }
	    if (null != style) {
		writer.writeAttribute("style", style, "style");
	    }
	}
        writer.writeText(message, null);
	if (null != styleClass) {
	    writer.endElement("span");
	}
    }

} // end of class OutputMessageRenderer
