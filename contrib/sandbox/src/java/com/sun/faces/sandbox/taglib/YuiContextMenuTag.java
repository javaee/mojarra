/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import com.sun.faces.sandbox.component.YuiContextMenu;
import com.sun.faces.sandbox.util.Util;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class YuiContextMenuTag extends YuiMenuTagBase {
    protected String trigger;

    @Override
    public String getComponentType() {
        return YuiContextMenu.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return YuiContextMenu.RENDERER_TYPE;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        YuiContextMenu menu = null;
        try {
            menu = (YuiContextMenu) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.steeplesoft.jsf.components.yui.component.HtmlYuiContextMenu.  Perhaps you're missing a tag?");
        }
        if (trigger != null) {
            if (isValueReference(trigger)) {
                ValueBinding vb = Util.getValueBinding(trigger);
                menu.setValueBinding("trigger", vb);
            } else {
                menu.setTrigger(trigger);
            }
        }
    }
}