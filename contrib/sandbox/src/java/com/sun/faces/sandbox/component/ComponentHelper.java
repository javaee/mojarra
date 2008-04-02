/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee
 *
 */
public class ComponentHelper {
    public static <V> V getValue(UIComponent comp, String attributeName, V defaultValue) {
        ValueBinding vb = comp.getValueBinding(attributeName);
        return (vb != null) ? (V) vb.getValue(FacesContext.getCurrentInstance()) : defaultValue;
    }

    protected void setStringProperty(UIComponent component, 
            String attributeName,
            String attributeValue) {
        if (attributeValue != null) {
            if (UIComponentTag.isValueReference(attributeValue)) {
                component.setValueBinding(attributeName,
                        Util.getValueBinding(attributeValue));
            }
            else {
                component.getAttributes().put(attributeName, attributeValue);
            }
        }
    }
}