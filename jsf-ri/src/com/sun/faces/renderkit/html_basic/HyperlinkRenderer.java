/*
 * $Id: HyperlinkRenderer.java,v 1.35 2003/02/07 00:18:09 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HyperlinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.io.IOException;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.event.FormEvent;
import javax.faces.component.UIComponent;
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
 * @version $Id: HyperlinkRenderer.java,v 1.35 2003/02/07 00:18:09 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HyperlinkRenderer extends HtmlBasicRenderer {
    //
    // Protected Constants
    //

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
    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    
        return (componentType.equals(UICommand.TYPE));
    }

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
        String value = context.getServletRequest().getParameter(clientId);
        if (value == null || value.equals("")) {
	    component.setValid(true);
	    return;
        }

        // Construct and enqueue a FormEvent for the application
        String commandName = (String) command.currentValue(context);
        String formName = null;
	UIForm form = getMyForm(context, command);

        if (null == (formName = (String) form.currentValue(context))) {
            // PENDING (visvan) log error
            //log.error("Button[" + component.getClientId() +
            //          "] not nested in a form");
            command.setValid(false);
            return;
        }
        FormEvent formEvent =
            new FormEvent(command, formName, commandName);
        context.addApplicationEvent(formEvent);
	
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
	String commandClass = null;
        Assert.assert_it( writer != null );

        writer.write("<a href=\"");

        //PENDING(rogerk) don't call this if "target" (destination) is
        // a non-faces page.  For non-faces pages, we would simply 
        // include the "target attribute's value.
        // ex: <a href="http://java.sun.com".....
        // For faces pages, we are expecting the "target" attribute to
        // begin with "/faces" (ex: /faces/Faces_Basic.xul).
        //PENDING(rogerk) what if "target" attribute is not set (null)???
        //
        String target = (String)command.getAttribute("target");
        String commandName = command.getCommandName();
        if (target != null) {
            if (target.startsWith(RIConstants.URL_PREFIX)) {
                writer.write(href(context, command));
            } else {
                writer.write(target);
            }
        }
	else if (null != commandName) {
	    handleCommandName(context, command, commandName);
	    return;
	}
	writer.write("\"");
	if (null != (commandClass = (String) 
		     command.getAttribute("commandClass"))) {
	    writer.write(" class=\"" + commandClass + "\"");
	}

        writer.write(">");
        String image = (String)command.getAttribute("image");
        if (image != null) {
            writer.write("<image src=\"");
            writer.write(image);
            writer.write("\">");
        }
        String text = getLinkText(context, command);

        if (text != null) {
            writer.write(text);
        }
        writer.write("</A>");        

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

    protected void handleCommandName(FacesContext context, 
				     UICommand command, String commandName)
        throws IOException {
        ResponseWriter writer = context.getResponseWriter();
	String 
	    linkText = null,
	    clientId = command.getClientId(context);

	int formNumber = getMyFormNumber(context, 
					 getMyForm(context, command));
	writer.write("#\" onclick=\"document.forms[" + formNumber + "]." + 
		     clientId + ".value='" + commandName + 
		     "'; document.forms[" + formNumber + "].submit()\">");
	if (null != (linkText = getLinkText(context, command))) {
	    writer.write(linkText);
	}
	writer.write("</a>");
	writer.write("<input type=\"hidden\" name=\"" + clientId + "\"/>");
    }


    private String href(FacesContext context, UIComponent component) {
	// PENDING(edburns): this method needs optimization.  For
	// exaple, the local variable contextPath isn't used.

        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        HttpServletResponse response =
            (HttpServletResponse) context.getServletResponse();
        String contextPath = request.getContextPath();
        if ( contextPath.indexOf("/") == -1 ) {
            contextPath = contextPath + "/";
        }
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append(RIConstants.URL_PREFIX);
        // need to make sure the rendered string contains where we
        // want to go next (target).
        //PENDING(rogerk) should "target" be required attribute??
        //
        String target = (String)component.getAttribute("target");
        if (target != null) {
            target = target.substring(RIConstants.URL_PREFIX.length());
            sb.append(target);
        } else {
            sb.append(context.getTree().getTreeId());
        }
        return (response.encodeURL(sb.toString()));
    }

} // end of class HyperlinkRenderer
