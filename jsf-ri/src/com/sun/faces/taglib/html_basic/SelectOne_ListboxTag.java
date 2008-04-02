/*
 * $Id: SelectOne_ListboxTag.java,v 1.9 2003/09/24 23:17:27 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;

import com.sun.faces.RIConstants;
import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>selectmany_listbox</code> custom tag.
 */

public class SelectOne_ListboxTag extends FacesTag
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

    public SelectOne_ListboxTag()
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
        return "Listbox"; 
    }
    public String getComponentType() { 
        return "SelectOne"; 
    }

    //
    // Methods from TagSupport
    // 

    public int doEndTag() throws JspException {
	UISelectOne component = (UISelectOne) getComponent();

	// This makes sure the no more SelectItems get added to this
	// selectOne instance.
	component.getAttributes().put(RIConstants.SELECTITEMS_CONFIGURED, 
			              RIConstants.SELECTITEMS_CONFIGURED);
	int rc = super.doEndTag();
	return rc;
    }
    
    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	
        if (null != size) {
	    component.getAttributes().put("size", size);
	}
        if (null != onselect) {
	    component.getAttributes().put("onselect", onselect);
	}
        if (null != onchange) {
	    component.getAttributes().put("onchange", onchange);
	}
    }


} // end of class SelectOne_ListboxTag
