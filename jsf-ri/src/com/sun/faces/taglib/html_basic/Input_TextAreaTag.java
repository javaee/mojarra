/*
 * $Id: Input_TextAreaTag.java,v 1.10 2003/09/05 14:34:46 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

/**
 * This class is the tag handler that evaluates the 
 * <code>input_textarea</code> custom tag.
 */

public class Input_TextAreaTag extends Input_TextTag
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

    public Input_TextAreaTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    //
    // General Methods
    //

    public String getRendererType() { 
        return "Textarea"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIInput inputTextArea = (UIInput) component;
	
	if (null != rows) {
	    inputTextArea.setAttribute("rows", rows);
	}
	if (null != cols) {
	    inputTextArea.setAttribute("cols", cols);
	}
    }
    //
    // Methods from TagSupport
    // 


} // end of class Input_TextAreaTag
