/*
 * $Id: OutputMessageRenderer.java,v 1.20 2005/06/09 22:37:48 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// OutputMessageRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * <B>OutputMessageRenderer</B> is a class that renderes UIOutput
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
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Begin encoding component " + component.getId());
        }
        
        String
            currentValue = null,
            style = (String) component.getAttributes().get("style"),
            styleClass = (String) component.getAttributes().get("styleClass");

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("End encoding component " + component.getId() +
                          " since rendered attribute is set to false");
            }
            return;
        }
        Object currentObj = ((ValueHolder) component).getValue();
        if (currentObj != null) {
            if (currentObj instanceof String) {
                currentValue = (String) currentObj;
            } else {
                currentValue = currentObj.toString();
            }
        } else {
            // if the value is null, do not output anything.
            return;
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

            parameterList.add(((UIParameter) kid).getValue());
        }

        // If at least one substitution parameter was specified,
        // use the string as a MessageFormat instance.
        String message = null;
        if (parameterList.size() > 0) {
            message = MessageFormat.format
                (currentValue, parameterList.toArray
                               (new Object[parameterList.size()]));
        } else {
            message = currentValue;
        }

        boolean wroteSpan = false;
        if (null != styleClass || null != style ||
            Util.hasPassThruAttributes(component) ||
            shouldWriteIdAttribute(component)) {
            writer.startElement("span", component);
            writeIdAttributeIfNecessary(context, writer, component);
            wroteSpan = true;

            if (null != styleClass) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            // style is rendered as a passthru attribute
            Util.renderPassThruAttributes(context, writer, component);
            Util.renderBooleanPassThruAttributes(writer, component);
        }
        Boolean escape = Boolean.TRUE;
        Object val = component.getAttributes().get("escape");
        if (val != null) {
            if (val instanceof Boolean) {
                escape = (Boolean) val;
            } else if (val instanceof String) {
                try {
                    escape = Boolean.valueOf((String) val);
                } catch (Throwable e) {
                }
            }
        }
        if (escape.booleanValue()) {
            writer.writeText(message, "value");
        } else {
            writer.write(message);
        }
        if (wroteSpan) {
            writer.endElement("span");
        }
        
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"End encoding component " + component.getId());
        }
    }

} // end of class OutputMessageRenderer
