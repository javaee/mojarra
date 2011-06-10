/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * <p>Represents a column that will be rendered
 * in an HTML <code>table</code> element.</p>
 */
public class AccessibleHtmlColumn extends javax.faces.component.UIColumn {



    public AccessibleHtmlColumn() {
        super();
    }


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE =
         "com.sun.faces.sandbox.AccessibleHtmlColumn";


    private String footerClass;

    /**
     * <p>Return the value of the <code>footerClass</code> property.</p>
     * <p>Contents: Space-separated list of CSS style class(es) that will be
     * applied to any footer generated for this column.
     */
    public String getFooterClass() {
        if (null != this.footerClass) {
            return this.footerClass;
        }
        ValueBinding _vb = getValueBinding("footerClass");
        if (_vb != null) {
            return (String) _vb.getValue(getFacesContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>footerClass</code> property.</p>
     */
    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }


    private String headerClass;

    /**
     * <p>Return the value of the <code>headerClass</code> property.</p>
     * <p>Contents: Space-separated list of CSS style class(es) that will be
     * applied to any header generated for this column.
     */
    public String getHeaderClass() {
        if (null != this.headerClass) {
            return this.headerClass;
        }
        ValueBinding _vb = getValueBinding("headerClass");
        if (_vb != null) {
            return (String) _vb.getValue(getFacesContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>headerClass</code> property.</p>
     */
    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }


    private boolean rowHeader;
    private boolean rowHeader_set;

    /**
     * <p>Return the value of the <code>rowHeader</code> property.</p>
     * <p>Contents:  Flag indicating that this column is a row header column and
     * therefore cells in this column should be rendered with "th"
     * instead of "td" and must have the 'scope="row"' attribute.
     */
    public boolean getRowHeader() {
        if (rowHeader_set) {
            return rowHeader;
        } else {
            ValueBinding _vb = getValueBinding("rowHeader");
            return (_vb != null
                 && ((Boolean) _vb.getValue(getFacesContext())).booleanValue());
        }
    }

    public void setRowHeader(boolean rowHeader) {
        rowHeader_set = true;
        this.rowHeader = rowHeader;
    }


    private Object[] _values;

    public Object saveState(FacesContext _context) {
        if (_values == null) {
            _values = new Object[3];
        }
        _values[0] = super.saveState(_context);
        _values[1] = footerClass;
        _values[2] = headerClass;
        return _values;
}


    public void restoreState(FacesContext _context, Object _state) {
        _values = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        this.footerClass = (String) _values[1];
        this.headerClass = (String) _values[2];
    }


}
