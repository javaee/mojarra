/*
 * $Id: CommandLinkRenderer.java,v 1.52 2006/07/25 21:06:03 rlubke Exp $
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

// CommandLinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <B>CommandLinkRenderer</B> is a class that renders the current value of
 * <code>UICommand<code> as a HyperLink that acts like a Button.
 */

public class CommandLinkRenderer extends LinkRenderer {
    
    private static final String SCRIPT_STATE = RIConstants.FACES_PREFIX +
                                               "scriptState";     

    public void decode(FacesContext context, UIComponent component) {

        if (context == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }
        if (component == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "component"));
        }

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin decoding component " + component.getId());
        }

        UICommand command = (UICommand) component;

        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOrReadonly(component)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("No decoding necessary since the component " +
                            component.getId() + " is disabled");
            }
            return;
        }       
              
        String clientId = command.getClientId(context);
        if (!context.getExternalContext().getRequestParameterMap()
              .containsKey(clientId)) {
            return;
        }
        ActionEvent actionEvent = new ActionEvent(component);
        component.queueEvent(actionEvent);

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("This command resulted in form submission " +
                        " ActionEvent queued " + actionEvent);
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End decoding component " + component.getId());
        }
    }

    public boolean getRendersChildren() {
        return true;
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
            logger.log(Level.FINER,
                       "Begin encoding component " + component.getId());
        }

        UICommand command = (UICommand) component;

        // suppress rendering if "rendered" property on the command is
        // false.
        if (!command.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " + component.getId() +
                            " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }

        boolean componentDisabled = false;
        if (command.getAttributes().get("disabled") != null) {
            if ((command.getAttributes().get("disabled"))
                  .equals(Boolean.TRUE)) {
                componentDisabled = true;
            }
        }
        
        String formClientId = getFormClientId(component, context);
        if (formClientId == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("component '" + component.getId() +
                               "' must be enclosed inside a form ");
            }
        }

        if (componentDisabled || formClientId == null) {
            renderAsDisabled(context, command);
        } else {
            if (!hasScriptBeenRendered(context, formClientId)) {
                RenderKitUtils
                      .renderAddParamToFormJavaScript(formClientId,
                                                      context.getResponseWriter());
                setScriptAsRendered(context, formClientId);
            }
            renderAsActive(context, command);
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
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " + component.getId() +
                            " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }
        for (UIComponent kid : component.getChildren()) {
            kid.encodeBegin(context);
            if (kid.getRendersChildren()) {
                kid.encodeChildren(context);
            }
            kid.encodeEnd(context);
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding children " + component.getId());
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

        UICommand command = (UICommand) component;

        // suppress rendering if "rendered" property on the command is
        // false.
        if (!command.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " + component.getId() +
                            " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);
        String formClientId = getFormClientId(component, context);
        if (formClientId == null) {
            writer.write(MessageUtils.getExceptionMessageString(
                  MessageUtils.COMMAND_LINK_NO_FORM_MESSAGE_ID));
            writer.endElement("span");
            return;
        }

        //Write Anchor inline elements

        boolean componentDisabled = false;
        if (component.getAttributes().get("disabled") != null) {
            if ((component.getAttributes().get("disabled"))
                  .equals(Boolean.TRUE)) {
                componentDisabled = true;
            }
        }

        if (componentDisabled) {

            writer.endElement("span");

            return;
        }

        //Done writing Anchor element
        writer.endElement("a");       
         
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding component " + component.getId());
        }
    }

    protected void renderAsActive(FacesContext context, UIComponent command)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);       
        String formClientId = getFormClientId(command, context);
        if (formClientId == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("component " + command.getId() +
                               " must be enclosed inside a form ");
            }
            return;
        }      

        //make link act as if it's a button using javascript        
        writer.startElement("a", command);
        writeIdAttributeIfNecessary(context, writer, command);
        writer.writeAttribute("href", "#", "href");
        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                command,
                                                new String[]{"target",
                                                             "onclick"});
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, command);

        // render onclick
        String userOnclick = (String) command.getAttributes().get("onclick");
        StringBuffer sb = new StringBuffer(128);
        boolean userSpecifiedOnclick =
              (userOnclick != null && !"".equals(userOnclick));

        // if user specified their own onclick value, we are going to
        // wrap their js and the injected js each in a function and
        // execute them in a choose statement, if the user didn't specify
        // an onclick, the original logic executes unaffected
        if (userSpecifiedOnclick) {
            sb.append("var a=function(){");
            userOnclick = userOnclick.trim();
            sb.append(userOnclick);
            if (userOnclick.charAt(userOnclick.length() - 1) != ';') {
                sb.append(';');
            }
            sb.append("};var b=function(){");
        }

        Param[] params = getParamList(command);
        String commandClientId = command.getClientId(context);
        sb.append(
              RenderKitUtils.getCommandLinkParamScript(formClientId,
                                                       commandClientId,
                                                       params));       
       
        // Set the target attribute on the form element using javascript.
        // Because we treat commandLink as a button,setting target on it,
        // will not have the desired effect since we "return false" for 
        // onclick which would essentially cancel the click.
        String target = (String) command.getAttributes().get("target");
        if (target != null && target.trim().length() > 0) {
            sb.append(" document.forms[");
            sb.append('\'');
            sb.append(formClientId);
            sb.append('\'');
            sb.append("].target='");
            sb.append(target);
            sb.append("';");
        }
        sb.append(" document.forms[");
        sb.append('\'');
        sb.append(formClientId);
        sb.append('\'');
        sb.append("].submit(); return false;");                              

        // we need to finish wrapping the injected js then
        if (userSpecifiedOnclick) {
            sb.append("};return (a()==false) ? false : b();");
        }

        writer.writeAttribute("onclick", sb.toString(), "onclick");

        writeCommonLinkAttributes(writer, command);

        // render the current value as link text.
        writeValue(command, writer);
        writer.flush();

    }


    private UIForm getMyForm(UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }
        if (null == parent) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("component " + component.getId() +
                               " must be enclosed inside a form ");
            }
        }

        return (UIForm) parent;
    }


    /**
     * @param context the <code>FacesContext</code> for the current request
     * @param formClientId the client ID of the form
     * @return <code>true</code> If the <code>add/remove</code> javascript
     *  has been rendered, otherwise <code>false</code>
     */
    private static boolean hasScriptBeenRendered(FacesContext context,
                                                 String formClientId) {

        Map scriptState = (Map)
              context.getExternalContext().getRequestMap()
                    .get(SCRIPT_STATE);
        return (scriptState != null && scriptState.containsKey(formClientId));

    }

    /**
     * <p>Set a flag to indicate that the <code>add/remove</code> javascript
     *  has been rendered for the current form.
     * @param context the <code>FacesContext</code> of the current request
     * @param formClientId the form client ID
     */
    @SuppressWarnings("unchecked")
    private static void setScriptAsRendered(FacesContext context,
                                            String formClientId) {
        
        Map requestMap = context.getExternalContext().getRequestMap();
        Map scriptState = (Map) requestMap.get(SCRIPT_STATE);
        if (scriptState == null) {
            scriptState = new HashMap(2, 1.0f);
            requestMap.put(SCRIPT_STATE, scriptState);
        }
        scriptState.put(formClientId, Boolean.TRUE);
        
    }


    /**
     * <p>Utility method to return the client ID of the parent form.</p>
     * @param component typically a command component
     * @param context the <code>FacesContext</code> for the current request
     * @return the client ID of the parent form, if any
     */
    private String getFormClientId(UIComponent component,
                                   FacesContext context) {
        
        UIForm form = getMyForm(component);
        if (form != null) {
            return form.getClientId(context);
        }
        
        return null;
        
    }
       

} // end of class CommandLinkRenderer
