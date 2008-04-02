/*
 * $Id: HyperlinkRenderer.java,v 1.48 2003/07/29 16:25:21 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HyperlinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;


/**
 *
 *  <B>HyperlinkRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HyperlinkRenderer.java,v 1.48 2003/07/29 16:25:21 rlubke Exp $
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

        //PENDING(rogerk) fire action event
        command.fireActionEvent(context);
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

        String commandName="";
        // PENDING(visvan) FIX AFTER SUCCESSFUL RI COMPILATION
        //commandName = command.getCommandName();

	if (null != commandName) {
	    handleCommandName(context, command, commandName);
	}
	return;
    }

    protected String getCommandClass(UICommand command) {

	String commandClass = (String)
            command.getAttribute("commandClass");
	if (commandClass != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(" class=");
            buffer.append(QUOTE);
            buffer.append(commandClass);
            buffer.append(QUOTE);
            return buffer.toString();
	}
        return null;
    }

    protected String getImageText(String image) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<img src=");
        buffer.append(QUOTE);
        buffer.append(image);
        buffer.append(QUOTE);
        buffer.append(">");
        return buffer.toString();
    }

    protected Param[] getParamList(FacesContext context, UIComponent command) {
        ArrayList parameterList = new ArrayList();

	Iterator kids = command.getChildren();
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

    protected void handleCommandName(FacesContext context,
				     UICommand command, String commandName)
        throws IOException {
        ResponseWriter writer = context.getResponseWriter();
	String clientId = command.getClientId(context);

	int formNumber = getMyFormNumber(context,
					 getMyForm(context, command));

	//Write Anchor attributes

        //make link act as if it's a button using javascript
	writer.write("<a href=\"#\" onmousedown=\"document.forms[");
        //need to have a String
        writer.write("" + formNumber + "");
        writer.write("].");
        writer.write(clientId);
        writer.write(".value='");
        writer.write(commandName);
        writer.write("'; document.forms[");
        //need to have a String
        writer.write("" + formNumber + "");
        writer.write("].submit()");
        writer.write(QUOTE);

        //handle css style class
        String commandClass = getCommandClass(command);
        if (commandClass != null) {
            writer.write(commandClass);
        }

        //Done writing Anchor attributes.
        writer.write(">");

	//Write Anchor inline elements

        //label text
        String imageSrc = getImageSrc(context, command);
        if (imageSrc != null) {
            writer.write(getImageText(imageSrc));
        } else {
            writer.write(getLabel(context, command));
        }

        //Done writing Anchor element
	writer.write("</a>");

        //Handle hidden fields

        //hidden clientId field
	writer.write("<input type=\"hidden\" name=");
        writer.write(QUOTE);
        writer.write(clientId);
        writer.write(QUOTE);
        writer.write("/>");

	// get UIParameter children...
        Param paramList[] = getParamList(context, command);
            for (int i = 0; i < paramList.length; i++) {
                writer.write("<input type=\"hidden\" name=");
                writer.write(QUOTE);
                writer.write((paramList[i]).getName());
                writer.write(QUOTE);
                writer.write(" value=");
                writer.write(QUOTE);
                writer.write((paramList[i]).getValue());
                writer.write(QUOTE);
                writer.write("/>");
            }

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
