/*
 * $Id: ButtonRenderer.java,v 1.10 2001/11/29 01:54:34 rogerk Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
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
import javax.faces.WCommand;
import javax.faces.WComponent;

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
 * @version $Id: ButtonRenderer.java,v 1.10 2001/11/29 01:54:34 rogerk Exp $
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
    public boolean supportsType(WComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof WCommand ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_WCOMMAND)) {
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

    public void renderStart(RenderContext rc, WComponent c) 
        throws IOException, FacesException {
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        WCommand wCommand = null;
        if ( supportsType(c)) {
            wCommand = (WCommand) c;
        } else {
            throw new FacesException("Invalid component type. " +
                      "Expected WCommand");
        }

        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );

        StringBuffer output = new StringBuffer();
        output.append("<input type=");
        if (wCommand.getAttribute(rc, "image") != null) {
            output.append("image src=");
            output.append(wCommand.getAttribute(rc, "image"));
            output.append(" name=");
            output.append(wCommand.getAttribute(rc, "name"));
        } else {
            String label = (String)wCommand.getAttribute(rc, "label");
            output.append("submit name=");
            output.append(wCommand.getAttribute(rc, "name"));
            output.append(" value=");
	    // Follow the UE Spec for Button:
	    // http://javaweb.sfbay.sun.com/engineering/jsue/j2ee/WebServices/
            // JavaServerFaces/uispecs/WCommand_Button.html
            if (label.length() == 3) {
                output.append("&nbsp;&nbsp;");
                output.append(label);
                output.append("&nbsp;&nbsp;");
            } else if (label.length() == 2) {
                output.append("&nbsp;&nbsp;&nbsp;");
                output.append(label);
                output.append("&nbsp;&nbsp;&nbsp;");
            } else {
                output.append(label);
            }
        }
        output.append(">");

        outputMethod.writeText(output.toString());
        outputMethod.flush();
    }

    public void renderChildren(RenderContext rc, WComponent c) 
        throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, WComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean getCanRenderChildren(RenderContext rc, WComponent c) {
        return false;
    }



} // end of class ButtonRenderer
