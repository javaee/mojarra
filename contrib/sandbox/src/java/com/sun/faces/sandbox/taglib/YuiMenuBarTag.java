/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import com.sun.faces.sandbox.component.YuiMenuBar;

/**
 * @author Jason Lee
 *
 */
public class YuiMenuBarTag extends YuiMenuTagBase {
    @Override public String getComponentType() { return YuiMenuBar.COMPONENT_TYPE; }
    @Override public String getRendererType()  { return YuiMenuBar.RENDERER_TYPE; }
}