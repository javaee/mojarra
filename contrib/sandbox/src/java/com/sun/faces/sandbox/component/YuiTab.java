/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.faces.component.UIOutput;

/**
 * @author Jason Lee
 *
 */
public class YuiTab extends UIOutput {
    public static String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiTab";
    public static String RENDERER_TYPE  = "com.sun.faces.sandbox.YuiTabRenderer";

    
    private Boolean active;
    private Boolean disabled = Boolean.FALSE;
    private String label;
    
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public Boolean getDisabled() {
        return disabled;
    }
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public YuiTab()              { setRendererType(RENDERER_TYPE); }
    public String getFamily()    { return COMPONENT_TYPE; }
}