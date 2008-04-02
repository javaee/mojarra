/*
 * $Id: Input_TextTag.java,v 1.13 2003/08/29 16:03:27 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.convert.Converter;


/**
 * This class is the tag handler that evaluates the 
 * <code>input_text</code> custom tag.
 */

public class Input_TextTag extends InputTag
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
    private Converter converter = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    public Input_TextTag() {
        super();
    }

    //
    // Accessors
    //
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public String getRendererType() { return "Text"; }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        UIInput input = (UIInput) component;
        if (converter != null) {           
            input.setConverter(converter);
        }
    }

    //
    // Methods from TagSupport
    //
    
} // end of class Input_TextTag
