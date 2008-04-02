/**
 * 
 */
package com.sun.faces.sandbox.component;


/**
 * The UIComponent class for the YUI context menu component.  For an 
 * example of this component, see the Yahoo! UI example <a target="_blank"
 * href="http://developer.yahoo.com/yui/examples/menu/contextmenu.html">page</a>.
 * @author Jason Lee
 *
 */
public class YuiContextMenu  extends YuiMenuBase {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiContextMenu";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.YuiContextMenuRenderer";
    
    public String getFamily() {
        return COMPONENT_TYPE;
    }
    
    public YuiContextMenu() {
        setRendererType(RENDERER_TYPE);
    }
}
