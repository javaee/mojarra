/*
 * $Id: Input_DateTimeTag.java,v 1.5 2003/07/16 00:00:10 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

/**
 * This class is the tag handler that evaluates the 
 * <code>input_datetime</code> custom tag.
 */

public class Input_DateTimeTag extends InputTag
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

    public Input_DateTimeTag()
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
        return "DateTime"; 
    }

    //
    // Methods from TagSupport
    //
    
} // end of class Input_DateTimeTag
