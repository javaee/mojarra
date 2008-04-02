/*
 * $Id: MessageRenderer.java,v 1.27 2003/09/08 20:10:09 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// MessageRenderer.java

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
 *  <B>MessageRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: MessageRenderer.java,v 1.27 2003/09/08 20:10:09 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class MessageRenderer extends HtmlBasicRenderer {
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

    public MessageRenderer() {
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
        String currentValue = null;
	String styleClass = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        Object currentObj = ((ValueHolder)component).currentValue(context);
        if ( currentObj != null) {
            if (currentObj instanceof String) {
                currentValue = (String)currentObj;
            } else {
                currentValue = currentObj.toString();
            }
        }

        if (currentValue == null) {
            try {
                currentValue = getKeyAndLookupInBundle(context, 
                    component, "key");
            } catch (java.util.MissingResourceException e) {
                // Do nothing since the absence of a resource is not an
                // error.
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

            parameterList.add(((ValueHolder)kid).currentValue(context));
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
                
	if (null != (styleClass = (String) 
		     component.getAttribute("styleClass"))) {
	    writer.startElement("span", component);
	    writer.writeAttribute("class", styleClass, "styleClass");
	}
        writer.writeText(message, null);
	if (null != styleClass) {
	    writer.endElement("span");
	}
    }

} // end of class MessageRenderer
