/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.faces.component.UIOutput;

/**
 * The UIComponent class for the YUI context menu component.  For an 
 * example of this component, see the Yahoo! UI example <a target="_blank"
 * href="http://developer.yahoo.com/yui/examples/menu/example07.html">page</a>.
 * @author Jason Lee
 */
public class YuiMenuItem extends UIOutput {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiMenuItem";
//    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.YuiMenuRenderer";
    
    protected String url;

    public YuiMenuItem()           { /* This will be rendered by the enclosing YuiMenuBase */ }
    public String getFamily()      { return COMPONENT_TYPE; }

    public String getUrl()         { return ComponentHelper.getValue(this, "url", url); }
    public void setUrl(String url) { this.url = url; }

}
