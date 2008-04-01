/*
 * $Id: SelectBoolean_CheckboxTag.java,v 1.30 2002/07/12 19:44:37 eburns Exp $
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
 * @version $Id: SelectBoolean_CheckboxTag.java,v 1.30 2002/07/12 19:44:37 eburns Exp $
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

    protected String selected = null;
    protected String label = null;


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

    public void setSelected(String newSelected) {
	selected = newSelected;
    }

    public String getSelected() {
	return selected;
    }

    public void setLabel(String newLabel) {
	label = newLabel;
    }

    public String getLabel() {
	return label;
    }

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
	if (null != checkbox.getAttribute("value")) {
	    if (null != getSelected()) {
		checkbox.setSelected(true);
	    }
	    else {
		checkbox.setSelected(false);
	    }
	}
	if (null == checkbox.getAttribute("label")) {
	    checkbox.setAttribute("label", getLabel());
	}
    }
    
//
// Methods from TagSupport
// 


} // end of class SelectBoolean_CheckboxTag
