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
public class YuiTabView extends UIOutput {
    public static String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiTabView";
    public static String RENDERER_TYPE  = "com.sun.faces.sandbox.YuiTabViewRenderer";
    
    public static String TABSTYLE_BORDER = "border";
    public static String TABSTYLE_ROUND = "round";
    public static String TABSTYLE_MODULE = "module";
    
    protected String tabStyle = "border"; // Currently supports "border", "round" or "module"
    protected String orientation = "top"; // top, right, bottom, or left
    private String maxHeight = "dynamic"; // "dynamic", "auto", or "##px"
    
    public YuiTabView()          { setRendererType(RENDERER_TYPE); }
    public String getFamily()    { return COMPONENT_TYPE; }
    
    public String getOrientation() {
        return orientation;
    }
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    public String getTabStyle() {
        return tabStyle;
    }
    public void setTabStyle(String tabStyle) {
        this.tabStyle = tabStyle;
    }
    public String getMaxHeight() {
        return maxHeight;
    }
    public void setMaxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
    }
}
