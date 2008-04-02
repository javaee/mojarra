package com.sun.faces.sandbox.taglib;

import com.sun.faces.sandbox.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;

public final class AccessibleDataTableTag extends UIComponentTag {


    // Setter Methods
    // PROPERTY: first
    private String first;
    public void setFirst(String first) {
        this.first = first;
    }

    // PROPERTY: rows
    private String rows;
    public void setRows(String rows) {
        this.rows = rows;
    }

    // PROPERTY: value
    private String value;
    public void setValue(String value) {
        this.value = value;
    }

    // PROPERTY: var
    private String _var;
    public void setVar(String _var) {
        this._var = _var;
    }

    // PROPERTY: bgcolor
    private String bgcolor;
    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    // PROPERTY: border
    private String border;
    public void setBorder(String border) {
        this.border = border;
    }

    // PROPERTY: bodyrows
    private String bodyrows;
    public void setBodyrows(String bodyrows) {
        this.bodyrows = bodyrows;
    }

    // PROPERTY: captionClass
    private String captionClass;
    public void setCaptionClass(String captionClass) {
        this.captionClass = captionClass;
    }

    // PROPERTY: captionStyle
    private String captionStyle;
    public void setCaptionStyle(String captionStyle) {
        this.captionStyle = captionStyle;
    }

    // PROPERTY: cellpadding
    private String cellpadding;
    public void setCellpadding(String cellpadding) {
        this.cellpadding = cellpadding;
    }

    // PROPERTY: cellspacing
    private String cellspacing;
    public void setCellspacing(String cellspacing) {
        this.cellspacing = cellspacing;
    }

    // PROPERTY: columnClasses
    private String columnClasses;
    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    // PROPERTY: dir
    private String dir;
    public void setDir(String dir) {
        this.dir = dir;
    }

