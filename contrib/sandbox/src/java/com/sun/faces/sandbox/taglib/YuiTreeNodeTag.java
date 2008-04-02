/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;

import com.sun.faces.sandbox.component.YuiTreeNode;

/**
 * @author Jason Lee
 *
 */
public class YuiTreeNodeTag extends UISandboxComponentTag{
    // General Methods
    public String getRendererType()  { return YuiTreeNode.RENDERER_TYPE; }
    public String getComponentType() { return YuiTreeNode.COMPONENT_TYPE; }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (!(component instanceof YuiTreeNode)) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.YuiTreeNode.  Perhaps you're missing a tag?");
        }
    }
}
