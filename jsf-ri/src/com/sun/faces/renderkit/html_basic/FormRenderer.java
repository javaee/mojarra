/*
 * $Id: FormRenderer.java,v 1.104 2007/07/06 18:21:57 rlubke Exp $
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

// FormRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;

/** <B>FormRenderer</B> is a class that renders a <code>UIForm<code> as a Form. */

public class FormRenderer extends HtmlBasicRenderer {

    private boolean writeStateAtEnd;


    // ------------------------------------------------------------ Constructors


    public FormRenderer() {
        WebConfiguration webConfig = WebConfiguration.getInstance();
        writeStateAtEnd =
             webConfig.isOptionEnabled(
                  BooleanWebContextInitParameter.WriteStateAtFormEnd);

    }

    // ---------------------------------------------------------- Public Methods


    public void decode(FacesContext context, UIComponent component) {

        // Was our form the one that was submitted?  If so, we need to set
        // the indicator accordingly..
        //
        String clientId = component.getClientId(context);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin decoding component " + component.getId());
        }
        Map<String, String> requestParameterMap = context.getExternalContext()
              .getRequestParameterMap();
        if (requestParameterMap.containsKey(clientId)) {
            ((UIForm) component).setSubmitted(true);
        } else {
            ((UIForm) component).setSubmitted(false);
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End decoding component " + component.getId());
        }

    }


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
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Begin encoding component " +
                                    component.getId());
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
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);
        String clientId = component.getClientId(context);
        // since method and action are rendered here they are not added
        // to the pass through attributes in Util class.
        writer.write('\n');
        writer.startElement("form", component);
        writer.writeAttribute("id", clientId, "clientId");
        writer.writeAttribute("name", clientId, "name");
        writer.writeAttribute("method", "post", null);
        writer.writeAttribute("action", getActionStr(context), null);
        String styleClass =
              (String) component.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        String acceptcharset = (String)
              component.getAttributes().get("acceptcharset");
        if (acceptcharset != null) {
            writer.writeAttribute("accept-charset", acceptcharset,
                                  "acceptcharset");
        }

        RenderKitUtils.renderPassThruAttributes(context, writer, component);
        writer.writeText("\n", component, null);

        // this hidden field will be checked in the decode method to
        // determine if this form has been submitted.         
        writer.startElement("input", component);
        writer.writeAttribute("type", "hidden", "type");
        writer.writeAttribute("name", clientId,
                              "clientId");
        writer.writeAttribute("value", clientId, "value");
        writer.endElement("input");
        writer.write('\n');

        if (!writeStateAtEnd) {
            context.getApplication().getViewHandler().writeState(context);
            writer.write('\n');
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
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

        // Render the end tag for form
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);
        if (writeStateAtEnd) {
            context.getApplication().getViewHandler().writeState(context);
        }
        writer.writeText("\n", component, null);
        writer.endElement("form");

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding component " + component.getId());
        }

    }

    // --------------------------------------------------------- Private Methods


    /**
     * @param context FacesContext for the response we are creating
     *
     * @return Return the value to be rendered as the <code>action</code> attribute
     *  of the form generated for this component.
     */
    private String getActionStr(FacesContext context) {

        String viewId = context.getViewRoot().getViewId();
        String actionURL =
              context.getApplication().getViewHandler().
                    getActionURL(context, viewId);
        return (context.getExternalContext().encodeActionURL(actionURL));

    }

} // end of class FormRenderer
