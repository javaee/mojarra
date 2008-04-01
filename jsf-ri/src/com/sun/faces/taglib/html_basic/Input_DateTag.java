/*
 * $Id: Input_DateTag.java,v 1.3 2002/08/13 18:29:51 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Input_DateTag.java

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
 * @version $Id: Input_DateTag.java,v 1.3 2002/08/13 18:29:51 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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
protected String timezone = null;

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

public String getTimezone()
{
    return timezone;
}

public void setTimezone(String newTimezone)
{
    timezone = newTimezone;
}

// 
// Accessors
//


//
// General Methods
//

    public String getLocalRendererType() { return "DateRenderer"; }

    public UIComponent createComponent() {
        return (new UIInput());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIInput input = (UIInput) component;
	
	if (null == input.getAttribute("timezone")) {
	    input.setAttribute("timezone", getTimezone());
	}
    }

    
//
// Methods from TagSupport
//
    


} // end of class Input_DateTag
