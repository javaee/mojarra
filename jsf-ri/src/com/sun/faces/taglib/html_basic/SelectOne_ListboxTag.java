/*
 * $Id: SelectOne_ListboxTag.java,v 1.5 2003/06/27 22:29:34 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectOne_ListboxTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.FacesTag;
import com.sun.faces.RIConstants;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: SelectOne_ListboxTag.java,v 1.5 2003/06/27 22:29:34 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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

    public String getLocalRendererType() { return "Listbox"; }
    public String getComponentType() { return "SelectOne"; }
    public UIComponent createComponent() {
        return (new UISelectOne());
    }

//
// Methods from TagSupport
// 

    public int doEndTag() throws JspException {
	UISelectOne component = (UISelectOne) getComponent();

	// This makes sure the no more SelectItems get added to this
	// selectOne instance.
	component.setAttribute(RIConstants.SELECTITEMS_CONFIGURED, 
			       RIConstants.SELECTITEMS_CONFIGURED);
	int rc = super.doEndTag();

	return rc;
    }
    
    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	
        if (null != getSize()) {
	    component.setAttribute("size", getSize());
	}
        if (null != getOnselect()) {
	    component.setAttribute("onselect", getOnselect());
	}
        if (null != getOnchange()) {
	    component.setAttribute("onchange", getOnchange());
	}
    }


} // end of class SelectOne_ListboxTag
