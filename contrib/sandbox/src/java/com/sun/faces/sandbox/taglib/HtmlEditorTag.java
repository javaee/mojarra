/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import com.sun.faces.sandbox.component.HtmlEditor;

import javax.faces.component.UIComponent;

/**
 * @author Jason Lee
 *
 */
public class HtmlEditorTag extends UISandboxComponentTag {
    private String cols;
    private String config;
    private String rows;
    private String themeStyle;
    private String toolbarLocation = "top";
    private String value;

    public String getRendererType()  { return HtmlEditor.RENDERER_TYPE; }
    public String getComponentType() { return HtmlEditor.COMPONENT_TYPE; }
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (!(component instanceof HtmlEditor)) {
            throw new IllegalStateException("Component " + component.toString() +
                " not expected type.  Expected: com.sun.faces.sandbox.component.HtmlEditor.  Perhaps you're missing a tag?");
        }
        HtmlEditor editor = (HtmlEditor)component;
        setIntegerProperty(editor, "cols", cols);
        setStringProperty (editor, "config", config);
        setIntegerProperty(editor, "rows", rows);
        setStringProperty (editor, "themeStyle", themeStyle);
        setStringProperty (editor, "toolbarLocation", toolbarLocation);
        setStringProperty (editor, "value", value);
    }

    public void setCols(String cols)                { this.cols = cols; }
    public void setConfig(String config)            { this.config = config; }
    public void setRows(String rows)                { this.rows = rows; }
    public void setThemeStyle (String style)        { this.themeStyle = style; }
    public void setToolbarLocation(String location) { this.toolbarLocation = location; }
    public void setValue(String value)              { this.value = value; }
}