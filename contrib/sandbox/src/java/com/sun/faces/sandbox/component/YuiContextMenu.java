/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.el.ValueExpression;

/**
 * @author Jason Lee
 *
 */
public class YuiContextMenu  extends YuiMenuBase {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiContextMenu";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.YuiContextMenuRenderer";
    protected String trigger;
    
    public String getFamily() {
        return COMPONENT_TYPE;
    }
    
    public String getTrigger() {
        if (null != this.trigger) {
            return this.trigger;
        }
        ValueExpression _ve = getValueExpression("trigger");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public YuiContextMenu() {
        setRendererType(RENDERER_TYPE);
    }
}
