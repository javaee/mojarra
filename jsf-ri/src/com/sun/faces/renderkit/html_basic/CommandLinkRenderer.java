/*
 * $Id: CommandLinkRenderer.java,v 1.27 2005/04/15 21:19:38 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// CommandLinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * <B>CommandLinkRenderer</B> is a class that renders the current value of
 * <code>UICommand<code> as a HyperLink that acts like a Button.
 */

public class CommandLinkRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(CommandLinkRenderer.class);
     
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

        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + component.getId());
        }

        UICommand command = (UICommand) component;

        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
            if (log.isTraceEnabled()) {
                log.trace("No decoding necessary since the component " +
                          component.getId() + " is disabled");
            }
            return;
        } 
	
        String 
	    clientId = command.getClientId(context),
	    paramName = getHiddenFieldName(context, command);
        Map requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        String value = (String) requestParameterMap.get(paramName);
        if (value == null || value.equals("") || !clientId.equals(value)) {
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

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " + component.getId());
        }

        UICommand command = (UICommand) component;

        // suppress rendering if "rendered" property on the command is
        // false.
        if (!command.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        String clientId = command.getClientId(context);

        UIForm uiform = getMyForm(context, command);
        if ( uiform == null ) {
            if (log.isErrorEnabled()) {
                log.error("component " + component.getId() +
                          " must be enclosed inside a form ");
            }
            return;
        }
        String formClientId = uiform.getClientId(context);

        //Write Anchor attributes

        //make link act as if it's a button using javascript
        Param paramList[] = getParamList(context, command);
        StringBuffer sb = new StringBuffer();
        writer.startElement("a", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("href", "#", "href");
        Util.renderPassThruAttributes(context, writer, component,
                                      new String[]{"onclick", "target"});
        Util.renderBooleanPassThruAttributes(writer, component);
        sb = new StringBuffer();
        // call the javascript function that clears the all the hidden field
        // parameters in the form.
        sb.append(CLEAR_HIDDEN_FIELD_FN_NAME + "_" + formClientId.replace(NamingContainer.SEPARATOR_CHAR, '_'));
        sb.append("('");
        sb.append(formClientId);
        sb.append("');");
        sb.append("document.forms[");
        sb.append("'");
        sb.append(formClientId);
        sb.append("'");
        sb.append("]['");
        sb.append(getHiddenFieldName(context, command));
        sb.append("'].value='");
        sb.append(clientId);
        sb.append("';");
        for (int i = 0, len = paramList.length; i < len; i++) {
            sb.append("document.forms[");
            sb.append("'");
            sb.append(formClientId);
            sb.append("'");
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
        String target = (String) component.getAttributes().get("target");
        if (target != null && target.trim().length() > 0) {
            sb.append(" document.forms[");
            sb.append("'");
            sb.append(formClientId);
            sb.append("'");
            sb.append("].target='");
            sb.append(target);
            sb.append("';");
        }
        sb.append(" document.forms[");
        sb.append("'");
        sb.append(formClientId);
        sb.append("'");
        sb.append("].submit()");

        sb.append("; return false;");
        writer.writeAttribute("onclick", sb.toString(), "onclick");

        //handle css style class
        String styleClass = (String)
            command.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        
        // render the current value as link text.
        String label = null;
        Object value = ((UICommand) component).getValue();
        if (value != null) {
            label = value.toString();
        }
        if (log.isTraceEnabled()) {
            log.trace("Value to be rendered " + value);
        }
        if (label != null && label.length() != 0) {
            writer.write(label);
        }
        writer.flush();

    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.encodeBegin(context);
            if (kid.getRendersChildren()) {
                kid.encodeChildren(context);
            }
            kid.encodeEnd(context);
        }
        if (log.isTraceEnabled()) {
            log.trace("End encoding children " + component.getId());
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
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        //Write Anchor inline elements

        //Done writing Anchor element
        writer.endElement("a");

        //Handle hidden fields

        //Only need one hidden field for the link itself per form.
	FormRenderer.addNeededHiddenField(context, 
	    getHiddenFieldName(context, command));

	// PENDING(edburns): not sure if the JSFA59 back button problem
	// manifests itself with param children as well...

        // get UIParameter children...
        Param paramList[] = getParamList(context, command);
        for (int i = 0; i < paramList.length; i++) {
            FormRenderer.addNeededHiddenField(context, paramList[i].getName());
        }
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }

        return;
    }
    
    protected String getHiddenFieldName(FacesContext context, 
					UIComponent component) {
	UIForm uiform = getMyForm(context, component);
	String formClientId = uiform.getClientId(context);
	return (formClientId + NamingContainer.SEPARATOR_CHAR + 
		UIViewRoot.UNIQUE_ID_PREFIX + "cl");
    }
    
    protected UIForm getMyForm(FacesContext context, UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }

} // end of class CommandLinkRenderer
