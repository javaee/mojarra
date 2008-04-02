/*
 * $Id: MessageRenderer.java,v 1.68 2006/11/16 23:18:18 rlubke Exp $
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

// MessageRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;

/**
 * <p><B>MessageRenderer</B> handles rendering for the Message<p>.
 *
 * @version $Id
 */

public class MessageRenderer extends HtmlBasicRenderer {


    private OutputMessageRenderer omRenderer = null;

    // ------------------------------------------------------------ Constructors


    public MessageRenderer() {

        omRenderer = new OutputMessageRenderer();

    }

    // ---------------------------------------------------------- Public Methods


    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "component"));
        }
        if (component instanceof UIOutput) {
            omRenderer.encodeBegin(context, component);
            return;
        }

    }


    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "component"));
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

        if (context == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "component"));
        }

        if (component instanceof UIOutput) {
            omRenderer.encodeEnd(context, component);
            return;
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding component " + component.getId());
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
        assert(writer != null);

        String clientId = ((UIMessage) component).getFor();
        //"for" attribute required for Message. Should be taken care of
        //by TLD in JSP case, but need to cover non-JSP case.
        if (clientId == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("'for' attribute cannot be null");
            }
            return;
        }

        clientId = augmentIdReference(context, clientId, component);
        messageIter = getMessageIter(context, clientId, component);


        assert(messageIter != null);
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
        // Default to summary if we have no detail
        detail = (null != (detail = curMessage.getDetail())) ?
                 detail : summary;

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

        String style = (String) component.getAttributes().get("style");
        String styleClass = (String) component.getAttributes().get("styleClass");
        String dir = (String) component.getAttributes().get("dir");
        String lang = (String) component.getAttributes().get("lang");
        String title = (String) component.getAttributes().get("title");

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
        if (styleClass != null
             || style != null
             || dir != null
             || lang != null
             || title != null
             || shouldWriteIdAttribute(component)) {
            writer.startElement("span", component);
            writeIdAttributeIfNecessary(context, writer, component);

            wroteSpan = true;
            if (style != null) {               
                writer.writeAttribute("style", style, "style");
            }
            if (styleClass != null) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            if (dir != null) {
                writer.writeAttribute("dir", dir, "dir");
            }
            if (lang != null) {
                writer.writeAttribute(RenderKitUtils.prefixAttribute("lang", writer),
                     lang,
                     "lang");
            }
            if (title != null) {
                writer.writeAttribute("title", title, "title");
            }

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
            if (title == null || title.length() == 0) {
                writer.writeAttribute("title", summary, "title");
            }
            writer.flush();
            writer.writeText("\t", component, null);
            wroteTooltip = true;
        } else if (wroteSpan) {
            writer.flush();
        }

        if (!wroteTooltip && showSummary) {
            writer.writeText("\t", component, null);
            writer.writeText(summary, component, null);
            writer.writeText(" ", component, null);
        }
        if (showDetail) {
            writer.writeText(detail, component, null);
        }

        if (wroteSpan || wroteTooltip) {
            writer.endElement("span");
        }

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding component " + component.getId());
        }

    }

} // end of class MessageRenderer


