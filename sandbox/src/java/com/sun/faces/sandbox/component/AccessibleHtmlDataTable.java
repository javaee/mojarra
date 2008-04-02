package com.sun.faces.sandbox.component;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * <p>Represents a set of repeating data (segregated into
 * columns by child UIColumn components) that will
 * be rendered in an HTML <code>table</code> element.</p>
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Table</code>".
 * This value can be changed by calling the <code>setRendererType()</code> method.</p>
 */
public class AccessibleHtmlDataTable extends javax.faces.component.UIData {



    public AccessibleHtmlDataTable() {
        super();
        setRendererType("com.sun.faces.sandbox.AccessibleTableRenderer");
    }


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.AccessibleHtmlDataTable";


    private String bgcolor;

    /**
     * <p>Return the value of the <code>bgcolor</code> property.</p>
     * <p>Contents: Name or code of the background color for this table.
     */
    public String getBgcolor() {
        if (null != this.bgcolor) {
            return this.bgcolor;
        }
        ValueExpression _ve = getValueExpression("bgcolor");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>bgcolor</code> property.</p>
     */
    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }


    private int border = Integer.MIN_VALUE;
    private boolean border_set = false;

    /**
     * <p>Return the value of the <code>border</code> property.</p>
     * <p>Contents: Width (in pixels) of the border to be drawn
     * around this table.
     */
    public int getBorder() {
        if (this.border_set) {
            return this.border;
        }
        ValueExpression _ve = getValueExpression("border");
        if (_ve != null) {
            Object _result = _ve.getValue(getFacesContext().getELContext());
            if (_result == null) {
                return Integer.MIN_VALUE;
            } else {
                return ((Integer) _result).intValue();
            }
        } else {
            return this.border;
        }
    }

    /**
     * <p>Set the value of the <code>border</code> property.</p>
     */
    public void setBorder(int border) {
        this.border = border;
        this.border_set = true;
    }


    private String bodyrows;
    private boolean bodyrows_set = false;

