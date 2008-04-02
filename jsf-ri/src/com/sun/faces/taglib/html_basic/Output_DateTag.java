/*
 * $Id: Output_DateTag.java,v 1.12 2003/08/15 19:15:12 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * This class is the tag handler that evaluates the 
 * <code>output_date</code> custom tag.
 */

public class Output_DateTag extends com.sun.faces.taglib.FacesTag
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

    public Output_DateTag()
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
    public String getComponentType() { 
        return "Output"; 
    }

    //
    // Methods from TagSupport
    //
} // end of class Output_DateTag
