/*
 * $Id: SelectBoolean_CheckboxTag.java,v 1.31 2002/08/14 22:01:36 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectBoolean_CheckboxTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
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
 * @version $Id: SelectBoolean_CheckboxTag.java,v 1.31 2002/08/14 22:01:36 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectBoolean_CheckboxTag extends FacesTag
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

    public SelectBoolean_CheckboxTag()
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

    public String getLocalRendererType() { return "CheckboxRenderer"; }

    public UIComponent createComponent() {
        return (new UISelectBoolean());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectBoolean checkbox = (UISelectBoolean) component;
	
        // Non-null implies this UISelectBooleanInstance has a value.
        // In that case, we don't set a value.
        // PENDING (visvan) check for null case instead of non null.
        if (null != checkbox.getAttribute("value")) {
            if (null != getSelected()) {
                checkbox.setSelected(true);
            } else {
                checkbox.setSelected(false);
            }
        }
        
	if (null == checkbox.getAttribute("size")) {
	    checkbox.setAttribute("size", getSize());
	}
        if (null == checkbox.getAttribute("disabled")) {
	    checkbox.setAttribute("disabled", getDisabled());
	}
	if (null == checkbox.getAttribute("readonly")) {
	    checkbox.setAttribute("readonly", getReadonly());
	}
	if (null == checkbox.getAttribute("alt")) {
	    checkbox.setAttribute("alt", getAlt());
	}
	if (null == checkbox.getAttribute("lang")) {
	    checkbox.setAttribute("lang", getLang());
	}
	if (null == checkbox.getAttribute("tabindex")) {
	    checkbox.setAttribute("tabindex", getTabindex());
	}
	if (null == checkbox.getAttribute("accesskey")) {
	    checkbox.setAttribute("accesskey", getAccesskey());
	}
	if (null == checkbox.getAttribute("onfocus")) {
	    checkbox.setAttribute("onfocus", getOnfocus());
	}
	if (null == checkbox.getAttribute("onblur")) {
	    checkbox.setAttribute("onblur", getOnblur());
	}
	if (null == checkbox.getAttribute("onselect")) {
	    checkbox.setAttribute("onselect", getOnselect());
	}
	if (null == checkbox.getAttribute("onchange")) {
	    checkbox.setAttribute("onchange", getOnchange());
	}
    }
    
//
// Methods from TagSupport
// 


} // end of class SelectBoolean_CheckboxTag
