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

package renderkits.taglib.svg;

import java.io.IOException;
import javax.el.*;
import javax.faces.*;
import javax.faces.component.*;
import javax.faces.context.*;
import javax.faces.convert.*;
import javax.faces.el.*;
import javax.faces.event.*;
import javax.faces.validator.*;
import javax.faces.webapp.*;
import javax.servlet.jsp.JspException;



public final class RectangleTag extends UIComponentELTag {


    // PROPERTY: height 
    private javax.el.ValueExpression height;
    public void setHeight(javax.el.ValueExpression height) {
        this.height = height;
    }

    // PROPERTY: onclick
    private javax.el.ValueExpression onclick;
    public void setOnclick(javax.el.ValueExpression onclick) {
        this.onclick = onclick;
    }

    // PROPERTY: onfocusin
    private javax.el.ValueExpression onfocusin;
    public void setOnfocusin(javax.el.ValueExpression onfocusin) {
        this.onfocusin = onfocusin;
    }

    // PROPERTY: onfocusout
    private javax.el.ValueExpression onfocusout;
    public void setOnfocusout(javax.el.ValueExpression onfocusout) {
        this.onfocusout = onfocusout;
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

    // PROPERTY: rx 
    private javax.el.ValueExpression rx;
    public void setRx(javax.el.ValueExpression rx) {
        this.rx = rx;
    }

    // PROPERTY: ry 
    private javax.el.ValueExpression ry;
    public void setRy(javax.el.ValueExpression ry) {
        this.ry = ry;
    }

    // PROPERTY: style
    private javax.el.ValueExpression style;
    public void setStyle(javax.el.ValueExpression style) {
        this.style = style;
    }

    // PROPERTY: width 
    private javax.el.ValueExpression width;
    public void setWidth(javax.el.ValueExpression width) {
        this.width = width;
    }

    // PROPERTY: x
    private javax.el.ValueExpression x;
    public void setX(javax.el.ValueExpression x) {
        this.x = x;
    }
                                                                                                                       
    // PROPERTY: y
    private javax.el.ValueExpression y;
    public void setY(javax.el.ValueExpression y) {
        this.y = y;
    }


    // General Methods
    public String getRendererType() {
        return "renderkit.svg.Rectangle";
    }

    public String getComponentType() {
        return "Rectangle";
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        renderkits.components.svg.Rectangle rectangle = null;
        try {
            rectangle = (renderkits.components.svg.Rectangle) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: renderkits.components.svg.Rectangle.  Perhaps you're missing a tag?");
        }

        if (height != null) {
            if (!height.isLiteralText()) {
                rectangle.setValueExpression("height", height);
            } else {
                rectangle.getAttributes().put("height", height.getExpressionString());
            }
        }
        if (onclick != null) {
            if (!onclick.isLiteralText()) {
                rectangle.setValueExpression("onclick", onclick);
            } else {
                rectangle.getAttributes().put("onclick", onclick.getExpressionString());
            }
        }
        if (onfocusin != null) {
            if (!onfocusin.isLiteralText()) {
                rectangle.setValueExpression("onfocusin", onfocusin);
            } else {
                rectangle.getAttributes().put("onfocusin", onfocusin.getExpressionString());
            }
        }
        if (onfocusout  != null) {
            if (!onfocusout.isLiteralText()) {
                rectangle.setValueExpression("onfocusout", onfocusout);
            } else {
                rectangle.getAttributes().put("onfocusout", onfocusout.getExpressionString());
            }
        }
        if (onmousedown != null) {
            if (!onmousedown.isLiteralText()) {
                rectangle.setValueExpression("onmousedown", onmousedown);
            } else {
                rectangle.getAttributes().put("onmousedown", onmousedown.getExpressionString());
            }
        }
        if (onmousemove != null) {
            if (!onmousemove.isLiteralText()) {
                rectangle.setValueExpression("onmousemove", onmousemove);
            } else {
                rectangle.getAttributes().put("onmousemove", onmousemove.getExpressionString());
            }
        }
        if (onmouseout != null) {
            if (!onmouseout.isLiteralText()) {
                rectangle.setValueExpression("onmouseout", onmouseout);
            } else {
                rectangle.getAttributes().put("onmouseout", onmouseout.getExpressionString());
            }
        }
        if (onmouseover != null) {
            if (!onmouseover.isLiteralText()) {
                rectangle.setValueExpression("onmouseover", onmouseover);
            } else {
                rectangle.getAttributes().put("onmouseover", onmouseover.getExpressionString());
            }
        }
        if (onmouseup != null) {
            if (!onmouseup.isLiteralText()) {
                rectangle.setValueExpression("onmouseup", onmouseup);
            } else {
                rectangle.getAttributes().put("onmouseup", onmouseup.getExpressionString());
            }
        }
        if (rx != null) {
            if (!rx.isLiteralText()) {
                rectangle.setValueExpression("rx", rx);
            } else {
                rectangle.getAttributes().put("rx", rx.getExpressionString());
            }
        }
        if (ry != null) {
            if (!ry.isLiteralText()) {
                rectangle.setValueExpression("ry", ry);
            } else {
                rectangle.getAttributes().put("ry", ry.getExpressionString());
            }
        }
        if (style != null) {
            if (!style.isLiteralText()) {
                rectangle.setValueExpression("style", style);
            } else {
                rectangle.getAttributes().put("style", style.getExpressionString());
            }
        }
        if (width != null) {
            if (!width.isLiteralText()) {
                rectangle.setValueExpression("width", width);
            } else {
                rectangle.getAttributes().put("width", width.getExpressionString());
            }
        }
        if (x != null) {
            if (!x.isLiteralText()) {
                rectangle.setValueExpression("x", x);
            } else {
                rectangle.getAttributes().put("x", x.getExpressionString());
            }
        }
        if (y != null) {
            if (!y.isLiteralText()) {
                rectangle.setValueExpression("y", y);
            } else {
                rectangle.getAttributes().put("y", y.getExpressionString());
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

        // rendered attributes
        this.height = null;
        this.onclick = null;
        this.onfocusin = null;
        this.onfocusout = null;
        this.onmousedown = null;
        this.onmousemove = null;
        this.onmouseout = null;
        this.onmouseover = null;
        this.onmouseup = null;
        this.rx = null;
        this.ry = null;
        this.style = null;
        this.width = null;
        this.x = null;
        this.y = null;
    }

    public String getDebugString() {
        String result = "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }

}
