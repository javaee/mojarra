/*
 * $Id: HyperlinkRenderer.java,v 1.55 2003/08/29 17:46:42 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HyperlinkRenderer.java

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

import org.mozilla.util.Assert;


/**
 *
 *  <B>HyperlinkRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HyperlinkRenderer.java,v 1.55 2003/08/29 17:46:42 eburns Exp $
 */

public class HyperlinkRenderer extends BaseCommandRenderer {
    //
    // Protected Constants
    //
    // Separator character
    private final char QUOTE = '\"';


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

    public void decode(FacesContext context, UIComponent component)
            throws IOException {
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

	context.addFacesEvent(new ActionEvent(component));
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
	Integer formsInt = (Integer)
	    form.getAttribute(RIConstants.FORM_NUMBER_ATTR);
	Assert.assert_it(null != formsInt);
	return formsInt.intValue();
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
        Assert.assert_it( writer != null );

	String clientId = command.getClientId(context);

	int formNumber = getMyFormNumber(context,
					 getMyForm(context, command));

	//Write Anchor attributes

        //make link act as if it's a button using javascript
	StringBuffer sb = new StringBuffer();
	writer.startElement("a", component);
	//PENDING(rogerk)href 3rd arg?
	writer.writeAttribute("href", "#", "href");
	sb = new StringBuffer();
	sb.append("document.forms[");
	sb.append("");
	sb.append(formNumber);
	sb.append("");
	sb.append("].");
	sb.append(clientId);
	sb.append(".value='");
	sb.append(clientId);
	sb.append("'; document.forms[");
	sb.append("");
	sb.append(formNumber);
	sb.append("");
	sb.append("].submit()");

	//PENDING(rogerk)null 3rd arg?
	writer.writeAttribute("onmousedown", sb.toString(), null); 

        //handle css style class
	String commandClass = (String)
            command.getAttribute("commandClass");
	if (commandClass != null) {
            writer.writeAttribute("class", commandClass, "commandClass");
        }

	//Write Anchor inline elements

        //label text
        String imageSrc = getImageSrc(context, command);
        if (imageSrc != null) {
	    writer.startElement("img", null);
	    writer.writeAttribute("src", getImageText(imageSrc), "image");
        } else {
	    //PENDING(rogerk)value 2nd arg?
            writer.writeText(getLabel(context, command), "value");
        }

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
	    //PENDING(rogerk)3rd args?
	    writer.writeAttribute("name", (paramList[i]).getName(), null);
	    writer.writeAttribute("value", (paramList[i]).getValue(), null);
	    writer.endElement("input");
        }


	return;
    }

    protected String getImageText(String image) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(image);
        return buffer.toString();
    }

    protected Param[] getParamList(FacesContext context, UIComponent command) {
        ArrayList parameterList = new ArrayList();

	Iterator kids = command.getChildren().iterator();
	while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();

            if (kid instanceof UIParameter) {
                UIParameter uiParam = (UIParameter) kid;
                Param param = new Param(uiParam.getName(),
                    ((String)uiParam.currentValue(context)));
                parameterList.add(param);
            }
	}

        return (Param[]) parameterList.toArray(new Param[parameterList.size()]);
    }

    //inner class to store parameter name and value pairs
    protected class Param {

        public Param(String name, String value) {
            set(name, value);
        }

        private String name;
        private String value;

        public void set(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

} // end of class HyperlinkRenderer
