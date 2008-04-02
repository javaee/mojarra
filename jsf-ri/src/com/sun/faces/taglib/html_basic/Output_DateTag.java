/*
 * $Id: Output_DateTag.java,v 1.10 2003/07/16 00:00:11 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * This class is the tag handler that evaluates the 
 * <code>output_date</code> custom tag.
 */

public class Output_DateTag extends com.sun.faces.taglib.FacesTag
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

    public Output_DateTag()
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
    public String getLocalRendererType() { 
        return "Date"; 
    }
    public String getComponentType() { 
        return "Output"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
        UIOutput uiOutput = (UIOutput)component;
	if (null != value) {
	    uiOutput.setValue(value);
	}
    }


    //
    // Methods from TagSupport
    //
} // end of class Output_DateTag
