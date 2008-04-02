/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;

import com.sun.faces.sandbox.component.YuiMenuBase;
import com.sun.faces.sandbox.component.YuiMenuItem;

/**
 * @author Jason Lee
 *
 */
public class YuiMenuItemTag extends UISandboxComponentTag {
    /**
     * <code>value</code> is an expression which evaulates to the Menu items.
     * @see com.sun.faces.sandbox.model.Menu
     */
    protected String value;
    /**
     * Either a string literal or expression which represents the width of the rendered
     * menu.
     */
    protected String url;
    
    @Override public String getComponentType(){ return YuiMenuItem.COMPONENT_TYPE; }
    @Override public String getRendererType() { return null; }  // Will be rendered buy the YuiMenu component

    public void setValue(String value) { this.value = value; }
    public void setUrl(String width) { this.url = width; }
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        YuiMenuItem menuItem = null;
        try {
            menuItem = (YuiMenuItem) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + 
                    " not expected type.  Expected: com.sun.faces.sandbox.component.YuiMenuItem.  Perhaps you're missing a tag?");
        }

        setStringProperty(menuItem, "value", value);
        setStringProperty(menuItem, "url", url);
    }
}
