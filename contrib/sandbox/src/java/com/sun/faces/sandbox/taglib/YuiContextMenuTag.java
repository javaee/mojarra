/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import com.sun.faces.sandbox.component.YuiContextMenu;

/**
 * The <code>UIComponentTag</code> class for the YUI context menu.
 * @author Jason Lee
 *
 */
public class YuiContextMenuTag extends YuiMenuTagBase {
    @Override
    public String getComponentType() {
        return YuiContextMenu.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return YuiContextMenu.RENDERER_TYPE;
    }
}