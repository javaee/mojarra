/*
 * $Id: InputRenderer.java,v 1.14 2002/03/16 00:09:36 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
import javax.faces.UITextEntry;
import javax.faces.UIComponent;

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
 * @version $Id: InputRenderer.java,v 1.14 2002/03/16 00:09:36 eburns Exp $
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

    public void renderStart(RenderContext rc, UIComponent c )
            throws IOException, FacesException { 
        
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        UITextEntry textField = null;
        if ( supportsComponentType(c)) {
            textField = (UITextEntry) c;
        } else {
            throw new FacesException("Invalid component type. " +
                     "Expected UITextEntry");
        }
	
        String textFieldId = textField.getId();
        Assert.assert_it(null != textFieldId);

        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );

        StringBuffer output = new StringBuffer();
        output.append("<INPUT TYPE=\"text\"");
            
        output.append(" NAME=\"");
        output.append(textFieldId);
        output.append("\"");

        // render default text specified
        String textField_value = textField.getText(rc);
        if ( textField_value != null ) {
            output.append(" VALUE=\"");
            output.append(textField_value);
            output.append("\"");
        }
        //render size if specified
        String textField_size = (String)textField.getAttribute(rc, "size");
        if ( textField_size != null ) {
            output.append(" SIZE=\"");
            output.append(textField_size);
            output.append("\"");
        }
        //render maxlength if specified 
        String textField_ml = (String)textField.getAttribute(rc, "maxlength");
        if ( textField_ml != null ) {
            output.append(" MAXLENGTH=\"");
            output.append(textField_ml);
            output.append("\"");
        }
        output.append(">");
        outputMethod.writeText(output.toString());
        outputMethod.flush();
            
        return;
    }

    public void renderChildren(RenderContext rc, UIComponent c) 
            throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, UIComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean supportsComponentType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_UITEXTENTRY)) {
            supports = true;
        }
        return supports;
    }
    
    public boolean supportsComponentType(UIComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof UITextEntry ) {
            supports = true;
        }
        return supports;
    }


} // end of class InputRenderer
