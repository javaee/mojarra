/*
 * $Id: SelectBoolean_CheckboxTag.java,v 1.43 2003/07/16 00:00:12 jvisvanathan Exp $
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

    public String getLocalRendererType() { 
        return "Checkbox"; 
    }
    public String getComponentType() { 
        return "SelectBoolean"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectBoolean checkbox = (UISelectBoolean) component;
	
        if (null != getChecked()) {
            checkbox.setSelected(true);
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
