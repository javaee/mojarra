/*
 * $Id: Output_MessageTag.java,v 1.13 2003/09/25 16:36:31 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.BaseComponentTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>output_message</code> custom tag.
 */

public class Output_MessageTag extends BaseComponentTag
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

    public Output_MessageTag()
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
        return "Message"; 
    }
    
    public String getComponentType() { 
        return "Output"; 
    }

    //
    // Methods from TagSupport
    // 

} // end of class Output_MessageTag
