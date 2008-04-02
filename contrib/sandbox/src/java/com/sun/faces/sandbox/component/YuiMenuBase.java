/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.faces.component.UIOutput;

/**
 * YuiMenuBase is the base UIComponent class for all YUI menu
 * components.
 * @author Jason Lee
 *
 */
public abstract class YuiMenuBase extends UIOutput {
    /**
     * The width of the rendered menu.  Default width is (an arbitraty) 200 pixels.
     */
    protected String width="200px";
    
    public String getWidth() { return ComponentHelper.getValue(this, "width", width); }

    public void setWidth(String width) { this.width = width; }
}
