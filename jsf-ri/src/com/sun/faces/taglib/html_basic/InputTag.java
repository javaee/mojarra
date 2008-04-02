/*
 * $Id: InputTag.java,v 1.6 2003/04/29 20:52:06 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// InputTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.FacesTag;

/**
 *
 * @version $Id: InputTag.java,v 1.6 2003/04/29 20:52:06 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public abstract class InputTag extends FacesTag
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
    protected String label = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public InputTag()
    {
        super();
    }

    //
    // Class methods
    //

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String newLabel)
    {
        label = newLabel;
    }

    // 
    // Accessors
    //


    //
    // General Methods
    //

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIInput input = (UIInput) component;
        if (null == input.getValue() && null != getValue()) {
	    input.setValue(getValue());
	}
        if (null == input.getAttribute("readonly")) {
	    input.setAttribute("readonly", getReadonly());
	}
	if (null == input.getAttribute("size")) {
	    input.setAttribute("size", getSize());
	}
	if (null == input.getAttribute("maxlength")) {
	    input.setAttribute("maxlength", getMaxlength());
	}
	if (null == input.getAttribute("alt")) {
	    input.setAttribute("alt", getAlt());
	}
        if (null == input.getAttribute("onselect")) {
	    input.setAttribute("onselect", getOnselect());
	}
	if (null == input.getAttribute("onchange")) {
	    input.setAttribute("onchange", getOnchange());
	}
    }

    public String getComponentType() { return "Input"; }
    
//
// Methods from TagSupport
//
    


} // end of class Input_DateTag
