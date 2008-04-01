/*
 * $Id: InputRenderer.java,v 1.6 2001/11/29 01:54:35 rogerk Exp $
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

// InputRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WTextEntry;
import javax.faces.WComponent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>InputRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: InputRenderer.java,v 1.6 2001/11/29 01:54:35 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class InputRenderer extends Object implements Renderer
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

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public InputRenderer()
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

    //
    // Methods From Renderer
    //
    
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

    public void renderStart(RenderContext rc, WComponent c )
            throws IOException, FacesException { 
        
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        WTextEntry textField = null;
        if ( supportsType(c)) {
            textField = (WTextEntry) c;
        } else {
            throw new FacesException("Invalid component type. " +
                     "Expected WTextEntry");
        }
	
        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );

        StringBuffer output = new StringBuffer();
        output.append("<input type=\"text\"");
            
        // render name of the component if specified
        String textField_name = (String)textField.getAttribute(rc, "name");
        if ( textField_name != null ) {
            output.append(" name=\"");
            output.append(textField_name);
            output.append("\"");
        }

        // render default text specified
        String textField_value = textField.getText(rc);
        if ( textField_value != null ) {
            output.append(" value=\"");
            output.append(textField_value);
            output.append("\"");
        }
        //render size if specified
        String textField_size = (String)textField.getAttribute(rc, "size");
        if ( textField_size != null ) {
            output.append(" size=\"");
            output.append(textField_size);
            output.append("\"");
        }
        //render maxlength if specified 
        String textField_ml = (String)textField.getAttribute(rc, "maxlength");
        if ( textField_ml != null ) {
            output.append(" maxlength=\"");
            output.append(textField_ml);
            output.append("\"");
        }
        output.append(">");
        outputMethod.writeText(output.toString());
        outputMethod.flush();
            
        return;
    }

    public void renderChildren(RenderContext rc, WComponent c) 
            throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, WComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_WTEXTENTRY)) {
            supports = true;
        }
        return supports;
    }
    
    public boolean supportsType(WComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof WTextEntry ) {
            supports = true;
        }
        return supports;
    }


} // end of class InputRenderer
