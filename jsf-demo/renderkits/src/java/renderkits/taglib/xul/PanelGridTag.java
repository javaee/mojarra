/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package renderkits.taglib.xul;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;

/*
* ******* GENERATED CODE - DO NOT EDIT *******
*/


public final class PanelGridTag extends UIComponentELTag {


    // Setter Methods
    // PROPERTY: bgcolor
    private javax.el.ValueExpression bgcolor;

    public void setBgcolor(javax.el.ValueExpression bgcolor) {
        this.bgcolor = bgcolor;
    }

    // PROPERTY: border
    private javax.el.ValueExpression border;

    public void setBorder(javax.el.ValueExpression border) {
        this.border = border;
    }

    // PROPERTY: columnClasses
    private javax.el.ValueExpression columnClasses;

    public void setColumnClasses(javax.el.ValueExpression columnClasses) {
        this.columnClasses = columnClasses;
    }

    // PROPERTY: columns
    private javax.el.ValueExpression columns;

    public void setColumns(javax.el.ValueExpression columns) {
        this.columns = columns;
    }

    // PROPERTY: onclick
    private javax.el.ValueExpression onclick;

    public void setOnclick(javax.el.ValueExpression onclick) {
        this.onclick = onclick;
    }

    // PROPERTY: ondblclick
    private javax.el.ValueExpression ondblclick;

    public void setOndblclick(javax.el.ValueExpression ondblclick) {
        this.ondblclick = ondblclick;
    }

    // PROPERTY: onkeydown
    private javax.el.ValueExpression onkeydown;

    public void setOnkeydown(javax.el.ValueExpression onkeydown) {
        this.onkeydown = onkeydown;
    }

    // PROPERTY: onkeypress
    private javax.el.ValueExpression onkeypress;

    public void setOnkeypress(javax.el.ValueExpression onkeypress) {
        this.onkeypress = onkeypress;
    }

    // PROPERTY: onkeyup
    private javax.el.ValueExpression onkeyup;

    public void setOnkeyup(javax.el.ValueExpression onkeyup) {
        this.onkeyup = onkeyup;
    }

    // PROPERTY: onmousedown
    private javax.el.ValueExpression onmousedown;

    public void setOnmousedown(javax.el.ValueExpression onmousedown) {
        this.onmousedown = onmousedown;
    }

    // PROPERTY: onmousemove
    private javax.el.ValueExpression onmousemove;

    public void setOnmousemove(javax.el.ValueExpression onmousemove) {
        this.onmousemove = onmousemove;
    }

    // PROPERTY: onmouseout
    private javax.el.ValueExpression onmouseout;

    public void setOnmouseout(javax.el.ValueExpression onmouseout) {
        this.onmouseout = onmouseout;
    }

    // PROPERTY: onmouseover
    private javax.el.ValueExpression onmouseover;

    public void setOnmouseover(javax.el.ValueExpression onmouseover) {
        this.onmouseover = onmouseover;
    }

    // PROPERTY: onmouseup
    private javax.el.ValueExpression onmouseup;

    public void setOnmouseup(javax.el.ValueExpression onmouseup) {
        this.onmouseup = onmouseup;
    }

    // PROPERTY: rowClasses
    private javax.el.ValueExpression rowClasses;

    public void setRowClasses(javax.el.ValueExpression rowClasses) {
        this.rowClasses = rowClasses;
    }

    // PROPERTY: style
    private javax.el.ValueExpression style;

    public void setStyle(javax.el.ValueExpression style) {
        this.style = style;
    }

    // PROPERTY: styleClass
    private javax.el.ValueExpression styleClass;

