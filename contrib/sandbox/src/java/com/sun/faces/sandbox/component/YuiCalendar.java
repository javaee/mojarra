/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.sandbox.component;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
/**
 * @author Jason Lee
 */
public class YuiCalendar extends UIInput {
    
        /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiCalendar";

    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.YuiCalendarRenderer";

    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.FacesMessage} to be created if
     * a conversion error occurs, and neither the page author nor
     * the {@link ConverterException} provides a message.</p>
     */
    public static final String CONVERSION_MESSAGE_ID = "com.sun.faces.sandbox.YuiCalendar.CONVERSION";


    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.FacesMessage} to be created if
     * a required check fails.</p>
     */
    public static final String REQUIRED_MESSAGE_ID = "com.sun.faces.sandbox.YuiCalendar.REQUIRED";

    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.FacesMessage} to be created if
     * a model update error occurs, and the thrown exception has
     * no message.</p>
     */
    public static final String UPDATE_MESSAGE_ID = "com.sun.faces.sandbox.YuiCalendar.UPDATE";    
    
    public static final String INVALID_MESSAGE_ID = "com.sun.faces.sandbox.YuiCalendar.INVALID";       
    

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
    
    public YuiCalendar() { 
        super();
        setRendererType(RENDERER_TYPE); 
    }
    
    public String getFamily() { 
        return COMPONENT_TYPE; 
    }
    
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