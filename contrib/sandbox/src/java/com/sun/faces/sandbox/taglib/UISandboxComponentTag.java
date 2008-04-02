/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ValueChangeEvent;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee, Adrian Mitev
 *
 */
public abstract class UISandboxComponentTag extends UIComponentTag {
    protected void setStringProperty(UIComponent component, String attributeName,
            String attributeValue) {
        if (attributeValue != null) {
            if (isValueReference(attributeValue)) {
                component.setValueBinding(attributeName,
                        Util.getValueBinding(attributeValue));
            }
            else {
                component.getAttributes().put(attributeName, attributeValue);
            }
        }
    }

    protected void setIntegerProperty(UIComponent component,
            String attributeName, String attributeValue) {
        if (attributeValue != null) {
            if (isValueReference(attributeValue)) {
                component.setValueBinding(attributeName,
                        Util.getValueBinding(attributeValue));
            }
            else {
                component.getAttributes().put(attributeName,
                        Integer.valueOf(attributeValue));
            }
        }
    }

    protected void setBooleanProperty(UIComponent component,
            String attributeName, String attributeValue) {
        if (attributeValue != null) {
            if (isValueReference(attributeValue)) {
                component.setValueBinding(attributeName,
                        Util.getValueBinding(attributeValue));
            }
            else {
                component.getAttributes().put(attributeName,
                        Boolean.valueOf(attributeValue));
            }
        }
    }

    protected MethodBinding createMethodBinding(UIComponent component,
            String attributeName, String attributeValue, Class params[]) {
        MethodBinding mb = null;
        if (attributeValue != null) {
            if (!isValueReference(attributeValue)) {
                throw new javax.faces.FacesException("Invalid MethodBinding expression:  " + attributeValue);
            }
            mb = FacesContext.getCurrentInstance().getApplication().createMethodBinding(attributeValue, params);
        }
        return mb;
    }
}