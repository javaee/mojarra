/*
 * $Id: SelectItemTag.java,v 1.10 2004/02/26 20:33:18 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;


/**
 * This class is the tag handler that evaluates the
 * <code>selectitem</code> custom tag.
 */

public class SelectItemTag extends BaseComponentTag {

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
    protected String itemDescription = null;
    protected String itemDisabled = null;
   
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectItemTag() {
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


    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemDisabled(String itemDisabled) {
        this.itemDisabled = itemDisabled;
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
            } else {
                selectItem.setItemValue(itemValue);
            }
        }
        if (null != itemLabel) {
            if (isValueReference(itemLabel)) {
                selectItem.setValueBinding("itemLabel",
                                           Util.getValueBinding(itemLabel));
            } else {
                selectItem.setItemLabel(itemLabel);
            }
        }
        if (null != itemDescription) {
            if (isValueReference(itemDescription)) {
                selectItem.setValueBinding("itemDescription",
                                           Util.getValueBinding(itemDescription));
            } else {
                selectItem.setItemDescription(itemDescription);
            }
        }


        if (null != itemDisabled) {
            if (isValueReference(itemDisabled)) {
                selectItem.setValueBinding("itemDisabled",
                                           Util.getValueBinding(itemDisabled));
            } else {
                selectItem.setItemDisabled((Boolean.valueOf(itemDisabled)).
                                           booleanValue());
            }
        }


    }

} // end of class SelectItemTag
