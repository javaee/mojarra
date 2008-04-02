package com.sun.faces.sandbox.component;

import javax.el.ValueExpression;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
/**
 * @author Jason Lee
 */
public class YuiCalendar extends UIInput {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiCalendar";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.YuiCalendarRenderer";
    private Object[] _state = null;
    protected Boolean hideBlankWeeks = false;
    protected String language = null;
    protected Boolean multiSelect = false;
    protected String onchange = null;
    protected Boolean showWeekdays = true;
    protected Boolean showWeekFooter = false;
    protected Boolean showWeekHeader = false;
    protected Integer startWeekday = 0; // Sunday
    
    public YuiCalendar() {
        setRendererType(RENDERER_TYPE);
    }

    public String getFamily() {
        return COMPONENT_TYPE;
    }
    
    public Boolean getHideBlankWeeks() {
        if (null != this.hideBlankWeeks) {
            return this.hideBlankWeeks;
        }
        ValueExpression _ve = getValueExpression("hideBlankWeeks");
        if (_ve != null) {
            return (java.lang.Boolean) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }
    
    public Boolean getMultiSelect() {
        if (null != this.multiSelect) {
            return this.multiSelect;
        }
        ValueExpression _ve = getValueExpression("multiSelect");
        if (_ve != null) {
            return (java.lang.Boolean) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public String getOnchange() {
        if (null != this.onchange) {
            return this.onchange;
        }
        ValueExpression _ve = getValueExpression("onchange");
        if (_ve != null) {
            return (java.lang.String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public Boolean getShowWeekdays() {
        if (null != this.showWeekdays) {
            return this.showWeekdays;
        }
        ValueExpression _ve = getValueExpression("showWeekdays");
        if (_ve != null) {
            return (java.lang.Boolean) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public Boolean getShowWeekFooter() {
        if (null != this.showWeekFooter) {
            return this.showWeekFooter;
        }
        ValueExpression _ve = getValueExpression("showWeekFooter");
        if (_ve != null) {
            return (java.lang.Boolean) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public Boolean getShowWeekHeader() {
        if (null != this.showWeekHeader) {
            return this.showWeekHeader;
        }
        ValueExpression _ve = getValueExpression("showWeekHeader");
        if (_ve != null) {
            return (java.lang.Boolean) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public Integer getStartWeekday() {
        if (null != this.startWeekday) {
            return this.startWeekday;
        }
        ValueExpression _ve = getValueExpression("startWeekday");
        if (_ve != null) {
            return (java.lang.Integer) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void restoreState(FacesContext _context, Object _state) {
        this._state = (Object[]) _state;
        super.restoreState(_context, this._state[0]);
        multiSelect = (java.lang.Boolean) this._state[1];
        showWeekdays = (java.lang.Boolean) this._state[2];
        startWeekday = (java.lang.Integer) this._state[3];
        showWeekHeader = (java.lang.Boolean) this._state[4];
        showWeekFooter = (java.lang.Boolean) this._state[5];
        hideBlankWeeks = (java.lang.Boolean) this._state[6];
        onchange = (java.lang.String) this._state[7];
        language = (java.lang.String) this._state[8];
    }

    public Object saveState(FacesContext _context) {
        if (_state == null) {
            _state = new Object[9];
        }
        _state[0] = super.saveState(_context);
        _state[1] = multiSelect;
        _state[2] = showWeekdays;
        _state[3] = startWeekday;
        _state[4] = showWeekHeader;
        _state[5] = showWeekFooter;
        _state[6] = hideBlankWeeks;
        _state[7] = onchange;
        _state[8] = language;
        
        return _state;
    }

    public void setHideBlankWeeks(Boolean hideBlankWeeks) {
        this.hideBlankWeeks = hideBlankWeeks;
    }

    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public void setOnchange(String onChange) {
        this.onchange = onChange;
    }

    public void setShowWeekdays(Boolean showWeekdays) {
        this.showWeekdays = showWeekdays;
    }

    public void setShowWeekFooter(Boolean showWeekFooter) {
        this.showWeekFooter = showWeekFooter;
    }

    public void setShowWeekHeader(Boolean showWeekHeader) {
        this.showWeekHeader = showWeekHeader;
    }

    public void setStartWeekday(Integer startWeekday) {
        this.startWeekday = startWeekday;
    }
}