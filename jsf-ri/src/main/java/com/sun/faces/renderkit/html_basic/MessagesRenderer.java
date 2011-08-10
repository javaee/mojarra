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

// MessagesRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;

/**
 * <p><B>MessagesRenderer</B> handles rendering for the Messages<p>.
 *
 */

public class MessagesRenderer extends HtmlBasicRenderer {


     private static final Attribute[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.MESSAGESMESSAGES);


    // ---------------------------------------------------------- Public Methods


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        //  If id is user specified, we must render
        boolean mustRender = shouldWriteIdAttribute(component);

        UIMessages messages = (UIMessages) component;
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String clientId = ((UIMessages) component).getFor();
        // if no clientId was included
        if (clientId == null) {
            // and the author explicitly only wants global messages
            if (messages.isGlobalOnly()) {
                // make it so only global messages get displayed.
                clientId = "";
            }
        }

        //"for" attribute optional for Messages
        Iterator messageIter = getMessageIter(context, clientId, component);

        assert(messageIter != null);
        
        if (!messageIter.hasNext()) {
            if (mustRender) {
                // no message to render, but must render anyway
                // but if we're writing the dev stage messages,
                // only write it if messages exist
                if ("javax_faces_developmentstage_messages".equals(component.getId())) {
                    return;
                }
                writer.startElement("div", component);
                writeIdAttributeIfNecessary(context, writer, component);
                writer.endElement("div");
            } // otherwise, return without rendering
            return;
        }

        String layout = (String) component.getAttributes().get("layout");
        boolean showSummary = messages.isShowSummary();
        boolean showDetail = messages.isShowDetail();
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
        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                component,
                                                ATTRIBUTES);

        while (messageIter.hasNext()) {
            FacesMessage curMessage = (FacesMessage) messageIter.next();
            if (curMessage.isRendered() && !messages.isRedisplay()) {
                continue;
            }
            curMessage.rendered();

            String severityStyle = null;
            String severityStyleClass = null;

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
            } else
            if (curMessage.getSeverity() == FacesMessage.SEVERITY_ERROR) {
                severityStyle =
                      (String) component.getAttributes().get("errorStyle");
                severityStyleClass = (String)
                      component.getAttributes().get("errorClass");
            } else
            if (curMessage.getSeverity() == FacesMessage.SEVERITY_FATAL) {
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
                writer.writeAttribute("style", severityStyle, "style");
            }
            if (severityStyleClass != null) {
                styleClass = severityStyleClass;
                writer.writeAttribute("class", styleClass, "styleClass");
            }

            if (wroteTable) {
                writer.startElement("td", component);
            }

            Object val = component.getAttributes().get("tooltip");
            boolean isTooltip = (val != null) && Boolean.valueOf(val.toString());

            boolean wroteTooltip = false;
            if (isTooltip) {
                writer.startElement("span", component);
                String title = (String) component.getAttributes().get("title");
                if (title == null || title.length() == 0) {
                    writer.writeAttribute("title", detail, "title");
                }
                writer.flush();
                writer.writeText("\t", component, null);
                wroteTooltip = true;
            }

            if (showSummary) {
                writer.writeText("\t", component, null);
                writer.writeText(summary, component, null);
                writer.writeText(" ", component, null);
            }
            if (showDetail) {
                writer.writeText(detail, component, null);
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


