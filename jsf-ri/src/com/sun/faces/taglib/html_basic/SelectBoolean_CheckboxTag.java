/*
 * $Id: SelectBoolean_CheckboxTag.java,v 1.42 2003/07/09 19:04:26 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
 * @version $Id: SelectBoolean_CheckboxTag.java,v 1.42 2003/07/09 19:04:26 rlubke Exp $
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

    public String getLocalRendererType() { return "Checkbox"; }
    public String getComponentType() { return "SelectBoolean"; }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectBoolean checkbox = (UISelectBoolean) component;
	
        // Non-null implies this UISelectBooleanInstance has a model value.
        // In that case, we don't set a value.
        if (null == checkbox.getValue()) {
            if (null != getChecked()) {
                checkbox.setSelected(true);
            } 
        }
        
        if (null != getSize()) {
	    checkbox.setAttribute("size", getSize());
	}
        if (null != getReadonly()) {
	    checkbox.setAttribute("readonly", getReadonly());
	}
        if (null != getAlt()) {
	    checkbox.setAttribute("alt", getAlt());
	}
        if (null != getOnselect()) {
	    checkbox.setAttribute("onselect", getOnselect());
	}
        if (null != getOnchange()) {
	    checkbox.setAttribute("onchange", getOnchange());
	}
    }
    
//
// Methods from TagSupport
// 


} // end of class SelectBoolean_CheckboxTag
