/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import com.sun.faces.sandbox.component.YuiMenu;

/**
 * @author Jason Lee
 *
 */
public class YuiMenuTag extends YuiMenuTagBase {
    @Override
    public String getComponentType() {
        return YuiMenu.COMPONENT_TYPE;
    }
    @Override
    public String getRendererType() {
        return YuiMenu.RENDERER_TYPE;
    }

}