    // PROPERTY: footerClass
    private String footerClass;
    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }

    // PROPERTY: frame
    private String frame;
    public void setFrame(String frame) {
        this.frame = frame;
    }

    // PROPERTY: headerClass
    private String headerClass;
    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    // PROPERTY: lang
    private String lang;
    public void setLang(String lang) {
        this.lang = lang;
    }

    // PROPERTY: onclick
    private String onclick;
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    // PROPERTY: ondblclick
    private String ondblclick;
    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    // PROPERTY: onkeydown
    private String onkeydown;
    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    // PROPERTY: onkeypress
    private String onkeypress;
    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    // PROPERTY: onkeyup
    private String onkeyup;
    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    // PROPERTY: onmousedown
    private String onmousedown;
    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    // PROPERTY: onmousemove
    private String onmousemove;
    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }

    // PROPERTY: onmouseout
    private String onmouseout;
    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    // PROPERTY: onmouseover
    private String onmouseover;
    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    // PROPERTY: onmouseup
    private String onmouseup;
    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }

    // PROPERTY: rowClasses
    private String rowClasses;
    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }

    // PROPERTY: rules
    private String rules;
    public void setRules(String rules) {
        this.rules = rules;
    }

    // PROPERTY: style
    private String style;
    public void setStyle(String style) {
        this.style = style;
    }

    // PROPERTY: styleClass
    private String styleClass;
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    // PROPERTY: summary
    private String summary;
    public void setSummary(String summary) {
        this.summary = summary;
    }

    // PROPERTY: title
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }

    // PROPERTY: width
    private String width;
    public void setWidth(String width) {
        this.width = width;
    }


    // General Methods
    public String getRendererType() {
        return "com.sun.faces.sandbox.AccessibleTableRenderer";
    }

    public String getComponentType() {
        return "com.sun.faces.sandbox.AccessibleHtmlDataTable";
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        javax.faces.component.UIData data = null;
        try {
            data = (javax.faces.component.UIData) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: javax.faces.component.UIData.  Perhaps you're missing a tag?");
        }

        if (first != null) {
            if (isValueReference(first)) {
                ValueBinding vb = Util.getValueBinding(first);
                data.setValueBinding("first", vb);
            } else {
                int _first = Integer.parseInt(first);
                data.setFirst(_first);
            }
        }

        if (rows != null) {
            if (isValueReference(rows)) {
                ValueBinding vb = Util.getValueBinding(rows);
                data.setValueBinding("rows", vb);
            } else {
                int _rows = Integer.parseInt(rows);
                data.setRows(_rows);
            }
        }

        if (value != null) {
            if (isValueReference(value)) {
                ValueBinding vb = Util.getValueBinding(value);
                data.setValueBinding("value", vb);
            } else {
                data.setValue(value);
            }
        }

        data.setVar(_var);

        if (bgcolor != null) {
            if (isValueReference(bgcolor)) {
                ValueBinding vb = Util.getValueBinding(bgcolor);
                data.setValueBinding("bgcolor", vb);
            } else {
                data.getAttributes().put("bgcolor", bgcolor);
            }
        }

        if (border != null) {
            if (isValueReference(border)) {
                ValueBinding vb = Util.getValueBinding(border);
                data.setValueBinding("border", vb);
            } else {
                int _border = Integer.parseInt(border);
                if (_border != Integer.MIN_VALUE) {
                    data.getAttributes().put("border", Integer.valueOf(_border));
                }
            }
        }
        if (bodyrows != null) {
            if (isValueReference(bodyrows)) {
                ValueBinding vb = Util.getValueBinding(bodyrows);
                data.setValueBinding("bodyrows", vb);
            } else {
                data.getAttributes().put("bodyrows", bodyrows);
            }
        }
        if (captionClass != null) {
            if (isValueReference(captionClass)) {
                ValueBinding vb = Util.getValueBinding(captionClass);
                data.setValueBinding("captionClass", vb);
            } else {
                data.getAttributes().put("captionClass", bgcolor);
            }
        }
        if (captionStyle != null) {
            if (isValueReference(captionStyle)) {
                ValueBinding vb = Util.getValueBinding(captionStyle);
                data.setValueBinding("captionStyle", vb);
            } else {
                data.getAttributes().put("captionStyle", bgcolor);
            }
        }
        if (cellpadding != null) {
            if (isValueReference(cellpadding)) {
                ValueBinding vb = Util.getValueBinding(cellpadding);
                data.setValueBinding("cellpadding", vb);
            } else {
                data.getAttributes().put("cellpadding", cellpadding);
            }
        }
        if (cellspacing != null) {
            if (isValueReference(cellspacing)) {
                ValueBinding vb = Util.getValueBinding(cellspacing);
                data.setValueBinding("cellspacing", vb);
            } else {
                data.getAttributes().put("cellspacing", cellspacing);
            }
        }
        if (columnClasses != null) {
            if (isValueReference(columnClasses)) {
                ValueBinding vb = Util.getValueBinding(columnClasses);
                data.setValueBinding("columnClasses", vb);
            } else {
                data.getAttributes().put("columnClasses", columnClasses);
            }
        }
        if (dir != null) {
            if (isValueReference(dir)) {
                ValueBinding vb = Util.getValueBinding(dir);
                data.setValueBinding("dir", vb);
            } else {
                data.getAttributes().put("dir", dir);
            }
        }
        if (footerClass != null) {
            if (isValueReference(footerClass)) {
                ValueBinding vb = Util.getValueBinding(footerClass);
                data.setValueBinding("footerClass", vb);
            } else {
                data.getAttributes().put("footerClass", footerClass);
            }
        }
        if (frame != null) {
            if (isValueReference(frame)) {
                ValueBinding vb = Util.getValueBinding(frame);
                data.setValueBinding("frame", vb);
            } else {
                data.getAttributes().put("frame", frame);
            }
        }
        if (headerClass != null) {
            if (isValueReference(headerClass)) {
                ValueBinding vb = Util.getValueBinding(headerClass);
                data.setValueBinding("headerClass", vb);
            } else {
                data.getAttributes().put("headerClass", headerClass);
            }
        }
        if (lang != null) {
            if (isValueReference(lang)) {
                ValueBinding vb = Util.getValueBinding(lang);
                data.setValueBinding("lang", vb);
            } else {
                data.getAttributes().put("lang", lang);
            }
        }
        if (onclick != null) {
            if (isValueReference(onclick)) {
                ValueBinding vb = Util.getValueBinding(onclick);
                data.setValueBinding("onclick", vb);
            } else {
                data.getAttributes().put("onclick", onclick);
            }
        }
        if (ondblclick != null) {
            if (isValueReference(ondblclick)) {
                ValueBinding vb = Util.getValueBinding(ondblclick);
                data.setValueBinding("ondblclick", vb);
            } else {
                data.getAttributes().put("ondblclick", ondblclick);
            }
        }
        if (onkeydown != null) {
            if (isValueReference(onkeydown)) {
                ValueBinding vb = Util.getValueBinding(onkeydown);
                data.setValueBinding("onkeydown", vb);
            } else {
                data.getAttributes().put("onkeydown", onkeydown);
            }
        }
        if (onkeypress != null) {
            if (isValueReference(onkeypress)) {
                ValueBinding vb = Util.getValueBinding(onkeypress);
                data.setValueBinding("onkeypress", vb);
            } else {
                data.getAttributes().put("onkeypress", onkeypress);
            }
        }
        if (onkeyup != null) {
            if (isValueReference(onkeyup)) {
                ValueBinding vb = Util.getValueBinding(onkeyup);
                data.setValueBinding("onkeyup", vb);
            } else {
                data.getAttributes().put("onkeyup", onkeyup);
            }
        }
        if (onmousedown != null) {
            if (isValueReference(onmousedown)) {
                ValueBinding vb = Util.getValueBinding(onmousedown);
                data.setValueBinding("onmousedown", vb);
            } else {
                data.getAttributes().put("onmousedown", onmousedown);
            }
        }
        if (onmousemove != null) {
            if (isValueReference(onmousemove)) {
                ValueBinding vb = Util.getValueBinding(onmousemove);
                data.setValueBinding("onmousemove", vb);
            } else {
                data.getAttributes().put("onmousemove", onmousemove);
            }
        }
        if (onmouseout != null) {
            if (isValueReference(onmouseout)) {
                ValueBinding vb = Util.getValueBinding(onmouseout);
                data.setValueBinding("onmouseout", vb);
            } else {
                data.getAttributes().put("onmouseout", onmouseout);
            }
        }
        if (onmouseover != null) {
            if (isValueReference(onmouseover)) {
                ValueBinding vb = Util.getValueBinding(onmouseover);
                data.setValueBinding("onmouseover", vb);
            } else {
                data.getAttributes().put("onmouseover", onmouseover);
            }
        }
        if (onmouseup != null) {
            if (isValueReference(onmouseup)) {
                ValueBinding vb = Util.getValueBinding(onmouseup);
                data.setValueBinding("onmouseup", vb);
            } else {
                data.getAttributes().put("onmouseup", onmouseup);
            }
        }
        if (rowClasses != null) {
            if (isValueReference(rowClasses)) {
                ValueBinding vb = Util.getValueBinding(rowClasses);
                data.setValueBinding("rowClasses", vb);
            } else {
                data.getAttributes().put("rowClasses", rowClasses);
            }
        }
        if (rules != null) {
            if (isValueReference(rules)) {
                ValueBinding vb = Util.getValueBinding(rules);
                data.setValueBinding("rules", vb);
            } else {
                data.getAttributes().put("rules", rules);
            }
        }
        if (style != null) {
            if (isValueReference(style)) {
                ValueBinding vb = Util.getValueBinding(style);
                data.setValueBinding("style", vb);
            } else {
                data.getAttributes().put("style", style);
            }
        }
        if (styleClass != null) {
            if (isValueReference(styleClass)) {
                ValueBinding vb = Util.getValueBinding(styleClass);
                data.setValueBinding("styleClass", vb);
            } else {
                data.getAttributes().put("styleClass", styleClass);
            }
        }
        if (summary != null) {
            if (isValueReference(summary)) {
                ValueBinding vb = Util.getValueBinding(summary);
                data.setValueBinding("summary", vb);
            } else {
                data.getAttributes().put("summary", summary);
            }
        }
        if (title != null) {
            if (isValueReference(title)) {
                ValueBinding vb = Util.getValueBinding(title);
                data.setValueBinding("title", vb);
            } else {
                data.getAttributes().put("title", title);
            }
        }
        if (width != null) {
            if (isValueReference(width)) {
                ValueBinding vb = Util.getValueBinding(width);
                data.setValueBinding("width", vb);
            } else {
                data.getAttributes().put("width", width);
            }
        }
    }
    // Methods From TagSupport
    public int doStartTag() throws JspException {
        try {
            return super.doStartTag();
        } catch (Exception e) {
            Throwable root = e;
            while (root.getCause() != null) {
                root = root.getCause();
            }
            throw new JspException(root);
        }
    }

    public int doEndTag() throws JspException {
        try {
            return super.doEndTag();
        } catch (Exception e) {
            Throwable root = e;
            while (root.getCause() != null) {
                root = root.getCause();
            }
            throw new JspException(root);
        }
    }

    // RELEASE
    public void release() {
        super.release();

        // component properties
        this.first = null;
        this.rows = null;
        this.value = null;
        this._var = null;

        // rendered attributes
        this.bgcolor = null;
        this.border = null;
        this.captionClass = null;
        this.captionStyle = null;
        this.cellpadding = null;
        this.cellspacing = null;
        this.columnClasses = null;
        this.dir = null;
        this.footerClass = null;
        this.frame = null;
        this.headerClass = null;
        this.lang = null;
        this.onclick = null;
        this.ondblclick = null;
        this.onkeydown = null;
        this.onkeypress = null;
        this.onkeyup = null;
        this.onmousedown = null;
        this.onmousemove = null;
        this.onmouseout = null;
        this.onmouseover = null;
        this.onmouseup = null;
        this.rowClasses = null;
        this.rules = null;
        this.style = null;
        this.styleClass = null;
        this.summary = null;
        this.title = null;
        this.width = null;
    }

    public String getDebugString() {
        return "id: " + this.getId() + " class: " + this.getClass().getName();
    }

}
