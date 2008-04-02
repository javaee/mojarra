/*
 * $Id: Output_TimeTag.java,v 1.11 2003/08/13 02:08:08 eburns Exp $
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
 * <code>output_time</code> custom tag.
 */

public class Output_TimeTag extends com.sun.faces.taglib.FacesTag
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

    public Output_TimeTag()
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
        return "Time"; 
    }
    public String getComponentType() { 
        return "Output"; 
    }


    //
    // Methods from TagSupport
    //
    
} // end of class Output_TimeTag
