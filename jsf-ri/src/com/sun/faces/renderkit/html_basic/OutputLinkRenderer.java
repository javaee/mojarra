/*
 * $Id: OutputLinkRenderer.java,v 1.21 2005/07/29 15:15:51 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// OutputLinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.util.Util;

import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * <B>OutputLinkRenderer</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OutputLinkRenderer.java,v 1.21 2005/07/29 15:15:51 rogerk Exp $
 */

public class OutputLinkRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //

    // Separator character

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
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // take no action, this is an Output component.
        if (logger.isLoggable(Level.FINE)) {
             logger.fine("No decoding necessary since the component "
                      + component.getId() +
                      " is not an instance or a sub class of UIInput");
        }
        return;
    }


    public boolean getRendersChildren() {
        return true;
    }


    protected Object getValue(UIComponent component) {
        return ((UIOutput) component).getValue();
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Begin encoding component " + component.getId());
        }

        UIOutput output = (UIOutput) component;
        boolean componentDisabled = false;
        if (output.getAttributes().get("disabled") != null) {
            if ((output.getAttributes().get("disabled")).equals(Boolean.TRUE)) {
                componentDisabled = true;
            }
        }
        if (componentDisabled) {
            renderAsDisabled(context, output);
        } else {
            renderAsActive(context, output);
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("End encoding component "
                          + component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.encodeBegin(context);
            if (kid.getRendersChildren()) {
                kid.encodeChildren(context);
            }
            kid.encodeEnd(context);
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
                                                                                                                          
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"End encoding " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
           if (logger.isLoggable(Level.FINE)) {
                 logger.fine("End encoding component "
                          + component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
                                                                                                                          
        boolean componentDisabled = false;
        if (component.getAttributes().get("disabled") != null) {
            if ((component.getAttributes().get("disabled")).equals(Boolean.TRUE)) {
                componentDisabled = true;
            }
        }

        if (componentDisabled) {
            if (shouldWriteIdAttribute(component) ||
                Util.hasPassThruAttributes(component) ||
                (component.getAttributes().get("style") != null) ||
                (component.getAttributes().get("styleClass") != null)) {
                writer.endElement("span");
            }
        } else {
            //Write Anchor inline elements
            //Done writing Anchor element
            writer.endElement("a");
        }

        return;
    }

    private void renderAsActive(FacesContext context, UIOutput component) 
        throws IOException {
 
        String hrefVal = getCurrentValue(context, component);
        if (logger.isLoggable(Level.FINE)) {
             logger.fine("Value to be rendered " + hrefVal);
        }

        // suppress rendering if "rendered" property on the output is
        // false
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("End encoding component "
                          + component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        writer.startElement("a", component);
        String writtenId = writeIdAttributeIfNecessary(context, writer, component);
        if (null != writtenId) {
            writer.writeAttribute("name", writtenId, "name");
        }
        // render an empty value for href if it is not specified
        if (null == hrefVal || 0 == hrefVal.length()) {
            hrefVal = "";
        }

        //Write Anchor attributes

        LinkRenderer.Param paramList[] = getParamList(context, component);
        int
            i = 0,
            len = paramList.length;
        StringBuffer sb = new StringBuffer();
        sb.append(hrefVal);
        if (0 < len) {
            sb.append("?");
        }
        for (i = 0; i < len; i++) {
            if (0 != i) {
                sb.append("&");
            }
            sb.append(paramList[i].getName());
            sb.append("=");
            sb.append(paramList[i].getValue());
        }
        writer.writeURIAttribute("href",
                                 context.getExternalContext()
                                 .encodeResourceURL(sb.toString()),
                                 "href");
        Util.renderPassThruAttributes(context, writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);

        //handle css style class
        String styleClass = (String)
            component.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        writer.flush();
    }

    private void renderAsDisabled(FacesContext context, UIOutput component) 
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        if (shouldWriteIdAttribute(component) ||
            Util.hasPassThruAttributes(component) ||
            (component.getAttributes().get("style") != null) ||
            (component.getAttributes().get("styleClass") != null)) {
            writer.startElement("span", component);
        }
        String writtenId = writeIdAttributeIfNecessary(context, writer, component);
        if (null != writtenId) {
            writer.writeAttribute("name", writtenId, "name");
        }

        Util.renderPassThruAttributes(context, writer, component);
        String[] exclude = {"disabled"};
        Util.renderBooleanPassThruAttributes(writer, component, exclude);
                                                                                                                          
        // style if present, rendered as passthru..
        //handle css style class
        String styleClass = (String)
            component.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        writer.flush();
    }

} // end of class OutputLinkRenderer
