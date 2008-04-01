/*
 * $Id: SelectItemTag.java,v 1.4 2002/08/14 19:11:25 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectItemTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIComponent;
import javax.faces.component.SelectItem;
import javax.faces.component.UISelectItem;
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
 * @version $Id: SelectItemTag.java,v 1.4 2002/08/14 19:11:25 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectItemTag extends FacesTag
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

    public String selected = null;
    public String value = null;
    public String label = null;
    public String description = null;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public SelectItemTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return getValue();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//
// General Methods
//
    public String getLocalRendererType() { return null; }


//
// Methods from FacesTag
// 
    public UIComponent createComponent() {
        return (new UISelectItem());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectItem selectItem = (UISelectItem) component;
	UISelectOne selectOne = (UISelectOne) selectItem.getParent();
	
	if (null == selectItem.getItemValue()) {
	    selectItem.setItemValue(getValue());
	}
	if (null == selectItem.getItemLabel()) {
	    selectItem.setItemLabel(getLabel());
	}
	if (null == selectItem.getItemDescription()) {
	    selectItem.setItemDescription(getDescription());
	}
	// If this SelectItemTag instance is selected and
	// there is no selected item in our UISelectOne...
	if (null != getSelected() && null == selectOne.getSelectedValue()) {
	    selectOne.setSelectedValue(selectItem.getItemValue());
	}
    }



} // end of class SelectItemTag
