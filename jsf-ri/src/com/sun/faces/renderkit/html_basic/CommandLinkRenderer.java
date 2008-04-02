/*
 * $Id: CommandLinkRenderer.java,v 1.38 2005/10/10 16:53:26 rlubke Exp $
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

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;

import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.util.logging.Level;

/**
 * <B>CommandLinkRenderer</B> is a class that renders the current value of
 * <code>UICommand<code> as a HyperLink that acts like a Button.
 */

public class CommandLinkRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
     
    // Separator character

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

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, 
                    "Begin decoding component " + component.getId());
        }

        UICommand command = (UICommand) component;

        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("No decoding necessary since the component " +
                          component.getId() + " is disabled");
            }
            return;
        } 
	
        String 
	    clientId = command.getClientId(context),
	    paramName = getHiddenFieldName(context, command);
	if (null == paramName) {
	    return;
	}
        Map<String,String> requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        String value = requestParameterMap.get(paramName);
        if (value == null || value.equals("") || !clientId.equals(value)) {
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

        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                    "Begin encoding component " + component.getId());
        }

        UICommand command = (UICommand)component;

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
            if ((command.getAttributes().get("disabled")).equals(Boolean.TRUE)) {
                componentDisabled = true;
            }
        }
        if (componentDisabled) {
            renderAsDisabled(context, command);
        } else {
            renderAsActive(context, command);
        }

    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
                                                                                                                        
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
        assert (writer != null);

        //Write Anchor inline elements
                                                                                                                        
        String fieldName = getHiddenFieldName(context, component);
        if (null == fieldName) {
            return;
        }
                                                                                                                        
        boolean componentDisabled = false;
        if (component.getAttributes().get("disabled") != null) {
            if ((component.getAttributes().get("disabled")).equals(Boolean.TRUE)) {
                componentDisabled = true;
            }
        }
                                                                                                                         
        if (componentDisabled) {
            if (shouldWriteIdAttribute(component) ||
                Util.hasPassThruAttributes(component) ||
                (component.getAttributes().get("style") != null) ||
                (component.getAttributes().get("styleClass") != null)) {
                writer.endElement("span");
            }
            return;
        }

        //Done writing Anchor element
        writer.endElement("a");
                                                                                                                        
        renderHiddenFieldsAndScriptIfNecessary(context, writer, component, fieldName);
                                                                                                                        
        UIForm uiform = getMyForm(command);
        if ( uiform == null ) {
            if (logger.isLoggable(Level.WARNING)) {
                 logger.warning("component " + component.getId() +
                          " must be enclosed inside a form ");
            }
            return;
        }
        
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                    "End encoding component " + component.getId());
        }        
    }

    private void renderAsActive(FacesContext context, UICommand command) 
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        String clientId = command.getClientId(context);

        UIForm uiform = getMyForm(command);
        if ( uiform == null ) {
            if (logger.isLoggable(Level.WARNING)) {
                 logger.warning("component " + command.getId() +
                          " must be enclosed inside a form ");
            }
            return;
        }
        String formClientId = uiform.getClientId(context);
        //Write Anchor attributes

        //make link act as if it's a button using javascript
        Param paramList[] = getParamList(context, command);

        writer.startElement("a", command);
        writeIdAttributeIfNecessary(context, writer, command);
        writer.writeAttribute("href", "#", "href");
        Util.renderPassThruAttributes(context, writer, command,
                                      new String[]{"onclick", "target"});
        Util.renderBooleanPassThruAttributes(writer, command);

        // render onclick
        String userOnclick = (String)command.getAttributes().get("onclick");
        StringBuffer sb = new StringBuffer(128);
        boolean userSpecifiedOnclick = (userOnclick != null && !"".equals(userOnclick));
        
        // if user specified their own onclick value, we are going to
        // wrap their js and the injected js each in a function and
        // execute them in a choose statement, if the user didn't specify
        // an onclick, the original logic executes unaffected
        if (userSpecifiedOnclick) {
            sb.append("var a=function(){");
            userOnclick = userOnclick.trim();
            sb.append(userOnclick);
            if (userOnclick.charAt(userOnclick.length()-1) != ';') sb.append(';');
            sb.append("};var b=function(){");
        }
        
        // call the javascript function that clears the all the hidden field
        // parameters in the form.
        sb.append(CLEAR_HIDDEN_FIELD_FN_NAME);
        sb.append('_');
        sb.append(formClientId.replace(NamingContainer.SEPARATOR_CHAR, '_'));
        sb.append("('");
        sb.append(formClientId);
        sb.append("');");
        sb.append("document.forms[");
        sb.append('\'');
        sb.append(formClientId);
        sb.append('\'');
        sb.append("]['");
        sb.append(getHiddenFieldName(context, command));
        sb.append("'].value='");
        sb.append(clientId);
        sb.append("';");
        for (int i = 0, len = paramList.length; i < len; i++) {
            sb.append("document.forms[");
            sb.append('\'');
            sb.append(formClientId);
            sb.append('\'');
            sb.append("]['");
            sb.append(paramList[i].getName());
            sb.append("'].value='");
            sb.append(paramList[i].getValue());
            sb.append("';");
        }
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
        sb.append("].submit()");

        sb.append("; return false;");

        // we need to finish wrapping the injected js then
        if (userSpecifiedOnclick) {
            sb.append("};return (a()==false) ? false : b();");
        }

        writer.writeAttribute("onclick", sb.toString(), "onclick");

        //handle css style class
        String styleClass = (String)
            command.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        
        // render the current value as link text.
        writeValue(command, writer);
        writer.flush();

    }

    private void renderAsDisabled(FacesContext context, UICommand command)
        throws IOException {
                                                                                                                        
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
                                                                                                                        
        if (shouldWriteIdAttribute(command) ||
            Util.hasPassThruAttributes(command) ||
            (command.getAttributes().get("style") != null) ||
            (command.getAttributes().get("styleClass") != null)) {
            writer.startElement("span", command);
        }
        String writtenId = writeIdAttributeIfNecessary(context, writer, command);
        if (null != writtenId) {
            writer.writeAttribute("name", writtenId, "name");
        }
                                                                                                                        
        Util.renderPassThruAttributes(context, writer, command);
        String[] exclude = {"disabled"};
        Util.renderBooleanPassThruAttributes(writer, command, exclude);
                                                                                                                        
                                                                                                                        
        // style if present, rendered as passthru..
        //handle css style class
        String styleClass = (String)
            command.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        // render the current value as span text.
        writeValue(command, writer);
        writer.flush();
    }

    private void writeValue(UICommand command, ResponseWriter writer) 
    throws IOException {
        String label = null;
        Object value = command.getValue();
        if (value != null) {
            label = value.toString();
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Value to be rendered " + value);
        }
        if (label != null && label.length() != 0) {
            writer.write(label);
        }
    }

    private void writeScriptContent(FacesContext context, 
				   ResponseWriter writer,
				   UIComponent component) throws IOException {
	Map<String,Object> requestMap = context.getExternalContext().getRequestMap();
	UIForm myForm = getMyForm(component);
	boolean isXHTML = 
	    requestMap.containsKey(RIConstants.CONTENT_TYPE_IS_XHTML);

	if (null == myForm) {
            return;
	}
	// if the script content has already been rendered for this form
	if (null != myForm.getAttributes().get(DID_RENDER_SCRIPT)){
	    return;
	}
	String formName = myForm.getClientId(context);
	writer.startElement("script", component);
	writer.writeAttribute("type", "text/javascript", "type");
	writer.writeAttribute("language", "Javascript", "language");	
	if (isXHTML) {
	    writer.write("//<![CDATA[\n");
	} else {
            writer.write("<!--\n");
        }
	writer.write("\nfunction ");
	String functionName = (CLEAR_HIDDEN_FIELD_FN_NAME + "_" + formName.replace(NamingContainer.SEPARATOR_CHAR, '_')); 
	writer.write(functionName);
	writer.write("(curFormName) {");
	writer.write("\n  var curForm = document.forms[curFormName];"); 
	writer.write("\n curForm.elements['"); 
	writer.write(getHiddenFieldName(context, component));
	writer.write("'].value = null;");
        Param paramList[] = getParamList(context, component);
        for (int i = 0; i < paramList.length; i++) {
	    writer.write("\n curForm.elements['"); 
	    writer.write(paramList[i].getName());
	    writer.write("'].value = null;");
        }
        String formTarget = (String) myForm.getAttributes().get("target");
	if (formTarget != null && formTarget.length() > 0) {
	    writer.write("\n  curForm.target=");
	    writer.write("'");
	    writer.write(formTarget);
	    writer.write("';");
	}
	writer.write("\n}\n");

	if (isXHTML) {
	    writer.write("//]]>\n");
	} else {
	    writer.write("//-->\n");
        }
	writer.endElement("script");

	// say that we've already rendered the script for this form
	myForm.getAttributes().put(DID_RENDER_SCRIPT, DID_RENDER_SCRIPT);
    }

    /**
     * <p>Render the hidden fields necessary to the execution of this
     * commandLink component.  This method should only take action once
     * per form.  This is achieved by storing a request attribute with
     * the name of the hidden field.  If there is no request attribute
     * with that name, or there is one, but it's a different name then
     * "our" hidden field name, render the hidden field.</p>
     */
    
    private void renderHiddenFieldsAndScriptIfNecessary(FacesContext context,
					       ResponseWriter writer,
					       UIComponent component,
					       String fieldName) throws IOException {
        //Handle hidden fields
	Map<String,Object> requestMap = context.getExternalContext().getRequestMap();
	
	if (null == fieldName) {
	    return;
	}
	String keyName = RIConstants.FACES_PREFIX + fieldName;
	Object keyVal;
	// if the hidden field for this form hasn't yet been rendered
	if ((null == (keyVal = requestMap.get(keyName))) 
	    || (!keyVal.equals(keyName))) {
            
	    writer.startElement("input", component);
	    writer.writeAttribute("type", "hidden", null);
	    writer.writeAttribute("name", fieldName, null);
	    writer.endElement("input");
	    // declare that we have rendered it
	    requestMap.put(keyName, keyName);
        }
        // PENDING(edburns): not sure if the JSFA59 back button problem
        // manifests itself with param children as well...
        ArrayList<String> renderedFields = null;
        // get UIParameter children...
        Param paramList[] = getParamList(context, component);
        if (paramList != null && paramList.length > 0) {            
            renderedFields = (ArrayList<String>)requestMap.get(RENDERED_HIDDEN_FIELDS);           
            if (renderedFields == null) {
                renderedFields = new ArrayList<String>();
            }

            // render any hidden fields that haven't been already for this form.
            // Hidden fields should be rendered only once per form.
            for (int i = 0; i < paramList.length; i++) {
                fieldName = paramList[i].getName();
                keyName = RIConstants.FACES_PREFIX + fieldName;
                int keyLocation = renderedFields.indexOf(keyName);

                if (keyLocation == -1) {
                    writer.startElement("input", component);
                    writer.writeAttribute("type", "hidden", null);
                    writer.writeAttribute("name", fieldName, null);
                    writer.endElement("input");
                    renderedFields.add(keyName);
                }
            }
        }
        
        requestMap.put(RENDERED_HIDDEN_FIELDS, renderedFields);
	writeScriptContent(context, writer, component);
    }

    
    private String getHiddenFieldName(FacesContext context, 
					UIComponent component) {
	UIForm uiform = getMyForm(component);
	if (null == uiform) {
	    return null;
	}
	String formClientId = uiform.getClientId(context);
	return (formClientId + NamingContainer.SEPARATOR_CHAR + 
		UIViewRoot.UNIQUE_ID_PREFIX + "cl");
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

} // end of class CommandLinkRenderer
