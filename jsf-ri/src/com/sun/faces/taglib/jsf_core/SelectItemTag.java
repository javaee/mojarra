/*
 * $Id: SelectItemTag.java,v 1.6 2004/01/30 22:58:08 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;


/**
 * This class is the tag handler that evaluates the 
 * <code>selectitem</code> custom tag.
 */

public class SelectItemTag extends BaseComponentTag
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

    protected String itemValue = null;
    protected String itemLabel = null;
    protected String description = null;
   
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

    public void setItemValue(String value) {
        this.itemValue = value;
    }

    public void setItemLabel(String label) {
        this.itemLabel = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

    //
    // General Methods
    //
    public String getRendererType() { 
        return null;
    }
    public String getComponentType() { 
        return "javax.faces.SelectItem"; 
    }
    
    //
    // Methods from BaseComponentTag
    //

    protected void setProperties(UIComponent component) {
	super.setProperties(component);
	UISelectItem selectItem = (UISelectItem) component;
	
        if (null != value) {
            if (isValueReference(value)) {
                component.setValueBinding("value",
                                          Util.getValueBinding(value));
             } else {
                 selectItem.setValue(value);
             }
	}

	if (null != itemValue) {
	    if (isValueReference(itemValue)) {
		selectItem.setValueBinding("itemValue", 
					   Util.getValueBinding(itemValue));
	    }
	    else {
		selectItem.setItemValue(itemValue);
	    }
	}
	if (null != itemLabel) {
	    if (isValueReference(itemLabel)) {
		selectItem.setValueBinding("itemLabel", 
					   Util.getValueBinding(itemLabel));
	    }
	    else {
		selectItem.setItemLabel(itemLabel);
	    }
	}
	if (null != description) {
	    if (isValueReference(description)) {
		selectItem.setValueBinding("description", 
					   Util.getValueBinding(description));
	    }
	    else {
		selectItem.setItemDescription(description);
	    }
	}


	if (null != super.disabled) {
	    if (isValueReference(super.disabled)) {
		selectItem.setValueBinding("disabled", 
					   Util.getValueBinding(super.disabled));
	    }
	    else {
		selectItem.setItemDisabled((Boolean.valueOf(super.disabled)).
                                            booleanValue());
	    }
	}
        
        
    }

} // end of class SelectItemTag
