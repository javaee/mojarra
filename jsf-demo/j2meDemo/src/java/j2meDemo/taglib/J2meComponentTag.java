package j2meDemo.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import j2meDemo.util.Util;

/**
 * Top level tag handler class for other tags.
 */
public abstract class J2meComponentTag extends UIComponentTag {   
    private String value;
    private String action;

    public void setValue(String newValue) { 
        value = newValue; 
    }

    public void setAction(String newValue) { 
        action = newValue; 
    }

    public void setProperties(UIComponent component) { 
        super.setProperties(component); 
        
        if (value != null) {
            if (isValueReference(value)) {
                ValueBinding vb = Util.getValueBinding(value);
                component.setValueBinding("value", vb);
            } else {
                component.getAttributes().put("value", value);
            }
        }

        if (action != null) {
            if (isValueReference(action)) {
                MethodBinding mb = FacesContext.getCurrentInstance().
                    getApplication().createMethodBinding(action, null);
                component.getAttributes().put("action", mb);
            } else {
                MethodBinding mb = Util.createConstantMethodBinding(action);
                component.getAttributes().put("action", mb);
            }
        }
    } 

    public void release() {
        value = null;
        action = null;
    }
}
