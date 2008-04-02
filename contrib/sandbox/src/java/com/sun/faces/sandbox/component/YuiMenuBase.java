/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.el.ValueExpression;
import javax.faces.component.UIOutput;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public abstract class YuiMenuBase extends UIOutput {
    protected String width="200px";
    
    public String getWidth() {
        if (null != this.width) {
            return this.width;
        }
        ValueExpression _ve = getValueExpression("width");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
