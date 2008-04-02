/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;

import com.sun.faces.sandbox.component.YuiTab;

/**
 * @author Jason Lee
 *
 */
public class YuiTabTag extends UISandboxComponentTag {
    protected String active;
    protected String disabled;
    protected String label;
    
    public String getComponentType() { return YuiTab.COMPONENT_TYPE; }
    public String getRendererType()  { return YuiTab.RENDERER_TYPE; }
    
    public void setActive(String active) {
        this.active = active;
    }
    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (!(component instanceof YuiTab)) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.YuiTab.  Perhaps you're missing a tag?");
        }
        YuiTab Tab = (YuiTab)component;
        setBooleanProperty(Tab, "active", active);
        setBooleanProperty(Tab, "disabled", disabled);
        setStringProperty(Tab, "label", label);
    }
}
