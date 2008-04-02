/**
 * 
 */
package com.sun.faces.sandbox.component;


/**
 * The UIComponent class for the YUI menu bar component.  For an 
 * example of this component, see the Yahoo! UI example <a target="_blank"
 * href="http://developer.yahoo.com/yui/examples/menu/topnavfrommarkup.html">page</a>.
 * @author Jason Lee
 */
public class YuiMenuBar  extends YuiMenuBase {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiMenuBar";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.YuiMenuBarRenderer";
    
    public String getFamily() {
        return COMPONENT_TYPE;
    }
    
    public YuiMenuBar() {
        setRendererType(RENDERER_TYPE);
    }
}
