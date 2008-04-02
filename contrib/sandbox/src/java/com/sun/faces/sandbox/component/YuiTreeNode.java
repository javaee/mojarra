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
public class YuiTreeNode extends UIOutput {
    public static String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiTreeNode";
    public static String RENDERER_TYPE  = "com.sun.faces.sandbox.YuiTreeNodeRenderer";
    
    public YuiTreeNode()      { setRendererType(RENDERER_TYPE); }
    public String getFamily() { return COMPONENT_TYPE; }
}
