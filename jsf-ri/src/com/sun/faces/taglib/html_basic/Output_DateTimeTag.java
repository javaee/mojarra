/*
 * $Id: Output_DateTimeTag.java,v 1.12 2003/08/15 19:15:12 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

/**
 *
 * @version $Id: Output_DateTimeTag.java,v 1.12 2003/08/15 19:15:12 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_DateTimeTag extends com.sun.faces.taglib.FacesTag
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

    public Output_DateTimeTag()
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
        return "DateTime"; 
    }
    public String getComponentType() { 
        return "Output"; 
    }

    //
    // Methods from TagSupport
    //
} // end of class Output_DateTimeTag
