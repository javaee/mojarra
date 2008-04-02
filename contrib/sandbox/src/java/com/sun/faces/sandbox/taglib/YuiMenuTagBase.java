/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.sandbox.component.YuiMenuBase;
import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee
 *
 */
public abstract class YuiMenuTagBase extends UIComponentTag {
    protected String value;
    protected String width;
    
    @Override
    public abstract String getComponentType();
    @Override
    public abstract String getRendererType();

    public void setValue(String value) {
        this.value = value;
    }

    public void setWidth(String width) {
        this.width = width;
    }
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        YuiMenuBase menu = null;
        try {
            menu = (YuiMenuBase) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + 
                    " not expected type.  Expected: com.sun.faces.sandbox.component.YuiMenu.  Perhaps you're missing a tag?");
        }

        if (value != null) {
            if (isValueReference(value)) {
                ValueBinding vb = Util.getValueBinding(value);
                menu.setValueBinding("value", vb);
            } else {
                menu.setValue(value);
            }
        }
        if (width != null) {
            if (isValueReference(width)) {
                ValueBinding vb = Util.getValueBinding(width);
                menu.setValueBinding("width", vb);
            } else {
                menu.setWidth(width);
            }
        }
    }
}