/*
 * $Id: CommandLinkRenderer.java,v 1.5 2004/01/14 17:13:02 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// CommandLinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.sun.faces.util.Util;


/**
 *
 *  <B>CommandLinkRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CommandLinkRenderer.java,v 1.5 2004/01/14 17:13:02 eburns Exp $
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
	    throw new NullPointerException(Util.getExceptionMessage(
				    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
	UICommand command = (UICommand) component;

        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
            return;
        } 

        // Was our command the one that caused this submission?  we don'
        // have to worry about getting the value from request parameter
        // because we just need to know if this command caused the
        // submission. We can get the command name by calling
        // currentValue. This way we can get around the IE bug.
        String clientId = command.getClientId(context);
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String value = (String)requestParameterMap.get(clientId);
        if (value == null || value.equals("")) {
            return;
        }

	command.queueEvent(new ActionEvent(component));
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

    protected int getMyFormNumber(FacesContext context, UIForm form) {
	// If we don't have a form, return 0
	if (null == form) {
	    return 0;
	}
        Map requestMap = context.getExternalContext().getRequestMap();
	int numForms = 0;
	Integer formsInt = null;
	// find out the current number of forms in the page.
	if (null != (formsInt = (Integer) 
		     requestMap.get(RIConstants.FORM_NUMBER_ATTR))) {
	    numForms = formsInt.intValue();
            // since the form index in the document starts from 0.
            numForms--;
	}
	return numForms;
    }

    public boolean getRendersChildren() {
	return true;
    }

    private String clientId = null;
    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	UICommand command = (UICommand) component;

        // suppress rendering if "rendered" property on the command is
        // false.
        if (!command.isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert( writer != null );

	clientId = command.getClientId(context);

	int formNumber = getMyFormNumber(context,
					 getMyForm(context, command));

	//Write Anchor attributes

        //make link act as if it's a button using javascript
        Param paramList[] = getParamList(context, command);
	StringBuffer sb = new StringBuffer();
	writer.startElement("a", component);
	writeIdAttributeIfNecessary(context, writer, component);
	writer.writeAttribute("href", "#", "href");
        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);
	sb = new StringBuffer();
	sb.append("document.forms[");
	sb.append("");
	sb.append(formNumber);
	sb.append("");
	sb.append("]['");
	sb.append(clientId);
	sb.append("'].value='");
	sb.append(clientId);
	sb.append("';");
        for (int i = 0, len = paramList.length; i < len; i++) {
	    sb.append("document.forms[");
	    sb.append("");
	    sb.append(formNumber);
	    sb.append("");
	    sb.append("]['");
	    sb.append(paramList[i].getName());
	    sb.append("'].value='");
	    sb.append(paramList[i].getValue());
	    sb.append("';");
	}	    
	sb.append(" document.forms[");
	sb.append("");
	sb.append(formNumber);
	sb.append("");
	sb.append("].submit()");

	writer.writeAttribute("onmousedown", sb.toString(), null); 

        //handle css style class
	String styleClass = (String)
            command.getAttributes().get("styleClass");
	if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
	writer.flush();

    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
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
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
	UICommand command = (UICommand) component;

        // suppress rendering if "rendered" property on the command is
        // false.
        if (!command.isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert( writer != null );

	//Write Anchor inline elements

        //Done writing Anchor element
        writer.endElement("a");

        //Handle hidden fields

        //hidden clientId field
	writer.startElement("input", component);
	writer.writeAttribute("type", "hidden", "type");
	writer.writeAttribute("name", clientId, "clientId");
	writer.endElement("input");

	// get UIParameter children...
        Param paramList[] = getParamList(context, command);
        for (int i = 0; i < paramList.length; i++) {
            writer.startElement("input", component);
	    writer.writeAttribute("type", "hidden", "type");
	    writer.writeAttribute("name", (paramList[i]).getName(), null);
	    writer.writeAttribute("value", (paramList[i]).getValue(), null);
	    writer.endElement("input");
        }


	return;
    }

} // end of class CommandLinkRenderer
