/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.sandbox.component.UISelectItems;
import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee
 * 
 */
public class SelectItemsTag extends UIComponentTag {
    protected String value;
    protected String itemLabel;
    protected String itemValue;
    protected String itemVar;

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getItemVar() {
        return itemVar;
    }

    public void setItemVar(String itemVar) {
        this.itemVar = itemVar;
    }

    public SelectItemsTag() {
        super();
    }

    public String getComponentType() {
        return UISelectItems.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        UISelectItems si = (UISelectItems)component;  
        if (value!= null) {  
            if (isValueReference(value)) {  
                ValueBinding vb = Util.getValueBinding(value);  
                si.setValueBinding("value", vb);  
            } else {  
                throw new IllegalStateException("The value for 'value' must be a ValueBinding.");  
            }  
        }  
        if (itemLabel!= null) {  
            if (isValueReference(itemLabel)) {  
                ValueBinding vb = Util.getValueBinding(itemLabel);  
                si.setValueBinding("itemLabel", vb);  
            } else {  
                throw new IllegalStateException("The value for 'value' must be a ValueBinding.");  
            }  
        }  
        if (itemValue!= null) {  
            if (isValueReference(itemValue)) {  
                ValueBinding vb = Util.getValueBinding(itemValue);  
                si.setValueBinding("itemValue", vb);  
            } else {  
                throw new IllegalStateException("The value for 'value' must be a ValueBinding.");  
            }  
        }  
        if (itemVar!= null) {  
            if (isValueReference(itemVar)) {  
                ValueBinding vb = Util.getValueBinding(itemVar);  
                si.setValueBinding("itemVar", vb);  
            } else {  
                si.setItemVar(itemVar);
            }  
        }  
    }
}