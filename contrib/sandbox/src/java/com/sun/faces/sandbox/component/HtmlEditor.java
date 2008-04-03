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