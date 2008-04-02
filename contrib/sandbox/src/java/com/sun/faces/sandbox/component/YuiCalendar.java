package com.sun.faces.sandbox.component;

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
    protected String onChange = null;
    protected Boolean showWeekdays = true;
    protected Boolean showWeekFooter = false;
    protected Boolean showWeekHeader = false;
    protected Integer startWeekday = 0; // Sunday
    protected Boolean showMenus = false;
    protected String minDate = null;
    protected String maxDate = null;
    
    public YuiCalendar() { setRendererType(RENDERER_TYPE); }
    public String getFamily() { return COMPONENT_TYPE; }
    
    public Boolean getHideBlankWeeks() { return ComponentHelper.getValue(this, "hideBlankWeeks", hideBlankWeeks); }
    public Boolean getMultiSelect()    { return ComponentHelper.getValue(this, "multiSelect", multiSelect); }
    public String getOnChange()        { return ComponentHelper.getValue(this, "onChange", onChange); }
    public Boolean getShowWeekdays()   { return ComponentHelper.getValue(this, "showWeekdays", showWeekdays); }
    public Boolean getShowWeekFooter() { return ComponentHelper.getValue(this, "showWeekFooter", showWeekFooter); }
    public Boolean getShowWeekHeader() { return ComponentHelper.getValue(this, "showWeekHeader", showWeekHeader); }
    public Integer getStartWeekday()   { return ComponentHelper.getValue(this, "startWeekday", startWeekday); }
    public String getMinDate()         { return ComponentHelper.getValue(this, "minDate", minDate); }
    public String getMaxDate()         { return ComponentHelper.getValue(this, "maxDate", maxDate); }   
    public Boolean getShowMenus()    { return ComponentHelper.getValue(this, "showMenus", showMenus); }

    public void setHideBlankWeeks(Boolean hideBlankWeeks) { this.hideBlankWeeks = hideBlankWeeks; }
    public void setMultiSelect(Boolean multiSelect)       { this.multiSelect = multiSelect; }
    public void setOnChange(String onChange)              { this.onChange = onChange; }
    public void setShowWeekdays(Boolean showWeekdays)     { this.showWeekdays = showWeekdays; }
    public void setShowWeekFooter(Boolean showWeekFooter) { this.showWeekFooter = showWeekFooter; }
    public void setShowWeekHeader(Boolean showWeekHeader) { this.showWeekHeader = showWeekHeader; }
    public void setStartWeekday(Integer startWeekday)     { this.startWeekday = startWeekday; }
    public void setMinDate(String minDate)                { this.minDate = minDate; }
    public void setMaxDate(String maxDate)                { this.maxDate = maxDate; }
    public void setShowMenus(Boolean showMenus)       { this.showMenus = showMenus; }

    public void restoreState(FacesContext _context, Object _state) {
        this._state = (Object[]) _state;
        super.restoreState(_context, this._state[0]);
        multiSelect = (java.lang.Boolean) this._state[1];
        showWeekdays = (java.lang.Boolean) this._state[2];
        startWeekday = (java.lang.Integer) this._state[3];
        showWeekHeader = (java.lang.Boolean) this._state[4];
        showWeekFooter = (java.lang.Boolean) this._state[5];
        hideBlankWeeks = (java.lang.Boolean) this._state[6];
        onChange = (java.lang.String) this._state[7];
        language = (java.lang.String) this._state[8];
        minDate = (java.lang.String) this._state[9];
        maxDate = (java.lang.String) this._state[10];       
        showMenus = (java.lang.Boolean) this._state[11];
    }

    public Object saveState(FacesContext _context) {
        if (_state == null) {
            _state = new Object[12];
        }
        _state[0] = super.saveState(_context);
        _state[1] = multiSelect;
        _state[2] = showWeekdays;
        _state[3] = startWeekday;
        _state[4] = showWeekHeader;
        _state[5] = showWeekFooter;
        _state[6] = hideBlankWeeks;
        _state[7] = onChange;
        _state[8] = language;
        _state[9] = minDate;
        _state[10] = maxDate;     
        _state[11] = showMenus;

        return _state;
    }
}