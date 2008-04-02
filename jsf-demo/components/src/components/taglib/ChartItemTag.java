/*
 * $Id: ChartItemTag.java,v 1.1 2004/03/06 01:58:09 jvisvanathan Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.taglib;

import components.components.ChartItemComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * This class is the tag handler that processes the <code>chartItem</code> 
 * custom tag.
 */

public class ChartItemTag extends UIComponentTag {

    public ChartItemTag() {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    private String itemLabel = null;
    public void setItemLabel(String label) {
        this.itemLabel = label;
    }

    private String itemColor = null;
    public void setItemColor(String color) {
        this.itemColor = color;
    }
    
    private String itemValue = null;
    public void setItemValue(String itemVal) {
        this.itemValue = itemVal;
    }

    private String value = null;
    public void setValue(String value) {
        this.value = value;
    }

    //
    // General Methods
    //
    public String getRendererType() {
        return null;
    }


    public String getComponentType() {
        return "ChartItem";
    }
    
    //
    // Methods from BaseComponentTag
    //

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        ChartItemComponent chartItem = (ChartItemComponent) component;
        if (null != value) {
            if (isValueReference(value)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(value);
                chartItem.setValueBinding("value", vb);
            } else {
                chartItem.setValue(value);
            }
        }

        if (null != itemLabel) {
            if (isValueReference(itemLabel)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(itemLabel);
                chartItem.setValueBinding("itemLabel", vb);
            } else {
                chartItem.setItemLabel(itemLabel);
            }
        }
        
        if (null != itemColor) {
            if (isValueReference(itemColor)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(itemColor);
                chartItem.setValueBinding("itemColor", vb);
            } else {
                chartItem.setItemColor(itemColor);
            }
        }
        
        if (null != itemValue) {
            if (isValueReference(itemValue)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(itemValue);
                chartItem.setValueBinding("itemValue", vb);
            } else {
                chartItem.setItemValue(itemValue);
            }
        }
    }

} // end of class SelectItemTag
