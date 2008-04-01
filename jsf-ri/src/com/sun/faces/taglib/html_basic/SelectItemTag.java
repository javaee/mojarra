/*
 * $Id: SelectItemTag.java,v 1.2 2002/07/15 23:48:32 eburns Exp $
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
import javax.faces.component.SelectItem;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;
import com.sun.faces.taglib.FacesTag;
import com.sun.faces.RIConstants;

import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: SelectItemTag.java,v 1.2 2002/07/15 23:48:32 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectItemTag extends TagSupport
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

//
// Methods from TagSupport
// 

    public int doStartTag() throws JspException {
	FacesTag parent = null;
	UISelectOne uiSelectOne = null;

	parent = (FacesTag) findAncestorWithClass(this, FacesTag.class);
	Assert.assert_it(null != parent);

	uiSelectOne = (UISelectOne) parent.getComponent();
	Assert.assert_it(null != uiSelectOne);

	// If the SelectItems have already been configured for this tag
	if (null !=uiSelectOne.getAttribute(RIConstants.SELECTITEMS_CONFIGURED)
	    &&
	    ((String)uiSelectOne.getAttribute(RIConstants.SELECTITEMS_CONFIGURED)).equals(RIConstants.SELECTITEMS_CONFIGURED)) {
	    // do nothing.
	    return EVAL_BODY_INCLUDE;
	}

	SelectItem [] oldItems = (SelectItem []) uiSelectOne.getItems();
	SelectItem [] newItems = null;
	
	if (null != oldItems) {
	    newItems = new SelectItem[oldItems.length + 1];
	    System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);
	    newItems[oldItems.length] = new SelectItem(getValue(), getLabel(), 
						       getDescription());
	}
	else {
	    newItems = new SelectItem[1];
	    newItems[0] = new SelectItem(getValue(), getLabel(), 
					 getDescription());
	}
	
	uiSelectOne.setItems(newItems);
	// if it is checked, make sure the model knows about it.
	// we should update selectedValue only if it is null
	// in the model bean otherwise we would be overwriting
	// the value in model bean, losing any earlier updates. 
	if ((null != getSelected()) && 
	    (null == uiSelectOne.getSelectedValue())) {
	    uiSelectOne.setSelectedValue(getValue());
	}
	
	return EVAL_BODY_INCLUDE;
    }


} // end of class SelectItemTag
