/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.faces.component.UIOutput;

import com.sun.faces.sandbox.model.TreeNode;

/**
 * @author Jason Lee
 *
 */
public class YuiTree extends UIOutput {
    public static String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiTree";
    public static String RENDERER_TYPE  = "com.sun.faces.sandbox.YuiTreeRenderer";
    TreeNode model;
    
    public YuiTree()          { setRendererType(RENDERER_TYPE); }
    public String getFamily() { return COMPONENT_TYPE; }

    public TreeNode getModel() { return ComponentHelper.getValue(this, "model", model); }

    public void setModel(TreeNode model) { this.model = model; }
}
