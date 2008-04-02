/*
 * $Id: ButtonRenderer.java,v 1.52 2003/07/29 16:25:21 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ButtonRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;


/**
 *
 *  <B>ButtonRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ButtonRenderer.java,v 1.52 2003/07/29 16:25:21 rlubke Exp $
 *
 */

public class ButtonRenderer extends BaseCommandRenderer {
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
        String clientId = component.getClientId(context);
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String value = (String)requestParameterMap.get(clientId);
        if (value == null) {
            if (requestParameterMap.get(clientId+".x") == null &&
                requestParameterMap.get(clientId+".y") == null) {
                return;
            }
        }

        String type = (String) component.getAttribute("type");
        if ((type != null) && (type.toLowerCase().equals("reset")) ) {
            return;
        }

        ((UICommand)component).fireActionEvent(context);
        return;
    }
    
     public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException  {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        
        // Which button type (SUBMIT, RESET, or BUTTON) should we generate?
        String type = (String) component.getAttribute("type");
	String commandClass = null;
        if (type == null) {
            type = "submit";
	    // This is needed in the decode method
	    component.setAttribute("type", type);
        }

        ResponseWriter writer = context.getResponseWriter();

        String imageSrc = getImageSrc(context, component);
        String label = getLabel(context, component);            

         writer.write("<input type=");
         if (imageSrc != null) {
             writer.write("\"image\" src=\"");
             writer.write(imageSrc);
             writer.write("\"");
             writer.write(" name=\"");
             writer.write(component.getClientId(context));
             writer.write("\"");
         } else {
             writer.write("\"");
             writer.write(type.toLowerCase());
             writer.write("\"");
             writer.write(" name=\"");
             writer.write(component.getClientId(context));
             writer.write("\"");
             writer.write(" value=\"");
             writer.write(padLabel(label));
             writer.write("\"");
         }


        writer.write(Util.renderPassthruAttributes(context, component));
        writer.write(Util.renderBooleanPassthruAttributes(context, component));
        if (null != (commandClass = (String) 
            component.getAttribute("commandClass"))) {
	    writer.write(" class=\"" + commandClass + "\" ");
	}
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
    }

} // end of class ButtonRenderer
