/*
 * $Id: ButtonRenderer.java,v 1.2 2005/08/22 22:10:43 ofung Exp $
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

// ButtonRenderer.java

package com.sun.faces.systest.render;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + component.getId());
        }
        
        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
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
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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

        Util.renderPassThruAttributes(context, writer, component, 
				      new String[]{"onclick"});
        Util.renderBooleanPassThruAttributes(writer, component);

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
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
                    // PENDING I18N
                    log.debug("Can't write ID attribute" + e.getMessage());
                }
            }
        }
    }


} // end of class ButtonRenderer