    public void setStyleClass(javax.el.ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

    // General Methods
    public String getRendererType() {
        return "renderkit.xul.Grid";
    }

    public String getComponentType() {
        return "javax.faces.Panel";
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        javax.faces.component.UIPanel panel = null;
        try {
            panel = (javax.faces.component.UIPanel) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component "
                                            + component.toString()
                                            + " not expected type.  Expected: javax.faces.component.UIPanel.  Perhaps you're missing a tag?");
        }

        if (bgcolor != null) {
            if (!bgcolor.isLiteralText()) {
                panel.setValueExpression("bgcolor", bgcolor);
            } else {
                panel.getAttributes()
                      .put("bgcolor", bgcolor.getExpressionString());
            }
        }
        if (border != null) {
            if (!border.isLiteralText()) {
                panel.setValueExpression("border", border);
            } else {
                panel.getAttributes().put("border",
                                          java.lang.Integer.valueOf(border.getExpressionString()));
            }
        }
        if (columnClasses != null) {
            if (!columnClasses.isLiteralText()) {
                panel.setValueExpression("columnClasses", columnClasses);
            } else {
                panel.getAttributes().put("columnClasses",
                                          columnClasses.getExpressionString());
            }
        }
        if (columns != null) {
            if (!columns.isLiteralText()) {
                panel.setValueExpression("columns", columns);
            } else {
                panel.getAttributes().put("columns",
                                          java.lang.Integer.valueOf(columns.getExpressionString()));
            }
        }
        if (onclick != null) {
            if (!onclick.isLiteralText()) {
                panel.setValueExpression("onclick", onclick);
            } else {
                panel.getAttributes()
                      .put("onclick", onclick.getExpressionString());
            }
        }
        if (ondblclick != null) {
            if (!ondblclick.isLiteralText()) {
                panel.setValueExpression("ondblclick", ondblclick);
            } else {
                panel.getAttributes()
                      .put("ondblclick", ondblclick.getExpressionString());
            }
        }
        if (onkeydown != null) {
            if (!onkeydown.isLiteralText()) {
                panel.setValueExpression("onkeydown", onkeydown);
            } else {
                panel.getAttributes()
                      .put("onkeydown", onkeydown.getExpressionString());
            }
        }
        if (onkeypress != null) {
            if (!onkeypress.isLiteralText()) {
                panel.setValueExpression("onkeypress", onkeypress);
            } else {
                panel.getAttributes()
                      .put("onkeypress", onkeypress.getExpressionString());
            }
        }
        if (onkeyup != null) {
            if (!onkeyup.isLiteralText()) {
                panel.setValueExpression("onkeyup", onkeyup);
            } else {
                panel.getAttributes()
                      .put("onkeyup", onkeyup.getExpressionString());
            }
        }
        if (onmousedown != null) {
            if (!onmousedown.isLiteralText()) {
                panel.setValueExpression("onmousedown", onmousedown);
            } else {
                panel.getAttributes()
                      .put("onmousedown", onmousedown.getExpressionString());
            }
        }
        if (onmousemove != null) {
            if (!onmousemove.isLiteralText()) {
                panel.setValueExpression("onmousemove", onmousemove);
            } else {
                panel.getAttributes()
                      .put("onmousemove", onmousemove.getExpressionString());
            }
        }
        if (onmouseout != null) {
            if (!onmouseout.isLiteralText()) {
                panel.setValueExpression("onmouseout", onmouseout);
            } else {
                panel.getAttributes()
                      .put("onmouseout", onmouseout.getExpressionString());
            }
        }
        if (onmouseover != null) {
            if (!onmouseover.isLiteralText()) {
                panel.setValueExpression("onmouseover", onmouseover);
            } else {
                panel.getAttributes()
                      .put("onmouseover", onmouseover.getExpressionString());
            }
        }
        if (onmouseup != null) {
            if (!onmouseup.isLiteralText()) {
                panel.setValueExpression("onmouseup", onmouseup);
            } else {
                panel.getAttributes()
                      .put("onmouseup", onmouseup.getExpressionString());
            }
        }
        if (rowClasses != null) {
            if (!rowClasses.isLiteralText()) {
                panel.setValueExpression("rowClasses", rowClasses);
            } else {
                panel.getAttributes()
                      .put("rowClasses", rowClasses.getExpressionString());
            }
        }
        if (style != null) {
            if (!style.isLiteralText()) {
                panel.setValueExpression("style", style);
            } else {
                panel.getAttributes().put("style", style.getExpressionString());
            }
        }
        if (styleClass != null) {
            if (!styleClass.isLiteralText()) {
                panel.setValueExpression("styleClass", styleClass);
            } else {
                panel.getAttributes()
                      .put("styleClass", styleClass.getExpressionString());
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

        // rendered attributes
        this.bgcolor = null;
        this.border = null;
        this.columnClasses = null;
        this.columns = null;
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
        this.style = null;
        this.styleClass = null;
    }

    public String getDebugString() {
        String result =
              "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }

}
