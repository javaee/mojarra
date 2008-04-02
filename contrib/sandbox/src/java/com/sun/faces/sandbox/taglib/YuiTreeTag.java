/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;

import com.sun.faces.sandbox.component.YuiTree;

/**
 * @author Jason Lee
 *
 */
public class YuiTreeTag  extends UISandboxComponentTag {
    protected String model;
    
    // General Methods
    public String getRendererType()  { return YuiTree.RENDERER_TYPE; }
    public String getComponentType() { return YuiTree.COMPONENT_TYPE; }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (!(component instanceof YuiTree)) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.YuiTree.  Perhaps you're missing a tag?");
        }
        YuiTree tree = (YuiTree)component;
        setValueBinding(tree, "model", model);
    }

    public void setModel(String model) { this.model = model; }
}
