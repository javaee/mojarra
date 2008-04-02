/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.faces.component.UIOutput;
import javax.faces.el.ValueBinding;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author lee
 *
 */
public class YuiTree extends UIOutput {
    public static String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiTree";
    public static String RENDERER_TYPE  = "com.sun.faces.sandbox.YuiTreeRenderer";
    
    protected DefaultMutableTreeNode model;
    
    public YuiTree() {
        setRendererType(RENDERER_TYPE);
    }
    
    public String getFamily() {
        return COMPONENT_TYPE;
    }

    public DefaultMutableTreeNode getModel() {
        if (null != this.model) {
            return this.model;
        }
        ValueBinding _vb = getValueBinding("model");
        if (_vb != null) {
            return (DefaultMutableTreeNode) _vb.getValue(getFacesContext());
        } else {
            return null;
        }
    }

    public void setModel(DefaultMutableTreeNode model) {
        this.model = model;
    }
}
