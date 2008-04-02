/*
 * $Id: Input_NumberTag.java,v 1.6 2003/07/09 19:04:23 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Input_NumberTag.java

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
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: Input_NumberTag.java,v 1.6 2003/07/09 19:04:23 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Input_NumberTag extends InputTag
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
    protected String numberStyle = null;
    
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Input_NumberTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    public String getNumberStyle() {
	return numberStyle;
    }
    
    public void setNumberStyle(String newFormatStyle) {
	numberStyle = newFormatStyle;
    }

    //
    // General Methods
    //

    public String getLocalRendererType() { return "Number"; }

    protected void overrideProperties(UIComponent component) {
    super.overrideProperties(component);
    
    if (null != getNumberStyle()) {
	component.setAttribute("numberStyle", getNumberStyle());
    }
}
    
    //
    // Methods from TagSupport
    // 


} // end of class Input_NumberTag
