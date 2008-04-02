/*
 * $Id: InputTag.java,v 1.7 2003/07/07 20:53:03 eburns Exp $
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
 * @version $Id: InputTag.java,v 1.7 2003/07/07 20:53:03 eburns Exp $
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
    // Accessors
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
    // General Methods
    //

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIInput input = (UIInput) component;

        if (null != getValue()) {
	    input.setValue(getValue());
	}
        if (null != getReadonly()) {
	    input.setAttribute("readonly", getReadonly());
	}
	if (null != getSize()) {
	    input.setAttribute("size", getSize());
	}
	if (null != getMaxlength()) {
	    input.setAttribute("maxlength", getMaxlength());
	}
	if (null != getAlt()) {
	    input.setAttribute("alt", getAlt());
	}
        if (null != getOnselect()) {
	    input.setAttribute("onselect", getOnselect());
	}
	if (null != getOnchange()) {
	    input.setAttribute("onchange", getOnchange());
	}
    }

    public String getComponentType() { return "Input"; }
    
//
// Methods from TagSupport
//
    


} // end of class Input_DateTag
