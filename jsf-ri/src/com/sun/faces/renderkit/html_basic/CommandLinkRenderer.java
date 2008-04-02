/*
 * $Id: CommandLinkRenderer.java,v 1.29 2005/05/12 22:08:15 jayashri Exp $
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
import javax.faces.context.ExternalContext;
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

    private static final String DID_RENDER_SCRIPT = RIConstants.FACES_PREFIX +
	"didRenderScript";

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
	if (null == paramName) {
	    return;
	}
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

	String fieldName = getHiddenFieldName(context, component);
	if (null == fieldName) {
	    return;
	}

        //Done writing Anchor element
        writer.endElement("a");

	renderHiddenFieldsAndScriptIfNecessary(context, writer, component, fieldName);

        UIForm uiform = getMyForm(context, command);
        if ( uiform == null ) {
            if (log.isErrorEnabled()) {
                log.error("component " + component.getId() +
                          " must be enclosed inside a form ");
            }
            return;
        }
        // DID_RENDER_SCRIPT needs to be reset, otherwise this attribute 
        // will also be persisted which will cause the script to be not rendered 
        // during postback.
        uiform.getAttributes().remove(DID_RENDER_SCRIPT);
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }

        return;
    }

    public void writeScriptContent(FacesContext context, 
				   ResponseWriter writer,
				   UIComponent component) throws IOException {
	Map requestMap = context.getExternalContext().getRequestMap();
	UIForm myForm = getMyForm(context, component);
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
	writer.write("<!--\n");
	if (isXHTML) {
	    writer.write("<![CDATA[\n");
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
	    writer.write("]]>\n");
	}
	writer.write("//-->\n");
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
	Map requestMap = context.getExternalContext().getRequestMap();
	
	if (null == fieldName) {
	    return;
	}
	String keyName = RIConstants.FACES_PREFIX + fieldName;
	Object keyVal = null;
	// if the hidden field for this form hasn't yet been rendered
	if ((null == (keyVal = requestMap.get(keyName))) 
	    ||
	    (null != keyVal && !keyVal.equals(keyName))) {
	    writer.startElement("input", component);
	    writer.writeAttribute("type", "hidden", null);
	    writer.writeAttribute("name", fieldName, null);
	    writer.endElement("input");
	    // declare that we have rendered it
	    requestMap.put(keyName, keyName);
	    
	    // PENDING(edburns): not sure if the JSFA59 back button problem
	    // manifests itself with param children as well...
	    
	    // get UIParameter children...
	    Param paramList[] = getParamList(context, component);
	    for (int i = 0; i < paramList.length; i++) {
		fieldName = paramList[i].getName();
		keyName = RIConstants.FACES_PREFIX + fieldName;
		if ((null != (keyVal = requestMap.get(keyName))) && 
		    !keyVal.equals(keyName)) {
		    writer.startElement("input", component);
		    writer.writeAttribute("type", "hidden", null);
		    writer.writeAttribute("name", fieldName, null);
		    writer.endElement("input");
		}
	    }
	    writeScriptContent(context, writer, component);
	}
    }

    
    protected String getHiddenFieldName(FacesContext context, 
					UIComponent component) {
	UIForm uiform = getMyForm(context, component);
	if (null == uiform) {
	    return null;
	}
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
	if (null == parent) {
            if (log.isErrorEnabled()) {
                log.error("component " + component.getId() +
                          " must be enclosed inside a form ");
            }
	}

        return (UIForm) parent;
    }

} // end of class CommandLinkRenderer
