/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import com.sun.faces.sandbox.component.YuiMenuBar;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class YuiMenuBarTag extends YuiMenuTagBase {
    @Override
    public String getComponentType() {
        return YuiMenuBar.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return YuiMenuBar.RENDERER_TYPE;
    }
}