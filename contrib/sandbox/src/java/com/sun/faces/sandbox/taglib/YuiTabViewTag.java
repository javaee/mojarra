/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;

import com.sun.faces.sandbox.component.YuiTabView;

/**
 * @author Jason Lee
 *
 */
public class YuiTabViewTag extends UISandboxComponentTag {
    protected String orientation;
    protected String tabStyle;

    public String getComponentType() { return YuiTabView.COMPONENT_TYPE; }
    public String getRendererType()  { return YuiTabView.RENDERER_TYPE; }
    
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    public void setTabStyle(String tabStyle) {
        this.tabStyle = tabStyle;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (!(component instanceof YuiTabView)) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.YuiTabView.  Perhaps you're missing a tag?");
        }
        YuiTabView tabView = (YuiTabView)component;
        setStringProperty(tabView, "tabStyle", tabStyle);
        setStringProperty(tabView, "orientation", orientation);
    }
}
