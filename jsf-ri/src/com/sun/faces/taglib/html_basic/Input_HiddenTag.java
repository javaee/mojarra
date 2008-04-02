/*
 * $Id: Input_HiddenTag.java,v 1.11 2003/07/16 00:00:10 jvisvanathan Exp $
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
 * This class is the tag handler that evaluates the 
 * <code>input_hidden</code> custom tag.
 */

public class Input_HiddenTag extends FacesTag
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

    private String converter = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Input_HiddenTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    public void setConverter(String converter) {
        this.converter = converter;
    }

    //
    // General Methods
    //

    public String getLocalRendererType() { 
        return "Hidden"; 
    }
    public String getComponentType() { 
        return "Input"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
        UIInput uiInput = (UIInput)component;
        if (null != value) {
	    uiInput.setValue(value);
	}
        if (converter != null) {
            component.setConverter(converter);
        }
    }
    
    //
    // Methods from TagSupport
    // 


} // end of class Input_HiddenTag
