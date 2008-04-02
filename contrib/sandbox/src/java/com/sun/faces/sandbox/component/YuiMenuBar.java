/**
 * 
 */
package com.sun.faces.sandbox.component;


/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class YuiMenuBar  extends YuiMenuBase {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiMenuBar";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.YuiMenuBarRenderer";
    
    public String getFamily() {
        return "YuiMenuBar";
    }
    
    public YuiMenuBar() {
        setRendererType(RENDERER_TYPE);
    }
}
