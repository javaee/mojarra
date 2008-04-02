/*
 * $Id: HyperlinkRenderer.java,v 1.46 2003/05/27 22:56:04 rkitain Exp $
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
import javax.faces.FacesException;
import javax.faces.render.Renderer;
import javax.faces.tree.TreeFactory;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>HyperlinkRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HyperlinkRenderer.java,v 1.46 2003/05/27 22:56:04 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HyperlinkRenderer extends HtmlBasicRenderer {
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

    public HyperlinkRenderer() {
        super();
    }

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
	    component.setValid(true);
	    return;
        }

        //PENDING(rogerk) fire action event
        //
        command.fireActionEvent(context);
	
        command.setValid(true);
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

        //PENDING(rogerk) don't call this if "href" (destination) is
        // a non-faces page.  For non-faces pages, we would simply 
        // include the "href attribute's value.
        // ex: <a href="http://java.sun.com".....
        // For faces pages, we are expecting the "href" attribute to
        // begin with "/faces" (ex: /faces/Faces_Basic.xul).
        //PENDING(rogerk) what if "href" attribute is not set (null)???
        //
        String href = (String)command.getAttribute("href");
        String commandName = command.getCommandName();

        //Write Anchor
        writer.write("<a href=");
        writer.write(QUOTE);

        if (href != null) {
            handleHref(context, command);
        }
	else if (null != commandName) {
	    handleCommandName(context, command, commandName);
	}
	return;
    }

    protected String getLinkText(FacesContext context, UIComponent component) {
        String text = null;
	
	try {
	    text = getKeyAndLookupInBundle(context, component, "key");
	}
	catch (java.util.MissingResourceException e) {
	    // Do nothing since the absence of a resource is not an
	    // error.
	}
	if (null == text) {
	    text = (String)component.getAttribute("label");
	}
	return text;
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

    protected String getImage(UICommand command) {

	String image = (String)command.getAttribute("image");
	if (image != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<img src=");
            buffer.append(QUOTE);
            buffer.append(image);
            buffer.append(QUOTE);
            buffer.append(">");
            return buffer.toString();
	}
        return null;
    }

    protected Object[] getParamList(FacesContext context, UIComponent command) {
        ArrayList parameterList = new ArrayList();

	Iterator kids = command.getChildren();
	while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();

            if (kid instanceof UIParameter) {
                Param param = new Param(((UIParameter)kid).getName(),
                    ((String)((UIParameter)kid).currentValue(context)));
                parameterList.add(param);
            }
	}
        
        if (!parameterList.isEmpty()) {
            return parameterList.toArray();
        }
        return null;
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
	writer.write("#\" onmousedown=\"document.forms[");
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
        String linkText = getLinkText(context, command);
	if (linkText != null) {
            writer.write(linkText);
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
        Object paramList[] = getParamList(context, command);
        if (paramList != null) {
            for (int i=0; i<paramList.length; i++) {
                writer.write("<input type=\"hidden\" name=");
                writer.write(QUOTE);
                writer.write(((Param)paramList[i]).getName());
                writer.write(QUOTE);
                writer.write(" value=");
                writer.write(QUOTE);
                writer.write(((Param)paramList[i]).getValue());
                writer.write(QUOTE);
                writer.write("/>");
            }
        }

    }


    protected void handleHref(FacesContext context, UICommand command)
        throws IOException {

	ResponseWriter writer = context.getResponseWriter();
        String href = (String)command.getAttribute("href");

        //Write Anchor attributes
        if (href.startsWith(RIConstants.URL_PREFIX)) {
	    writer.write(getHref(context, command));
        } else {
	    writer.write(href);
        }

        String params = getHrefParams(context, command);
        if (params != null) {
            if (href.indexOf('?') < 0) {
                writer.write("?");
            } else {
                writer.write("&");
            }
            writer.write(params);
        }

	writer.write(QUOTE);

        //handle css style class
	String commandClass = getCommandClass(command);
	if (commandClass != null) {
            writer.write(commandClass);
	}

        //Done writing Anchor attributes.
        writer.write(">");

        //Write Anchor inline elements

        //handle image
        String image = getImage(command);
        if (image != null ) {
            writer.write(image);
        }

        //label text
	String text = getLinkText(context, command);
	if (text != null) {
            writer.write(text);
	}

        //Done writing Anchor element
	writer.write("</a>");
    }

    private String getHref(FacesContext context, UIComponent component) {
	// PENDING(edburns): this method needs optimization.  For
	// exaple, the local variable contextPath isn't used.

        String contextPath = context.getExternalContext().getRequestContextPath();
        StringBuffer sb = new StringBuffer(contextPath);
        sb.append(RIConstants.URL_PREFIX);
        // need to make sure the rendered string contains where we
        // want to go next (href).
        //PENDING(rogerk) should "href" be required attribute??
        //
        String href = (String)component.getAttribute("href");
        if (href != null) {
            href = href.substring(RIConstants.URL_PREFIX.length());
            sb.append(href);
        } else {
            sb.append(context.getTree().getTreeId());
        }

        return (context.getExternalContext().encodeURL(sb.toString()));
    }

    protected String getHrefParams(FacesContext context, UIComponent component) {

        // get UIParameter children and add them to the URL
	Object paramList[] = getParamList(context, component);
        if (paramList != null) {
	    StringBuffer paramBuff = new StringBuffer();
	    for (int i=0; i<paramList.length; i++) {
                if (i > 0) {
		    paramBuff.append("&");
                }
                paramBuff.append(((Param)paramList[i]).getName());
                paramBuff.append("=");
                paramBuff.append(((Param)paramList[i]).getValue());
	    }
            return(paramBuff.toString());
        }
        return null;
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
