/*
 * $Id: Output_TextTag.java,v 1.48 2003/08/29 16:03:28 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.convert.Converter;

import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>output_text</code> custom tag.
 */

public class Output_TextTag extends FacesTag
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

    private Converter converter = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Output_TextTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    //
    // General Methods
    //

    public String getRendererType() { 
        return "Text"; 
    }
    public String getComponentType() { 
        return "Output"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIOutput textOutput = (UIOutput) component;
        if (converter != null) {
            textOutput.setConverter(converter);
        }
    }
    
    //
    // Methods from TagSupport
    // 

} // end of class Output_TextTag
