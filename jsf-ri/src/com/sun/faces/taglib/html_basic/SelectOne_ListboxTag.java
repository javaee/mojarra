/*
 * $Id: SelectOne_ListboxTag.java,v 1.4 2003/04/29 20:52:09 eburns Exp $
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
 * @version $Id: SelectOne_ListboxTag.java,v 1.4 2003/04/29 20:52:09 eburns Exp $
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
	
        if (null == component.getAttribute("size")) {
	    component.setAttribute("size", getSize());
	}
        if (null == component.getAttribute("onselect")) {
	    component.setAttribute("onselect", getOnselect());
	}
	if (null == component.getAttribute("onchange")) {
	    component.setAttribute("onchange", getOnchange());
	}
    }


} // end of class SelectOne_ListboxTag
