/*
 * $Id: Input_TimeTag.java,v 1.6 2003/08/15 19:15:11 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

/**
 * This class is the tag handler that evaluates the 
 * <code>input_time</code> custom tag.
 */
public class Input_TimeTag extends InputTag
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

    public Input_TimeTag()
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
        return "Time"; 
    }

    //
    // Methods from TagSupport
    //
    
} // end of class Input_TimeTag
