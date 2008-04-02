/*
 * $Id: SelectOne_ListboxTag.java,v 1.12 2003/10/07 13:05:36 eburns Exp $
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
import com.sun.faces.taglib.BaseComponentTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>selectmany_listbox</code> custom tag.
 */

public class SelectOne_ListboxTag extends BaseComponentTag
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
        return "SelectOneListbox"; 
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
	
        if (size != Integer.MIN_VALUE) {
	    component.getAttributes().put("size", new Integer(size));
	}
        if (null != onselect) {
	    component.getAttributes().put("onselect", onselect);
	}
        if (null != onchange) {
	    component.getAttributes().put("onchange", onchange);
	}
    }


} // end of class SelectOne_ListboxTag
