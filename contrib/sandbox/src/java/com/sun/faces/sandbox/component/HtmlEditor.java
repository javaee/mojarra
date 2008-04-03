/**
 * 
 */
package com.sun.faces.sandbox.component;

import com.sun.faces.sandbox.model.FileHolderImpl;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;


/**
 * @author Jason Lee
 *
 */
public class HtmlEditor extends UIInput {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.HtmlEditor";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.HtmlEditorRenderer";
    private Object[] _state = null;

    private String config;
    private String cols = "80";
    private String rows = "10";
    private String themeStyle = "normal";
    private String toolbarLocation = "top";
    private String value;

    public HtmlEditor()       { setRendererType(RENDERER_TYPE); }
    public String getFamily() { return COMPONENT_TYPE; }
    
    public String getCols()            { return ComponentHelper.getValue(this, "cols", cols); }
    public String getConfig()          { return ComponentHelper.getValue(this, "config", config); }
    public String getRows()            { return ComponentHelper.getValue(this, "rows", rows); }
    public String getThemeStyle()      { return ComponentHelper.getValue(this, "themeStyle", themeStyle); }
    public String getToolbarLocation() { return ComponentHelper.getValue(this, "toolbarLocation", toolbarLocation); }
    public String getValue()           { return ComponentHelper.getValue(this, "value", value); }

    public void setCols(String cols)                       { this.cols = cols; }
    public void setConfig(String config)                   { this.config = config; }
    public void setRows(String rows)                       { this.rows = rows; }
    public void setThemeStyle(String style)                { this.themeStyle = style; }
    public void setToolbarLocation(String toolbarLocation) { this.toolbarLocation = toolbarLocation; }
    public void setValue(String value)                     { this.value = value; }

    public void restoreState(FacesContext _context, Object _state) {
        this._state = (Object[]) _state;
        super.restoreState(_context, this._state[0]);
        cols  = (String) this._state[1];
        config = (String) this._state[2];
        rows  = (String) this._state[3];
        themeStyle = (String) this._state[4];
        toolbarLocation = (String) this._state[5];
        value = (String) this._state[6];
    }

    public Object saveState(FacesContext _context) {
        if (_state == null) {
            _state = new Object[7];
        }
        _state[0] = super.saveState(_context);
        _state[1] = cols;
        _state[2] = config;
        _state[3] = rows;
        _state[4] = themeStyle;
        _state[5] = toolbarLocation;
        _state[6] = value;
        
        return _state;
    }
}