/*
 * $Id: Output_TextTag.java,v 1.44 2003/07/09 19:04:25 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_TextTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: Output_TextTag.java,v 1.44 2003/07/09 19:04:25 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_TextTag extends FacesTag
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

    private String converter = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public Output_TextTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//
    public void setConverter(String converter) {
        this.converter = converter;
    }

//
// General Methods
//

    public String getLocalRendererType() { return "Text"; }
    public String getComponentType() { return "Output"; }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIOutput textOutput = (UIOutput) component;
	// if component has non null value, do not call setValue().
	if (null != getValue()) {
	    textOutput.setValue(getValue());
	}
        if (converter != null) {
            component.setAttribute("converter", converter);
        }
    }
    
//
// Methods from TagSupport
// 


} // end of class Output_TextTag
