/*
 * $Id: Output_DateTimeTag.java,v 1.4 2002/10/22 21:26:59 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_DateTimeTag.java

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
 * @version $Id: Output_DateTimeTag.java,v 1.4 2002/10/22 21:26:59 jvisvanathan Exp $
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
    public String getLocalRendererType() { return "DateTime"; }

    public UIComponent createComponent() {
        return (new UIOutput());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	// if component has non null value, do not call setValue().
	if (null == component.getValue() && null != getValue()) {
	    component.setValue(getValue());
	}
    }


//
// Methods from TagSupport
//
    


} // end of class Output_DateTimeTag
