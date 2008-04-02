/*
 * $Id: InputTag.java,v 1.11 2003/09/08 20:10:12 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import com.sun.faces.taglib.FacesTag;


/**
 * This class acts as the base class for input tags.
 */

public abstract class InputTag extends FacesTag
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
    protected String label = null;
    protected boolean required = false;
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public InputTag()
    {
        super();
    }

    // 
    // Accessors
    //
    
     public void setRequired(boolean newVal) {
	required = newVal;
    }

    public void setLabel(String newLabel)
    {
        label = newLabel;
    }

    //
    // General Methods
    //

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIInput input = (UIInput) component;

        if (null != readonly) {
	    input.setAttribute("readonly", readonly);
	}
	if (null != size) {
	    input.setAttribute("size", size);
	}
	if (null != maxlength) {
	    input.setAttribute("maxlength", maxlength);
	}
	if (null != alt) {
	    input.setAttribute("alt", alt);
	}
        if (null != onselect) {
	    input.setAttribute("onselect", onselect);
	}
	if (null != onchange) {
	    input.setAttribute("onchange", onchange);
	}
        input.setRequired(required);
    }

    public String getComponentType() { return "Input"; }
    
    //
    // Methods from TagSupport
    //
    
} // end of class Input_DateTag
