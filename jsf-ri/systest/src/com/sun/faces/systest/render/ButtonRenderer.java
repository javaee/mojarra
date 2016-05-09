/*
 * $Id: ButtonRenderer.java,v 1.11 2007/07/10 18:46:52 rlubke Exp $
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

// ButtonRenderer.java

package com.sun.faces.systest.render;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.Map;

/**
 * <B>ButtonRenderer</B> is a class that renders the current value of
 * <code>UICommand<code> as a Button.
 */

public class ButtonRenderer extends Renderer {

    private static final String[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.COMMANDBUTTON);

    public static final String CLEAR_HIDDEN_FIELD_FN_NAME = 
         "clearFormHiddenParams";
    public static final String FORM_CLIENT_ID_ATTR = 
         "com.sun.faces.FORM_CLIENT_ID_ATTR";

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(ButtonRenderer.class);

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
        if (context == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "component"));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + component.getId());
        }
        
        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOrReadonly(component)) {
            if (log.isTraceEnabled()) {
                log.trace("No decoding necessary since the component " +
                          component.getId() + " is disabled");
            }
            return;
        } 


        // Was our command the one that caused this submission?
        // we don' have to worry about getting the value from request parameter
        // because we just need to know if this command caused the submission. We
        // can get the command name by calling currentValue. This way we can 
        // get around the IE bug.
        String clientId = component.getClientId(context);
        Map requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        String value = (String) requestParameterMap.get(clientId);
        if (value == null) {
            if (requestParameterMap.get(clientId + ".x") == null &&
                requestParameterMap.get(clientId + ".y") == null) {
                return;
            }
        }

        String type = (String) component.getAttributes().get("type");
        if ((type != null) && (type.toLowerCase().equals("reset"))) {
            return;
        }
        ActionEvent actionEvent = new ActionEvent(component);
        component.queueEvent(actionEvent);

        if (log.isDebugEnabled()) {
            log.debug("This command resulted in form submission " +
                      " ActionEvent queued " + actionEvent);
        }
        if (log.isTraceEnabled()) {
            log.trace("End decoding component " + component.getId());
        }
        return;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "component"));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since rendered attribute is set to false ");
            }
            return;
        }
        
        // Which button type (SUBMIT, RESET, or BUTTON) should we generate?
        String type = (String) component.getAttributes().get("type");
        String styleClass = null;
        if (type == null) {
            type = "submit";
            // This is needed in the decode method
            component.getAttributes().put("type", type);
        }

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        String label = "";
        Object value = ((UICommand) component).getValue();
        if (value != null) {
            label = value.toString();
        }
        String imageSrc = (String) component.getAttributes().get("image");
        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
	String clientId = component.getClientId(context);
        if (imageSrc != null) {
            writer.writeAttribute("type", "image", "type");
            writer.writeURIAttribute("src", src(context, imageSrc), "image");
            writer.writeAttribute("name", clientId, "clientId");
        } else {
            writer.writeAttribute("type", type.toLowerCase(), "type");
            writer.writeAttribute("name", clientId, "clientId");
            writer.writeAttribute("value", label, "value");
        }
        
        // look up the clientId of the form in request scope to arrive the name of
        // the javascript function to invoke from the onclick event handler.
        // PENDING (visvan) we need to fix this dependency between the renderers.
        // This solution is only temporary.
        Map requestMap = context.getExternalContext().getRequestMap();
        String formClientId = (String)requestMap.get(FORM_CLIENT_ID_ATTR);
        
        StringBuffer sb = new StringBuffer();
        // call the javascript function that clears the all the hidden field
        // parameters in the form.
        sb.append(CLEAR_HIDDEN_FIELD_FN_NAME);
        if (formClientId != null) {
            sb.append("_" + formClientId.replace(NamingContainer.SEPARATOR_CHAR, '_'));
        }
        sb.append("(this.form.id);");
        // append user specified script for onclick if any.
        String onclickAttr = (String)component.getAttributes().get("onclick");
        if (onclickAttr != null && onclickAttr.length() != 0) {
            sb.append(onclickAttr);
            
        }
        writer.writeAttribute("onclick", sb.toString(), null);

        RenderKitUtils.renderPassThruAttributes(
              writer,
                                                component,
                                                ATTRIBUTES);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        if (null != (styleClass = (String)
            component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        writer.endElement("input");
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "component"));
        }
    }

    //
    // General Methods
    //
       
    private String src(FacesContext context, String value) {
        if (value == null) {
            return "";
        }
        value = context.getApplication().getViewHandler().
            getResourceURL(context, value);
        return (context.getExternalContext().encodeResourceURL(value));
    }
                                                                                                          
    private boolean shouldWriteIdAttribute(UIComponent component) {
        String id;
        return (null != (id = component.getId()) &&
            !id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX));
    }

    private void writeIdAttributeIfNecessary(FacesContext context,
                                               ResponseWriter writer,
                                               UIComponent component) {
        String id;
        if (shouldWriteIdAttribute(component)) {
            try {
                writer.writeAttribute("id", component.getClientId(context),
                                      "id");
            } catch (IOException e) {
                if (log.isDebugEnabled()) {                   
                    log.debug("Can't write ID attribute" + e.getMessage());
                }
            }
        }
    }


} // end of class ButtonRenderer
