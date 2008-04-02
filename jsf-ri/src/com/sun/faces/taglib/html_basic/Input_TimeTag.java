/*
 * $Id: Input_TimeTag.java,v 1.4 2003/07/09 19:04:24 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Input_TimeTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

/**
 *
 * @version $Id: Input_TimeTag.java,v 1.4 2003/07/09 19:04:24 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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

    public String getLocalRendererType() { return "Time"; }

//
// Methods from TagSupport
//
    


} // end of class Input_TimeTag
