/*
 * $Id: MessageRenderer.java,v 1.53 2005/06/09 22:37:48 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// MessageRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;

/**
 * <p><B>MessageRenderer</B> handles rendering for the Message<p>.
 *
 * @version $Id
 */

public class MessageRenderer extends HtmlBasicRenderer {

    //
    // Private/Protected Constants
    //

    // 
    // Ivars
    // 

    private OutputMessageRenderer omRenderer = null;

    //
    // Ctors
    // 

    public MessageRenderer() {
        omRenderer = new OutputMessageRenderer();
    }
   
    //
    // Methods From Renderer
    //

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (component instanceof UIOutput) {
            omRenderer.encodeBegin(context, component);
            return;
        }
    }


    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (component instanceof UIOutput) {
            omRenderer.encodeChildren(context, component);
            return;
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        Iterator messageIter = null;
        FacesMessage curMessage = null;
        ResponseWriter writer = null;

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (component instanceof UIOutput) {
            omRenderer.encodeEnd(context, component);
            return;
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"Begin encoding component " + component.getId());
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
        writer = context.getResponseWriter();
        assert (writer != null);

        String clientId = ((UIMessage) component).getFor();
        //"for" attribute required for Message. Should be taken care of
        //by TLD in JSP case, but need to cover non-JSP case.
        if (clientId == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("'for' attribute cannot be null");
            }
            return;
        }

        messageIter = getMessageIter(context, clientId, component);


        assert (messageIter != null);
        if (!messageIter.hasNext()) {
            //no messages to render
            return;
        }
        curMessage = (FacesMessage) messageIter.next();

        String
            summary = null,
            detail = null,
            severityStyle = null,
            severityStyleClass = null;
        boolean
            showSummary = ((UIMessage) component).isShowSummary(),
            showDetail = ((UIMessage) component).isShowDetail();

        // make sure we have a non-null value for summary and
        // detail.
        summary = (null != (summary = curMessage.getSummary())) ?
            summary : "";
        detail = (null != (detail = curMessage.getDetail())) ?
            detail : "";

        if (curMessage.getSeverity() == FacesMessage.SEVERITY_INFO) {
            severityStyle =
                (String) component.getAttributes().get("infoStyle");
            severityStyleClass = (String)
                component.getAttributes().get("infoClass");
        } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_WARN) {
            severityStyle =
                (String) component.getAttributes().get("warnStyle");
            severityStyleClass = (String)
                component.getAttributes().get("warnClass");
        } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_ERROR) {
            severityStyle =
                (String) component.getAttributes().get("errorStyle");
            severityStyleClass = (String)
                component.getAttributes().get("errorClass");
        } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_FATAL) {
            severityStyle =
                (String) component.getAttributes().get("fatalStyle");
            severityStyleClass = (String)
                component.getAttributes().get("fatalClass");
        }

        String
            style = (String) component.getAttributes().get("style"),
            styleClass = (String) component.getAttributes().get("styleClass");

        // if we have style and severityStyle
        if ((style != null) && (severityStyle != null)) {
            // severityStyle wins
            style = severityStyle;
        }
        // if we have no style, but do have severityStyle
        else if ((style == null) && (severityStyle != null)) {
            // severityStyle wins
            style = severityStyle;
        }

        // if we have styleClass and severityStyleClass
        if ((styleClass != null) && (severityStyleClass != null)) {
            // severityStyleClass wins
            styleClass = severityStyleClass;
        }
        // if we have no styleClass, but do have severityStyleClass
        else if ((styleClass == null) && (severityStyleClass != null)) {
            // severityStyleClass wins
            styleClass = severityStyleClass;
        }

        //Done intializing local variables. Move on to rendering.

        boolean wroteSpan = false;
        boolean wroteTable = false;

        if (styleClass != null || style != null ||
            shouldWriteIdAttribute(component) || 
	    Util.hasPassThruAttributes(component)) {
            writer.startElement("span", component);
            writeIdAttributeIfNecessary(context, writer, component);

            wroteSpan = true;
            if (styleClass != null) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
	    // style is rendered as a passthru attribute
	    Util.renderPassThruAttributes(context, writer, component);
        }

        Object tooltip = component.getAttributes().get("tooltip");
        boolean isTooltip = false;
        if (tooltip instanceof Boolean) {
            //if it's not a boolean can ignore it
            isTooltip = ((Boolean) tooltip).booleanValue();
        }

        boolean wroteTooltip = false;
        if (showSummary && showDetail && isTooltip) {

            if (!wroteSpan) {
                writer.startElement("span", component);
            }
            writer.writeAttribute("title", summary, "title");
            writer.flush();
            writer.writeText("\t", null);
            wroteTooltip = true;
        } else if (wroteSpan) {
            writer.flush();
        }

        if (!wroteTooltip && showSummary) {
            writer.writeText("\t", null);
            writer.writeText(summary, null);
            writer.writeText(" ", null);
        }
        if (showDetail) {
            writer.writeText(detail, null);
        }

        if (wroteSpan || wroteTooltip) {
            writer.endElement("span");
        }

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"End encoding component " + component.getId());
        }
    }

} // end of class MessageRenderer


