/*
 * $Id: ButtonRenderer.java,v 1.27 2002/08/02 19:31:59 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ButtonRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.event.CommandEvent;

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
 * @version $Id: ButtonRenderer.java,v 1.27 2002/08/02 19:31:59 jvisvanathan Exp $
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
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // Was our command the one that caused this submission?
        Object value = component.currentValue(context);
        String commandName = null;
        if (value != null) {
            commandName = value.toString();
            if (context.getServletRequest().getParameter(commandName) == null) {
                return;
            }
        } else {
            return;
        }

        // Does the extra path info on this request identify a form submit?
        String pathInfo = (String) context.getServletRequest().getAttribute
          ("javax.servlet.include.path_info");
        if (pathInfo == null) {
          pathInfo =
            ((HttpServletRequest) context.getServletRequest()).getPathInfo();
        }
        if (pathInfo == null) {
            return;
        }
        if (!pathInfo.startsWith(RIConstants.FORM_PREFIX)) {
            return;
        }
        String formName = pathInfo.substring(RIConstants.FORM_PREFIX.length() + 1);

        // Enqueue a form event to the application
        context.addApplicationEvent
            (new FormEvent(component, formName, commandName));            
    }
    
    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }
    
    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
       
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it( writer != null );
        
        //can commandName be null ?
        String cmdName = (String) component.currentValue(context);
        Assert.assert_it(cmdName != null);
        
        writer.write("<INPUT TYPE=");
        if (component.getAttribute("image") != null) {
            writer.write("\"IMAGE\" SRC=\"");
            writer.write((String) component.getAttribute("image"));
            writer.write("\"");
            writer.write(" NAME=\"");
            writer.write(cmdName);
            writer.write("\"");
        } else {
            writer.write("\"SUBMIT\" NAME=\"");
            writer.write(cmdName);
            writer.write("\"");
            writer.write(" VALUE=\"");
            // Follow the UE Spec for Button:
            // http://javaweb.sfbay.sun.com/engineering/jsue/j2ee/WebServices/
            // JavaServerFaces/uispecs/UICommand_Button.html
            // If there is no label specified, default to commandName.
            String label = (String)component.getAttribute("label");
            if ( label == null ) {
                label = cmdName;
            }    
            if (label.length() == 3) {
                writer.write("&nbsp;&nbsp;");
                writer.write(label);
                writer.write("&nbsp;&nbsp;");
                writer.write("\"");
            } else if (label.length() == 2) {
                writer.write("&nbsp;&nbsp;&nbsp;");
                writer.write(label);
                writer.write("&nbsp;&nbsp;&nbsp;");
                writer.write("\"");
            } else {
                writer.write(label);
                writer.write("\"");
            }
        }
        writer.write(">");
    }

} // end of class ButtonRenderer
