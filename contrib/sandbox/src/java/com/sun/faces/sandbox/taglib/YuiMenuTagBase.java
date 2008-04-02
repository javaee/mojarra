/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;

import com.sun.faces.sandbox.component.YuiMenuBase;

/**
 * <b><code>YuiMenuTagBase</code></b> is the base <code>UIComponentTag</code> for
 * all YUI menu components.
 * @author Jason Lee
 *
 */
public abstract class YuiMenuTagBase extends UISandboxComponentTag {
    /**
     * <code>value</code> is an expression which evaulates to the Menu items.
     * @see com.sun.faces.sandbox.model.Menu
     */
    protected String value;
    /**
     * Either a string literal or expression which represents the width of the rendered
     * menu.
     */
    protected String width;
    
    @Override public abstract String getComponentType();
    @Override public abstract String getRendererType();

    public void setValue(String value) { this.value = value; }
    public void setWidth(String width) { this.width = width; }
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        YuiMenuBase menu = null;
        try {
            menu = (YuiMenuBase) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + 
                    " not expected type.  Expected: com.sun.faces.sandbox.component.YuiMenu.  Perhaps you're missing a tag?");
        }

        setStringProperty(menu, "value", value);
        setStringProperty(menu, "width", width);
    }
}