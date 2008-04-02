/*
 * $Id: Output_NumberTag.java,v 1.11 2003/08/13 02:08:08 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>output_number</code> custom tag.
 */

public class Output_NumberTag extends FacesTag
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

    public Output_NumberTag()
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
    public String getComponentType() { 
        return "Output"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIOutput output = (UIOutput) component;
        if (null != numberStyle) {
	    component.setAttribute("numberStyle", numberStyle);
        }
     }   
    
    
    //
    // Methods from TagSupport
    // 


} // end of class Output_NumberTag
