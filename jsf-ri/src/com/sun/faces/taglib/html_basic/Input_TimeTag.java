/*
 * $Id: Input_TimeTag.java,v 1.3 2003/02/20 22:49:17 ofung Exp $
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
 * @version $Id: Input_TimeTag.java,v 1.3 2003/02/20 22:49:17 ofung Exp $
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

    public UIComponent createComponent() {
        return (new UIInput());
    }

//
// Methods from TagSupport
//
    


} // end of class Input_TimeTag
