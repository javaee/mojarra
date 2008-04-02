/**
 * 
 */
package com.sun.faces.sandbox.component;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class YuiMenu extends YuiMenuBase {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiMenu";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.YuiMenuRenderer";

    public YuiMenu() {
        setRendererType(RENDERER_TYPE);
    }
}
