/*
 * $Id: Input_DateTag.java,v 1.1 2002/08/12 19:57:37 eburns Exp $
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

import com.sun.faces.taglib.FacesTag;

/**
 *
 * @version $Id: Input_DateTag.java,v 1.1 2002/08/12 19:57:37 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Input_DateTag extends FacesTag
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

public String getLabel()
{
    return label;
}

public void setLabel(String newLabel)
{
    label = newLabel;
}


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
	
	if (null == input.getValue() && null != getValue()) {
	    input.setValue(getValue());
	}
	if (null == input.getAttribute("timezone")) {
	    input.setAttribute("timezone", getTimezone());
	}
	if (null == input.getAttribute("disabled")) {
	    input.setAttribute("disabled", getDisabled());
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
	if (null == input.getAttribute("lang")) {
	    input.setAttribute("lang", getLang());
	}
	if (null == input.getAttribute("tabindex")) {
	    input.setAttribute("tabindex", getTabindex());
	}
	if (null == input.getAttribute("accesskey")) {
	    input.setAttribute("accesskey", getAccesskey());
	}
	if (null == input.getAttribute("onfocus")) {
	    input.setAttribute("onfocus", getOnfocus());
	}
	if (null == input.getAttribute("onblur")) {
	    input.setAttribute("onblur", getOnblur());
	}
	if (null == input.getAttribute("onselect")) {
	    input.setAttribute("onselect", getOnselect());
	}
	if (null == input.getAttribute("onchange")) {
	    input.setAttribute("onchange", getOnchange());
	}
    }

    
//
// Methods from TagSupport
//
    


} // end of class Input_DateTag
