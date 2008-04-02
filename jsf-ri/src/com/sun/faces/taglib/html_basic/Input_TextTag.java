/*
 * $Id: Input_TextTag.java,v 1.11 2003/08/15 19:15:11 rlubke Exp $
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
    private String converter = null;

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
    public void setConverter(String converter) {
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
        if (converter != null) {
            component.setConverter(converter);
        }
    }

    //
    // Methods from TagSupport
    //
    
} // end of class Input_TextTag
