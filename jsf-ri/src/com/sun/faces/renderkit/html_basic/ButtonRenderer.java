/*
 * $Id: ButtonRenderer.java,v 1.33 2002/08/30 23:49:28 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ButtonRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;
import java.util.MissingResourceException;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;

import javax.faces.component.UIForm;
import javax.faces.event.FormEvent;
import javax.faces.component.UIForm;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import javax.faces.event.FormEvent;
import javax.faces.component.UICommand;
import com.sun.faces.RIConstants;
import javax.servlet.http.HttpServletRequest;


/**
 *
 *  <B>ButtonRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ButtonRenderer.java,v 1.33 2002/08/30 23:49:28 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ButtonRenderer extends HtmlBasicRenderer {
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

    public ButtonRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    /** Follow the UE Spec for Button:
     * http://javaweb.sfbay.sun.com/engineering/jsue/j2ee/WebServices/
     * JavaServerFaces/uispecs/UICommand_Button.html
     */
    protected String padLabel(String label) {
	if (label.length() == 3) {
	    label = "&nbsp;&nbsp;" + label + "&nbsp;&nbsp;";
	} else if (label.length() == 2) {
	    label = "&nbsp;&nbsp;&nbsp;" + label + "&nbsp;&nbsp;&nbsp;";
	}
	return label;
    }

    
    //
    // Methods From Renderer
    //

    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    
        return (componentType.equals(UICommand.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
            throws IOException {
       if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // Was our command the one that caused this submission?
        // we don' have to worry about getting the value from request parameter
        // because we just need to know if this command caused the submission. We
        // can get the command name by calling currentValue. This way we can 
        // get around the IE bug.
        component.setValid(true);
        String value = context.getServletRequest().
                getParameter(component.getCompoundId());
        if (value == null) {
            return;
        }

        String type = (String) component.getAttribute("type");
        if ((type == null) || (type.toLowerCase().equals("reset")) ) {
            return;
        }

        // Construct and enqueue a FormEvent for the application
        String commandName = (String) component.currentValue(context);
        String formName = null;
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                formName = (String) parent.currentValue(context);
                break;
            }
            parent = parent.getParent();
        }
        if (formName == null) {
            // PENDING (visvan) log error
            //log.error("Button[" + component.getCompoundId() +
            //          "] not nested in a form");
            return;
        }
        FormEvent formEvent =
            new FormEvent(component, formName, commandName);
        context.addApplicationEvent(formEvent);
    }
    
     public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException  {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // Which button type (SUBMIT, RESET, or BUTTON) should we generate?
        String type = (String) component.getAttribute("type");
        if (type == null) {
            type = "submit";
	    // This is needed in the decode method
	    component.setAttribute("type", type);
        }

        // Render the beginning of this button
        ResponseWriter writer = context.getResponseWriter();
        // PENDING (visvan) we need to change the '/' chars to
        // something else in the rendered name and id attributes. As
        // per HTML 4.0, "/" cannot be used as part of id.
        writer.write("<button id=\"");
        writer.write(component.getCompoundId());
        writer.write("\" name=\"");
        writer.write(component.getCompoundId());
        writer.write("\" type=\"");
        writer.write(type.toLowerCase());
        writer.write("\" value=\"");
        writer.write(type.toLowerCase());
        writer.write("\"");
        // PENDING (visvan) how to pad label now that its not rendered here ??
        // render HTML 4.0 attributes if any.
        writer.write(Util.renderPassthruAttributes(context, component));
	writer.write(Util.renderBooleanPassthruAttributes(context, component));
        writer.write(">");
    }
    
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
       
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it( writer != null );
        writer.write("</button>\n");
    }

} // end of class ButtonRenderer
