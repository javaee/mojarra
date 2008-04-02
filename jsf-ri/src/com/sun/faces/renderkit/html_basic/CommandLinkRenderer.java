/*
 * $Id: CommandLinkRenderer.java,v 1.22 2004/06/08 13:47:28 rogerk Exp $
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


    protected UIForm getMyForm(FacesContext context, UICommand command) {
        UIComponent parent = command.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }

    protected String getHiddenFieldName(FacesContext context, 
					UICommand command) {
	UIForm uiform = getMyForm(context, command);
	String formClientId = uiform.getClientId(context);
	return (formClientId + NamingContainer.SEPARATOR_CHAR + 
		UIViewRoot.UNIQUE_ID_PREFIX + "cl");
    }



    public boolean getRendersChildren() {
        return true;
    }


    private String clientId = null;


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
        Util.doAssert(writer != null);

        clientId = command.getClientId(context);

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
        Util.renderPassThruAttributes(writer, component,
                                      new String[]{"onclick"});
        Util.renderBooleanPassThruAttributes(writer, component);
        sb = new StringBuffer();
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
        sb.append(" document.forms[");
        sb.append("'");
        sb.append(formClientId);
        sb.append("'");
        sb.append("].submit()");

        sb.append("; return false;");
        writer.writeAttribute("onclick", sb.toString(), null);

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
        Util.doAssert(writer != null);

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

} // end of class CommandLinkRenderer
