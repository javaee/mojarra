/*
 * $Id: SelectItemTag.java,v 1.8 2002/09/09 23:52:26 visvan Exp $
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
import javax.faces.component.UISelectMany;
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
 * @version $Id: SelectItemTag.java,v 1.8 2002/09/09 23:52:26 visvan Exp $
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
    public String itemValue = null;
    public String itemLabel = null;
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

    public String getItemValue() {
        return itemValue;
    }

    public String getId() {
        return getItemValue();
    }

    public void setItemValue(String value) {
        this.itemValue = value;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String label) {
        this.itemLabel = label;
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
	UIComponent parent = selectItem.getParent();
	Assert.assert_it(null != parent);
	
	if (null == selectItem.getItemValue()) {
	    selectItem.setItemValue(getItemValue());
	}
	if (null == selectItem.getItemLabel()) {
	    selectItem.setItemLabel(getItemLabel());
	}
	if (null == selectItem.getItemDescription()) {
	    selectItem.setItemDescription(getDescription());
	}

	if (parent.getComponentType().equals(UISelectOne.TYPE)) {
	    UISelectOne selectOne = (UISelectOne) parent;
	    // If this SelectItemTag instance is selected and
	    // there is no selected item in our UISelectOne...
	    if (null != getSelected() && null == selectOne.getValue()) {
		selectOne.setSelectedValue(selectItem.getItemValue());
	    }
	}
	else if (parent.getComponentType().equals(UISelectMany.TYPE)) {
	    UISelectMany selectMany = (UISelectMany) parent;
	    Object newSelectItems[] = null, selectItems[] = null;
	    int len, i = 0;
	    boolean foundMatch = false;
	    // If this SelectItemTag instance is marked as selected and
	    // there is no Value in the UISelectMany values that matches
	    // the current value
	    if (null != getSelected()) {
		// If there are no selected values
		if (null == (selectItems = selectMany.getSelectedValues())) {
		    // create some.
		    selectItems = new Object[] { selectItem.getItemValue() };
		}
		else {
		    // Search the items for a match.
		    len = selectItems.length;
		    for (i = 0; i < len; i++) {
			if (foundMatch = selectItems[i] == 
			    selectItem.getItemValue()) {
			    break;
			}
		    }
		    if (!foundMatch) {
			newSelectItems = new Object[len+1];
			System.arraycopy(selectItems, 0, newSelectItems,0,len);
			newSelectItems[len] = selectItem.getItemValue();
			selectItems = newSelectItems;
			newSelectItems = null;
		    }
		}
		Assert.assert_it(null != selectItems);
		selectMany.setSelectedValues(selectItems);
	    }
	}
    }



} // end of class SelectItemTag
