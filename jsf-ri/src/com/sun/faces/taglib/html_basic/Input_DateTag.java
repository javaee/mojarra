/*
 * $Id: Input_DateTag.java,v 1.9 2003/08/15 19:15:09 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

/**
 * This class is the tag handler that evaluates the 
 * <code>input_date</code> custom tag.
 */

public class Input_DateTag extends InputTag
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

    public Input_DateTag()
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
        return "Date"; 
    }

    //
    // Methods from TagSupport
    //
    
} // end of class Input_DateTag
