package com.sun.faces.sandbox.component;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

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
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.AccessibleHtmlColumn";


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
        ValueExpression _ve = getValueExpression("footerClass");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
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
        ValueExpression _ve = getValueExpression("headerClass");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
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
            ValueExpression _ve = getValueExpression("rowHeader");
            return (_ve != null
                 && (Boolean) _ve.getValue(getFacesContext().getELContext()));
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
