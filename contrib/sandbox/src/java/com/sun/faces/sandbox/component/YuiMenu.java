/**
 * 
 */
package com.sun.faces.sandbox.component;

/**
 * The UIComponent class for the YUI context menu component.  For an 
 * example of this component, see the Yahoo! UI example <a target="_blank"
 * href="http://developer.yahoo.com/yui/examples/menu/example07.html">page</a>.
 * @author Jason Lee
 */
public class YuiMenu extends YuiMenuBase {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiMenu";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.YuiMenuRenderer";

    public YuiMenu() {
        setRendererType(RENDERER_TYPE);
    }
}
