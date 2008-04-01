/*
 * $Id: ButtonRenderer.java,v 1.17 2002/03/16 00:09:35 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ButtonRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.Constants;
import java.beans.PropertyDescriptor;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.UICommand;
import javax.faces.UIComponent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>ButtonRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ButtonRenderer.java,v 1.17 2002/03/16 00:09:35 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ButtonRenderer extends Object implements Renderer
{
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

    private String property = null;
    private String type = null;
    private String value = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ButtonRenderer()
    {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init()
    {
        // super.init();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    /**
     * Return the property name.
     */
    public String getProperty() {
        return (property);
    }

    /**
     * Set the property name.
     *
     * @param name The property name
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Return the button "type".
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the button "type".
     *
     * @param name The property name
     */
    public void setType(String type) {
        this.type = type;
    }

    //
    // Methods From Renderer
    //
    public boolean supportsComponentType(UIComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof UICommand ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsComponentType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_UICOMMAND)) {
            supports = true;
        }
        return supports;
    }

    public Iterator getSupportedAttributeNames(String componentType) throws FacesException {
        return null;
    }

    public Iterator getSupportedAttributes(String componentType) throws FacesException {
	return null;
    }

    public PropertyDescriptor getAttributeDescriptor(String attributeName)
	throws FacesException {
	return null;
    }

    public void renderStart(RenderContext rc, UIComponent c) 
        throws IOException, FacesException {
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        UICommand wCommand = null;
        if ( supportsComponentType(c)) {
            wCommand = (UICommand) c;
        } else {
            throw new FacesException("Invalid component type. " +
                      "Expected UICommand");
        }

        String commandId = wCommand.getId();
        Assert.assert_it(null != commandId);

        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );

        StringBuffer output = new StringBuffer();
        output.append("<INPUT TYPE=");

        if (wCommand.getAttribute(rc, "image") != null) {
            output.append("\"IMAGE\" SRC=\"");
            output.append(wCommand.getAttribute(rc, "image"));
            output.append("\"");
            output.append(" NAME=\"");
            output.append(commandId);
            output.append("\"");
        } else {
            String label = (String)wCommand.getAttribute(rc, "label");
            output.append("\"SUBMIT\" NAME=\"");
            output.append(commandId);
            output.append("\"");
            output.append(" value=\"");
	    // Follow the UE Spec for Button:
	    // http://javaweb.sfbay.sun.com/engineering/jsue/j2ee/WebServices/
            // JavaServerFaces/uispecs/UICommand_Button.html
            if (label.length() == 3) {
                output.append("&nbsp;&nbsp;");
                output.append(label);
                output.append("&nbsp;&nbsp;");
                output.append("\"");
            } else if (label.length() == 2) {
                output.append("&nbsp;&nbsp;&nbsp;");
                output.append(label);
                output.append("&nbsp;&nbsp;&nbsp;");
                output.append("\"");
            } else {
                output.append(label);
                output.append("\"");
            }
        }
        output.append(">");

        outputMethod.writeText(output.toString());
        outputMethod.flush();
    }

    public void renderChildren(RenderContext rc, UIComponent c) 
        throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, UIComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean getCanRenderChildren(RenderContext rc, UIComponent c) {
        return false;
    }



} // end of class ButtonRenderer
