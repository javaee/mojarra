/*
 * $Id: CheckboxRenderer.java,v 1.10 2001/12/12 20:41:59 visvan Exp $
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

// CheckboxRenderer.java

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
import javax.faces.WSelectBoolean;
import javax.faces.Constants;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>CheckboxRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CheckboxRenderer.java,v 1.10 2001/12/12 20:41:59 visvan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CheckboxRenderer extends Object implements Renderer {
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

    public CheckboxRenderer() {
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
        if ( c instanceof WSelectBoolean ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_WSELECTBOOLEAN)) {
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
 
        WSelectBoolean wSelectBoolean = null;
        if ( supportsType(c)) {
            wSelectBoolean = (WSelectBoolean) c;
        } else {
            throw new FacesException("Invalid component type. " +
                      "Expected WSelectBoolean");
        }
        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );

        StringBuffer output = new StringBuffer();

        output.append("<input type=\"checkbox\" ");
        if (wSelectBoolean.isSelected(rc)) {
            output.append(" checked");
        }

        // do not render the name and value of the checkbox.The
        // state of this checkbox will be tracked using hidden
        // field because HTML doesn't send the status of the check
        // box during form submissions if it is not selected.

        String cb_name = (String) wSelectBoolean.getAttribute(rc, "name");
        String hiddenFieldname = Constants.REF_HIDDENCHECKBOX + cb_name;
        String clickScript = hiddenFieldname + ".value=this.checked";
        output.append("onClick=\"" + clickScript + "\" ");

        output.append(">");
        if (wSelectBoolean.getAttribute(rc, "label") != null) {
            output.append(" ");
            output.append(wSelectBoolean.getAttribute(rc, "label"));
        }

        // render a hiddenField to track the state of the checkbox
        output.append(" ");
        output.append ("<input type=\"hidden\" name=\"");
        output.append ( hiddenFieldname );
        output.append ("\">");
 
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


} // end of class CheckboxRenderer