    /**
     * <p>Return the value of the <code>bodyrows</code> property.</p>
     * <p>Contents: Comma separated list of row indices for which a new
     * "tbody" element should be started (and any
     * previously opened one should be ended).</p>
     */
    public String getBodyrows() {
         if (this.bodyrows_set) {
            return this.bodyrows;
        }
        ValueExpression _ve = getValueExpression("bodyrows");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Sets the value of the <code>bodyrows</code> property.</p>     
     */
    public void setBodyrows(String bodyrows) {
        this.bodyrows = bodyrows;
        this.bodyrows_set = true;
    }



    private String captionClass;

    /**
     * <p>Return the value of the <code>captionClass</code> property.</p>
     * <p>Contents: Space-separated list of CSS style class(es) that will be
     * applied to any caption generated for this table.
     */
    public String getCaptionClass() {
        if (null != this.captionClass) {
            return this.captionClass;
        }
        ValueExpression _ve = getValueExpression("captionClass");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>captionClass</code> property.</p>
     */
    public void setCaptionClass(String captionClass) {
        this.captionClass = captionClass;
    }


    private String captionStyle;

    /**
     * <p>Return the value of the <code>captionStyle</code> property.</p>
     * <p>Contents: CSS style(s) to be applied when this caption is rendered.
     */
    public String getCaptionStyle() {
        if (null != this.captionStyle) {
            return this.captionStyle;
        }
        ValueExpression _ve = getValueExpression("captionStyle");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>captionStyle</code> property.</p>
     */
    public void setCaptionStyle(String captionStyle) {
        this.captionStyle = captionStyle;
    }


    private String cellpadding;

    /**
     * <p>Return the value of the <code>cellpadding</code> property.</p>
     * <p>Contents: Definition of how much space the user agent should
     * leave between the border of each cell and its contents.
     */
    public String getCellpadding() {
        if (null != this.cellpadding) {
            return this.cellpadding;
        }
        ValueExpression _ve = getValueExpression("cellpadding");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>cellpadding</code> property.</p>
     */
    public void setCellpadding(String cellpadding) {
        this.cellpadding = cellpadding;
    }


    private String cellspacing;

    /**
     * <p>Return the value of the <code>cellspacing</code> property.</p>
     * <p>Contents: Definition of how much space the user agent should
     * leave between the left side of the table and the
     * leftmost column, the top of the table and the top of
     * the top side of the topmost row, and so on for the
     * right and bottom of the table.  It also specifies
     * the amount of space to leave between cells.
     */
    public String getCellspacing() {
        if (null != this.cellspacing) {
            return this.cellspacing;
        }
        ValueExpression _ve = getValueExpression("cellspacing");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>cellspacing</code> property.</p>
     */
    public void setCellspacing(String cellspacing) {
        this.cellspacing = cellspacing;
    }


    private String columnClasses;

    /**
     * <p>Return the value of the <code>columnClasses</code> property.</p>
     * <p>Contents: Comma-delimited list of CSS style classes that will be applied
     * to the columns of this table.  A space separated list of
     * classes may also be specified for any individual column.  If
     * the number of elements in this list is less than the number of
     * columns specified in the "columns" attribute, no "class"
     * attribute is output for each column greater than the number of
     * elements in the list.  If the number of elements in the list
     * is greater than the number of columns specified in the
     * "columns" attribute, the elements at the posisiton in the list
     * after the value of the "columns" attribute are ignored.
     */
    public String getColumnClasses() {
        if (null != this.columnClasses) {
            return this.columnClasses;
        }
        ValueExpression _ve = getValueExpression("columnClasses");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>columnClasses</code> property.</p>
     */
    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }


    private String dir;

    /**
     * <p>Return the value of the <code>dir</code> property.</p>
     * <p>Contents: Direction indication for text that does not inherit directionality.
     * Valid values are "LTR" (left-to-right) and "RTL" (right-to-left).
     */
    public String getDir() {
        if (null != this.dir) {
            return this.dir;
        }
        ValueExpression _ve = getValueExpression("dir");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>dir</code> property.</p>
     */
    public void setDir(String dir) {
        this.dir = dir;
    }


    private String footerClass;

    /**
     * <p>Return the value of the <code>footerClass</code> property.</p>
     * <p>Contents: Space-separated list of CSS style class(es) that will be
     * applied to any footer generated for this table.
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


    private String frame;

    /**
     * <p>Return the value of the <code>frame</code> property.</p>
     * <p>Contents: Code specifying which sides of the frame surrounding
     * this table will be visible.  Valid values are:
     * none (no sides, default value); above (top side only);
     * below (bottom side only); hsides (top and bottom sides
     * only); vsides (right and left sides only); lhs (left
     * hand side only); rhs (right hand side only); box
     * (all four sides); and border (all four sides).
     */
    public String getFrame() {
        if (null != this.frame) {
            return this.frame;
        }
        ValueExpression _ve = getValueExpression("frame");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>frame</code> property.</p>
     */
    public void setFrame(String frame) {
        this.frame = frame;
    }


    private String headerClass;

    /**
     * <p>Return the value of the <code>headerClass</code> property.</p>
     * <p>Contents: Space-separated list of CSS style class(es) that will be
     * applied to any header generated for this table.
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


    private String lang;

    /**
     * <p>Return the value of the <code>lang</code> property.</p>
     * <p>Contents: Code describing the language used in the generated markup
     * for this component.
     */
    public String getLang() {
        if (null != this.lang) {
            return this.lang;
        }
        ValueExpression _ve = getValueExpression("lang");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>lang</code> property.</p>
     */
    public void setLang(String lang) {
        this.lang = lang;
    }


    private String onclick;

    /**
     * <p>Return the value of the <code>onclick</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * clicked over this element.
     */
    public String getOnclick() {
        if (null != this.onclick) {
            return this.onclick;
        }
        ValueExpression _ve = getValueExpression("onclick");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>onclick</code> property.</p>
     */
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }


    private String ondblclick;

    /**
     * <p>Return the value of the <code>ondblclick</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * double clicked over this element.
     */
    public String getOndblclick() {
        if (null != this.ondblclick) {
            return this.ondblclick;
        }
        ValueExpression _ve = getValueExpression("ondblclick");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>ondblclick</code> property.</p>
     */
    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }


    private String onkeydown;

    /**
     * <p>Return the value of the <code>onkeydown</code> property.</p>
     * <p>Contents: Javascript code executed when a key is
     * pressed down over this element.
     */
    public String getOnkeydown() {
        if (null != this.onkeydown) {
            return this.onkeydown;
        }
        ValueExpression _ve = getValueExpression("onkeydown");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>onkeydown</code> property.</p>
     */
    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }


    private String onkeypress;

    /**
     * <p>Return the value of the <code>onkeypress</code> property.</p>
     * <p>Contents: Javascript code executed when a key is
     * pressed and released over this element.
     */
    public String getOnkeypress() {
        if (null != this.onkeypress) {
            return this.onkeypress;
        }
        ValueExpression _ve = getValueExpression("onkeypress");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>onkeypress</code> property.</p>
     */
    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }


    private String onkeyup;

    /**
     * <p>Return the value of the <code>onkeyup</code> property.</p>
     * <p>Contents: Javascript code executed when a key is
     * released over this element.
     */
    public String getOnkeyup() {
        if (null != this.onkeyup) {
            return this.onkeyup;
        }
        ValueExpression _ve = getValueExpression("onkeyup");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>onkeyup</code> property.</p>
     */
    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }


    private String onmousedown;

    /**
     * <p>Return the value of the <code>onmousedown</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * pressed down over this element.
     */
    public String getOnmousedown() {
        if (null != this.onmousedown) {
            return this.onmousedown;
        }
        ValueExpression _ve = getValueExpression("onmousedown");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>onmousedown</code> property.</p>
     */
    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }


    private String onmousemove;

    /**
     * <p>Return the value of the <code>onmousemove</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * moved within this element.
     */
    public String getOnmousemove() {
        if (null != this.onmousemove) {
            return this.onmousemove;
        }
        ValueExpression _ve = getValueExpression("onmousemove");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>onmousemove</code> property.</p>
     */
    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }


    private String onmouseout;

    /**
     * <p>Return the value of the <code>onmouseout</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * moved away from this element.
     */
    public String getOnmouseout() {
        if (null != this.onmouseout) {
            return this.onmouseout;
        }
        ValueExpression _ve = getValueExpression("onmouseout");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>onmouseout</code> property.</p>
     */
    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }


    private String onmouseover;

    /**
     * <p>Return the value of the <code>onmouseover</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * moved onto this element.
     */
    public String getOnmouseover() {
        if (null != this.onmouseover) {
            return this.onmouseover;
        }
        ValueExpression _ve = getValueExpression("onmouseover");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>onmouseover</code> property.</p>
     */
    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }


    private String onmouseup;

    /**
     * <p>Return the value of the <code>onmouseup</code> property.</p>
     * <p>Contents: Javascript code executed when a pointer button is
     * released over this element.
     */
    public String getOnmouseup() {
        if (null != this.onmouseup) {
            return this.onmouseup;
        }
        ValueExpression _ve = getValueExpression("onmouseup");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>onmouseup</code> property.</p>
     */
    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }


    private String rowClasses;

    /**
     * <p>Return the value of the <code>rowClasses</code> property.</p>
     * <p>Contents: Comma-delimited list of CSS style classes that will be applied
     * to the rows of this table.  A space separated list of classes
     * may also be specified for any individual row.  Thes styles are
     * applied, in turn, to each row in the table.  For example, if
     * the list has two elements, the first style class in the list
     * is applied to the first row, the second to the second row, the
     * first to the third row, the second to the fourth row, etc.  In
     * other words, we keep iterating through the list until we reach
     * the end, and then we start at the beginning again.
     */
    public String getRowClasses() {
        if (null != this.rowClasses) {
            return this.rowClasses;
        }
        ValueExpression _ve = getValueExpression("rowClasses");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>rowClasses</code> property.</p>
     */
    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }


    private String rules;

    /**
     * <p>Return the value of the <code>rules</code> property.</p>
     * <p>Contents: Code specifying which rules will appear between cells  within this table.  Valid values are:  none (no rules,
     * default value); groups (between row groups); rows
     * (between rows only); cols (between columns only); and
     * all (between all rows and columns).
     */
    public String getRules() {
        if (null != this.rules) {
            return this.rules;
        }
        ValueExpression _ve = getValueExpression("rules");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>rules</code> property.</p>
     */
    public void setRules(String rules) {
        this.rules = rules;
    }


    private String style;

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     * <p>Contents: CSS style(s) to be applied when this component is rendered.
     */
    public String getStyle() {
        if (null != this.style) {
            return this.style;
        }
        ValueExpression _ve = getValueExpression("style");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>style</code> property.</p>
     */
    public void setStyle(String style) {
        this.style = style;
    }


    private String styleClass;

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     * <p>Contents: Space-separated list of CSS style class(es) to be applied when     this element is rendered.  This value must be passed through
     * as the "class" attribute on generated markup.
     */
    public String getStyleClass() {
        if (null != this.styleClass) {
            return this.styleClass;
        }
        ValueExpression _ve = getValueExpression("styleClass");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }


    private String summary;

    /**
     * <p>Return the value of the <code>summary</code> property.</p>
     * <p>Contents: Summary of this table's purpose and structure, for
     * user agents rendering to non-visual media such as
     * speech and Braille.
     */
    public String getSummary() {
        if (null != this.summary) {
            return this.summary;
        }
        ValueExpression _ve = getValueExpression("summary");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>summary</code> property.</p>
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }


    private String title;

    /**
     * <p>Return the value of the <code>title</code> property.</p>
     * <p>Contents: Advisory title information about markup elements generated
     * for this component.
     */
    public String getTitle() {
        if (null != this.title) {
            return this.title;
        }
        ValueExpression _ve = getValueExpression("title");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>title</code> property.</p>
     */
    public void setTitle(String title) {
        this.title = title;
    }


    private String width;

    /**
     * <p>Return the value of the <code>width</code> property.</p>
     * <p>Contents: Width of the entire table, for visual user agents.
     */
    public String getWidth() {
        if (null != this.width) {
            return this.width;
        }
        ValueExpression _ve = getValueExpression("width");
        if (_ve != null) {
            return (String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Set the value of the <code>width</code> property.</p>
     */
    public void setWidth(String width) {
        this.width = width;
    }


    private Object[] _values;

    public Object saveState(FacesContext _context) {
        if (_values == null) {
            _values = new Object[31];
        }
        _values[0] = super.saveState(_context);
        _values[1] = bgcolor;
        _values[2] = new Integer(this.border);
        _values[3] = this.border_set ? Boolean.TRUE : Boolean.FALSE;
        _values[4] = captionClass;
        _values[5] = captionStyle;
        _values[6] = cellpadding;
        _values[7] = cellspacing;
        _values[8] = columnClasses;
        _values[9] = dir;
        _values[10] = footerClass;
        _values[11] = frame;
        _values[12] = headerClass;
        _values[13] = lang;
        _values[14] = onclick;
        _values[15] = ondblclick;
        _values[16] = onkeydown;
        _values[17] = onkeypress;
        _values[18] = onkeyup;
        _values[19] = onmousedown;
        _values[20] = onmousemove;
        _values[21] = onmouseout;
        _values[22] = onmouseover;
        _values[23] = onmouseup;
        _values[24] = rowClasses;
        _values[25] = rules;
        _values[26] = style;
        _values[27] = styleClass;
        _values[28] = summary;
        _values[29] = title;
        _values[30] = width;
        return _values;
}


    public void restoreState(FacesContext _context, Object _state) {
        _values = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        this.bgcolor = (String) _values[1];
        this.border = ((Integer) _values[2]).intValue();
        this.border_set = ((Boolean) _values[3]).booleanValue();
        this.captionClass = (String) _values[4];
        this.captionStyle = (String) _values[5];
        this.cellpadding = (String) _values[6];
        this.cellspacing = (String) _values[7];
        this.columnClasses = (String) _values[8];
        this.dir = (String) _values[9];
        this.footerClass = (String) _values[10];
        this.frame = (String) _values[11];
        this.headerClass = (String) _values[12];
        this.lang = (String) _values[13];
        this.onclick = (String) _values[14];
        this.ondblclick = (String) _values[15];
        this.onkeydown = (String) _values[16];
        this.onkeypress = (String) _values[17];
        this.onkeyup = (String) _values[18];
        this.onmousedown = (String) _values[19];
        this.onmousemove = (String) _values[20];
        this.onmouseout = (String) _values[21];
        this.onmouseover = (String) _values[22];
        this.onmouseup = (String) _values[23];
        this.rowClasses = (String) _values[24];
        this.rules = (String) _values[25];
        this.style = (String) _values[26];
        this.styleClass = (String) _values[27];
        this.summary = (String) _values[28];
        this.title = (String) _values[29];
        this.width = (String) _values[30];
    }


}
