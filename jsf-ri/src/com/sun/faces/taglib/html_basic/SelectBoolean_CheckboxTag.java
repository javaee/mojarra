/*
 * $Id: SelectBoolean_CheckboxTag.java,v 1.45 2003/09/05 14:34:47 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;

import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>select_boolean</code> custom tag.
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

    public String getRendererType() { 
        return "Checkbox"; 
    }
    public String getComponentType() { 
        return "SelectBoolean"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectBoolean checkbox = (UISelectBoolean) component;
	
        if (null != checked) {
            checkbox.setSelected(true);
        } 
       
        if (null != size) {
	    checkbox.setAttribute("size", size);
	}
        if (null != readonly) {
	    checkbox.setAttribute("readonly", readonly);
	}
        if (null != alt) {
	    checkbox.setAttribute("alt", alt);
	}
        if (null != onselect) {
	    checkbox.setAttribute("onselect", onselect);
	}
        if (null != onchange) {
	    checkbox.setAttribute("onchange", onchange);
	}
    }
    
    //
    // Methods from TagSupport
    // 


} // end of class SelectBoolean_CheckboxTag
