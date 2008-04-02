/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.sandbox.component.YuiTree;
import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee
 *
 */
public class YuiTreeTag  extends UIComponentTag {
    protected String model;
    
    // General Methods
    public String getRendererType() {
        return YuiTree.RENDERER_TYPE;
    }

    public String getComponentType() {
        return YuiTree.COMPONENT_TYPE;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (!(component instanceof YuiTree)) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.YuiTree.  Perhaps you're missing a tag?");
        }
        YuiTree tree = (YuiTree)component;
        if (model != null) {
            if (isValueReference(model)) {
                ValueBinding vb = Util.getValueBinding(model);
                tree.setValueBinding("model", vb);
            } else {
                throw new IllegalStateException("The value for 'model' must be a ValueBinding.");
            }
        }
    }

    public void setModel(String model) {
        this.model = model;
    }
}
