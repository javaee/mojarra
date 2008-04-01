/*
 * $Id: ButtonRenderer.java,v 1.31 2002/08/29 19:38:00 eburns Exp $
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
 * @version $Id: ButtonRenderer.java,v 1.31 2002/08/29 19:38:00 eburns Exp $
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

    /**

    * @return the image src if this component is configured to display
    * an image label, null otherwise.

    */

    protected String getImageSrc(FacesContext context, 
				 UIComponent component) {
	String result = null;
	try {
	    result = getKeyAndLookupInBundle(context, component, "imageKey");
	}
	catch (MissingResourceException e) {
	    // Do nothing since the absence of a resource is not an
	    // error.
	}
	if (null == result) {
	    result = (String) component.getAttribute("image");
	}
	return result;
    }

    protected String getLabel(FacesContext context, 
			      UIComponent component) throws IOException {
	String result = null;

	try {
	    result = getKeyAndLookupInBundle(context, component, "key");
	}
	catch (MissingResourceException e) {
	    // Do nothing since the absence of a resource is not an
	    // error.
	}
	if (null == result) {
	    result = (String) component.getAttribute("label");
	}
	return result;
    }

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
        String 
	    formName = null,
	    commandName = null;
        if (value != null) {
            commandName = value.toString();
            // to handle image buttons, check for x and y parameters.
            if ((context.getServletRequest().getParameter(commandName) == null) &&
                (context.getServletRequest().getParameter(commandName+".x") == null &&
                context.getServletRequest().getParameter(commandName+".y") == null)) {
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
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                formName = ((UIForm)parent).getFormName();
                break;
            }
            parent = parent.getParent();
        }

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
        String label= null, imageSrc = null,
	    cmdName = (String) component.currentValue(context);
        Assert.assert_it(cmdName != null);
        
        String buttonType = (String)component.getAttribute("type");
        if ( buttonType == null ) {
            buttonType = "submit";
        }    
        // PENDING (visvan) IE has a bug in HTML 4.0 Button implementation.
        // with nested label and graphic tags, the values don't get submitted
        // correctly. So we decided not to render as HTML 4.0 BUTTON element.
        // label attribute will go away once we move to nested tags.
        writer.write("<input type=");
	imageSrc = getImageSrc(context, component);
	label = getLabel(context, component);
        if (null != imageSrc) {
            writer.write("\"image\" src=\"");
            writer.write(imageSrc);
            writer.write("\"");
            writer.write(" name=\"");
            writer.write(cmdName);
            writer.write("\"");
        } else {
            writer.write("\"");
            writer.write(buttonType);
            writer.write("\"");
            writer.write(" name=\"");
            writer.write(cmdName);
            writer.write("\"");
            writer.write(" value=\"");
            if ( label == null ) {
                label = cmdName;
            }    
	    writer.write(padLabel(label));
	    writer.write("\"");
        }
        // render HTML 4.0 attributes if any.
        writer.write(Util.renderPassthruAttributes(context, component));
	writer.write(Util.renderBooleanPassthruAttributes(context, component));
        writer.write(">");
    }

} // end of class ButtonRenderer
