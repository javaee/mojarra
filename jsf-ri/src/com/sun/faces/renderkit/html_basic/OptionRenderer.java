/*
 * $Id: OptionRenderer.java,v 1.1 2001/12/12 20:08:57 edburns Exp $
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

// OptionRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WComponent;
import javax.faces.WSelectOne;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>OptionRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OptionRenderer.java,v 1.1 2001/12/12 20:08:57 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class OptionRenderer extends Object implements Renderer {
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

    public OptionRenderer() {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init() {
        // super.init();
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

    public boolean supportsType(WComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof WSelectOne ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_WSELECTONE)) {
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
 
        WSelectOne wSelectOne = null;
        if ( supportsType(c)) {
            wSelectOne = (WSelectOne) c;
        } else {
            throw new FacesException("Invalid component type. " +
                      "Expected WSelectOne");
        }
        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );
	String selectedValue = (String) wSelectOne.getSelectedValue(rc);

        StringBuffer output = new StringBuffer();
        output.append("<OPTION VALUE=\"");
        output.append(wSelectOne.getAttribute(rc, "value"));
        output.append("\"");

        if ((null != selectedValue) &&
	    selectedValue.equals(wSelectOne.getAttribute(rc, "label"))) {
            output.append(" SELECTED");
        }
        output.append(">");
	output.append(wSelectOne.getAttribute(rc, "label"));
        output.append("</OPTION>");

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


} // end of class OptionRenderer
