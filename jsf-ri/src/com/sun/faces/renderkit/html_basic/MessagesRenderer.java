/*
 * $Id: MessagesRenderer.java,v 1.25 2006/03/24 16:43:03 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// MessagesRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.renderkit.RenderKitUtils;

import java.util.logging.Level;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;

/**
 * <p><B>MessagesRenderer</B> handles rendering for the Messages<p>.
 *
 * @version $Id
 */

public class MessagesRenderer extends HtmlBasicRenderer {

    //
    // Prviate/Protected Constants
    //
   
    //
    // Methods From Renderer
    //

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        Iterator messageIter = null;
        FacesMessage curMessage = null;
        ResponseWriter writer = null;

        if (context == null || component == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,"End encoding component " + component.getId());
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
        writer = context.getResponseWriter();
        assert (writer != null);

        // String clientId = ((UIMessages) component).getFor();
        String clientId = null; // PENDING - "for" is actually gone now
        // if no clientId was included
        if (clientId == null) {
            // and the author explicitly only wants global messages
            if (((UIMessages) component).isGlobalOnly()) {
                // make it so only global messages get displayed.
                clientId = "";
            }
        }

        //"for" attribute optional for Messages
        messageIter = getMessageIter(context, clientId, component);

        assert (messageIter != null);

        String layout = (String) component.getAttributes().get("layout");
        boolean showSummary = ((UIMessages)component).isShowSummary();
        boolean showDetail = ((UIMessages)component).isShowDetail();
        String style = (String) component.getAttributes().get("style");
        String styleClass = (String) component.getAttributes().get(
                    "styleClass");

        boolean wroteTable = false;

        //For layout attribute of "table" render as HTML table.
        //If layout attribute is not present, or layout attribute
        //is "list", render as HTML list. 
        if ((layout != null) && (layout.equals("table"))) {
            writer.startElement("table", component);
            wroteTable = true;
        } else {
            writer.startElement("ul", component);
        }

        //Render "table" or "ul" level attributes.
        writeIdAttributeIfNecessary(context, writer, component);
        if (null != styleClass) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        // style is rendered as a passthru attribute
        RenderKitUtils.renderPassThruAttributes(context, writer, component);

        while (messageIter.hasNext()) {
            curMessage = (FacesMessage) messageIter.next();

            String
                summary = null,
                detail = null,
                severityStyle = null,
                severityStyleClass = null;

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

            //Done intializing local variables. Move on to rendering.

            if (wroteTable) {
                writer.startElement("tr", component);
            } else {
                writer.startElement("li", component);
            }

            if (severityStyle != null) {
                style = severityStyle;
                writer.writeAttribute("style", style, "style");
            }
            if (severityStyleClass != null) {
                styleClass = severityStyleClass;
                writer.writeAttribute("class", styleClass, "styleClass");
            }

            if (wroteTable) {
                writer.startElement("td", component);
            }
            
            Object tooltip = component.getAttributes().get("tooltip");
            boolean isTooltip = false;
            if (tooltip instanceof Boolean) {
                //if it's not a boolean can ignore it
                isTooltip = ((Boolean) tooltip).booleanValue();
            }

            boolean wroteTooltip = false;
            if (showSummary && showDetail && isTooltip) {
                writer.startElement("span", component);
                String title = (String) component.getAttributes().get("title");
                if (title == null || title.length() == 0) {
                    writer.writeAttribute("title", summary, "title");
                }               
                writer.flush();
                writer.writeText("\t", null);
                wroteTooltip = true;
            }

            if (!wroteTooltip && showSummary) {
                writer.writeText("\t", null);
                writer.writeText(summary, null);
                writer.writeText(" ", null);
            }
            if (showDetail) {
                writer.writeText(detail, null);
            }

            if (wroteTooltip) {
                writer.endElement("span");
            }

            //close table row if present
            if (wroteTable) {
                writer.endElement("td");
                writer.endElement("tr");
            } else {
                writer.endElement("li");
            }

        } //messageIter

        //close table if present
        if (wroteTable) {
            writer.endElement("table");
        } else {
            writer.endElement("ul");
        }
    }

} // end of class MessagesRenderer


