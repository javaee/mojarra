/*
 * $Id: MessagesRenderer.java,v 1.37 2007/08/30 19:29:13 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;

/**
 * <p><B>MessagesRenderer</B> handles rendering for the Messages<p>.
 *
 * @version $Id
 */

public class MessagesRenderer extends HtmlBasicRenderer {


     private static final String[] ATTRIBUTES =
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

        UIMessages messages = (UIMessages) component;
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        // String clientId = ((UIMessages) component).getFor();
        String clientId = null; // PENDING - "for" is actually gone now
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
        RenderKitUtils.renderPassThruAttributes(writer,
                                                component,
                                                ATTRIBUTES);

        while (messageIter.hasNext()) {
            FacesMessage curMessage = (FacesMessage) messageIter.next();

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
            if (showSummary && showDetail && isTooltip) {
                writer.startElement("span", component);
                String title = (String) component.getAttributes().get("title");
                if (title == null || title.length() == 0) {
                    writer.writeAttribute("title", summary, "title");
                }
                writer.flush();
                writer.writeText("\t", component, null);
                wroteTooltip = true;
            }

            if (!wroteTooltip && showSummary) {
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


