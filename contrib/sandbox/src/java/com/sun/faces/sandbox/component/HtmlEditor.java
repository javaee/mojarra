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

    private String rows = "10";
    private String cols = "80";
    private String value;

    public HtmlEditor()       { setRendererType(RENDERER_TYPE); }
    public String getFamily() { return COMPONENT_TYPE; }
    
    public String getCols() { return ComponentHelper.getValue(this, "cols", cols); }
    public String getRows() { return ComponentHelper.getValue(this, "rows", rows); }
    public String getValue() { return ComponentHelper.getValue(this, "value", value); }

    public void setCols(String cols)   { this.cols = cols; }
    public void setRows(String rows)   { this.rows = rows; }
    public void setValue(String value) {this.value = value; }

    public void restoreState(FacesContext _context, Object _state) {
        this._state = (Object[]) _state;
        super.restoreState(_context, this._state[0]);
        cols  = (String) this._state[1];
        rows  = (String) this._state[2];
        value = (String) this._state[3];
    }

    public Object saveState(FacesContext _context) {
        if (_state == null) {
            _state = new Object[4];
        }
        _state[0] = super.saveState(_context);
        _state[1] = cols;
        _state[2] = rows;
        _state[3] = value;
        
        return _state;
    }
}