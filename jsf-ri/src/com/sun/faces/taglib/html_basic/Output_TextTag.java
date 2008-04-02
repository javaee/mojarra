/*
 * $Id: Output_TextTag.java,v 1.50 2003/09/25 16:36:31 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.BaseComponentTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>output_text</code> custom tag.
 */

public class Output_TextTag extends BaseComponentTag
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
    
    //
    // General Methods
    //

    public String getRendererType() { 
        return "Text"; 
    }
    public String getComponentType() { 
        return "Output"; 
    }

    //
    // Methods from TagSupport
    // 

} // end of class Output_TextTag
