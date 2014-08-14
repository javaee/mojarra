/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// MessageRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.RenderKitUtils;

/**
 * <p><B>MessageRenderer</B> handles rendering for the Message<p>.
 *
 */

public class MessageRenderer extends HtmlBasicRenderer {


    private OutputMessageRenderer omRenderer = null;

    // ------------------------------------------------------------ Constructors


    public MessageRenderer() {

        omRenderer = new OutputMessageRenderer();

    }

    // ---------------------------------------------------------- Public Methods


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (component instanceof UIOutput) {
            omRenderer.encodeBegin(context, component);
        }

    }


    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (component instanceof UIOutput) {
            omRenderer.encodeChildren(context, component);
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (component instanceof UIOutput) {
            omRenderer.encodeEnd(context, component);
            return;
        }

        if (!shouldEncode(component)) {
            return;
        }

        //  If id is user specified, we must render
        boolean mustRender = shouldWriteIdAttribute(component);

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        UIMessage message = (UIMessage) component;

        String clientId = message.getFor();
        //"for" attribute required for Message. Should be taken care of
        //by TLD in JSP case, but need to cover non-JSP case.
        if (clientId == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("'for' attribute cannot be null");
            }
            return;
        }

        clientId = augmentIdReference(clientId, component);
        Iterator messageIter = getMessageIter(context, clientId, component);


        assert(messageIter != null);
        if (!messageIter.hasNext()) {
            if (mustRender) {
                // no message to render, but must render anyway
                writer.startElement("span", component);
                writeIdAttributeIfNecessary(context, writer, component);
                writer.endElement("span");
            } // otherwise, return without rendering
            return;
        }
        FacesMessage curMessage = (FacesMessage) messageIter.next();
        if (curMessage.isRendered() && !message.isRedisplay()) {
            return;
        }
        curMessage.rendered();

        String severityStyle = null;
        String severityStyleClass = null;
        boolean showSummary = message.isShowSummary();
        boolean showDetail = message.isShowDetail();

        // make sure we have a non-null value for summary and
        // detail.
        String summary = (null != (summary = curMessage.getSummary())) ?
                  summary : "";
        // Default to summary if we have no detail
        String detail = (null != (detail = curMessage.getDetail())) ?
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
             || mustRender) {
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

        Object val = component.getAttributes().get("tooltip");
        boolean isTooltip = (val != null) && Boolean.valueOf(val.toString());

        boolean wroteTooltip = false;
        if ((showSummary || showDetail) && isTooltip) {

            if (!wroteSpan) {
                writer.startElement("span", component);
            }            
            if (title == null || title.length() == 0) {
                writer.writeAttribute("title", detail, "title");
            }
            writer.flush();
            writer.writeText("\t", component, null);
            wroteTooltip = true;
        } else if (wroteSpan) {
            writer.flush();
        }

        if (showSummary) {
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

    }

} // end of class MessageRenderer


