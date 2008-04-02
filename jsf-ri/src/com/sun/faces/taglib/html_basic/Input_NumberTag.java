/*
 * $Id: Input_NumberTag.java,v 1.7 2003/07/16 00:00:10 jvisvanathan Exp $
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
 * <code>input_number</code> custom tag.
 */

public class Input_NumberTag extends InputTag
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
    protected String numberStyle = null;
    
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Input_NumberTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    public String getNumberStyle() {
	return numberStyle;
    }
    
    public void setNumberStyle(String newFormatStyle) {
	numberStyle = newFormatStyle;
    }

    //
    // General Methods
    //

    public String getLocalRendererType() { 
        return "Number"; 
    }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if (null != numberStyle) {
            component.setAttribute("numberStyle", numberStyle);
        }
    }
    
    //
    // Methods from TagSupport
    // 


} // end of class Input_NumberTag
