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
    private String rows;
    private String value;

    public String getRendererType()  { return HtmlEditor.RENDERER_TYPE; }
    public String getComponentType() { return HtmlEditor.COMPONENT_TYPE; }
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (!(component instanceof HtmlEditor)) {
            throw new IllegalStateException("Component " + component.toString() +
                " not expected type.  Expected: com.foo.Foo.  Perhaps you're missing a tag?");
        }
        HtmlEditor editor = (HtmlEditor)component;
        setIntegerProperty(editor, "cols", cols);
        setIntegerProperty(editor, "rows", rows);
        setStringProperty (editor, "value", value);
    }

    public void setCols(String cols)  { this.cols = cols; }
    public void setRows(String rows)  { this.rows = rows; }
    public void setValue(String value) { this.value = value; }
}