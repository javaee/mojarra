/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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



public final class CommandButtonTag extends UIComponentELTag {


    // Setter Methods
    // PROPERTY: action
    private javax.el.MethodExpression action;
    public void setAction(javax.el.MethodExpression action) {
        this.action = action;
    }

    // PROPERTY: actionListener
    private javax.el.MethodExpression actionListener;
    public void setActionListener(javax.el.MethodExpression actionListener) {
        this.actionListener = actionListener;
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

    // PROPERTY: dx 
    private javax.el.ValueExpression dx;
    public void setDx(javax.el.ValueExpression dx) {
        this.dx = dx;
    }

    // PROPERTY: dy 
    private javax.el.ValueExpression dy;
    public void setDy(javax.el.ValueExpression dy) {
        this.dy = dy;
    }

    // PROPERTY: height 
    private javax.el.ValueExpression height;
    public void setHeight(javax.el.ValueExpression height) {
        this.height = height;
    }

    // PROPERTY: immediate
    private javax.el.ValueExpression immediate;
    public void setImmediate(javax.el.ValueExpression immediate) {
        this.immediate = immediate;
    }

    // PROPERTY: label 
    private javax.el.ValueExpression label;
    public void setLabel(javax.el.ValueExpression label) {
        this.label = label;
    }

    // PROPERTY: onactivate
    private javax.el.ValueExpression onactivate;
    public void setOnactivate(javax.el.ValueExpression onactivate) {
        this.onactivate = onactivate;
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

    // PROPERTY: type
    private javax.el.ValueExpression type;
    public void setType(javax.el.ValueExpression type) {
        this.type = type;
    }

    // PROPERTY: value
    private javax.el.ValueExpression value;
    public void setValue(javax.el.ValueExpression value) {
        this.value = value;
    }

    // PROPERTY: width 
    private javax.el.ValueExpression width;
    public void setWidth(javax.el.ValueExpression width) {
        this.width = width;
    }

    // General Methods
    public String getRendererType() {
        return "renderkit.svg.Button";
    }

    public String getComponentType() {
        return "javax.faces.Command";
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        javax.faces.component.UICommand command = null;
        try {
            command = (javax.faces.component.UICommand) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: javax.faces.component.UICommand.  Perhaps you're missing a tag?");
        }

        if (action != null) {
            command.setActionExpression(action);
        }
        if (actionListener != null) {
            command.addActionListener(new MethodExpressionActionListener(actionListener));
        }
        if (x != null) {
            if (!x.isLiteralText()) {
                command.setValueExpression("x", x);
            } else {
                command.getAttributes().put("x", x.getExpressionString());
            }
        }
        if (y != null) {
            if (!y.isLiteralText()) {
                command.setValueExpression("y", y);
            } else {
                command.getAttributes().put("y", y.getExpressionString());
            }
        }
        if (dx != null) {
            if (!dx.isLiteralText()) {
                command.setValueExpression("dx", dx);
            } else {
                command.getAttributes().put("dx", dx.getExpressionString());
            }
        }
        if (dy != null) {
            if (!dy.isLiteralText()) {
                command.setValueExpression("dy", dy);
            } else {
                command.getAttributes().put("dy", dy.getExpressionString());
            }
        }
        if (height != null) {
            if (!height.isLiteralText()) {
                command.setValueExpression("height", height);
            } else {
                command.getAttributes().put("height", height.getExpressionString());
            }
        }
        if (immediate != null) {
            if (!immediate.isLiteralText()) {
                command.setValueExpression("immediate", immediate);
            } else {
                command.setImmediate(java.lang.Boolean.valueOf(immediate.getExpressionString()).booleanValue());
            }
        }
        if (label != null) {
            if (!label.isLiteralText()) {
                command.setValueExpression("label", label);
            } else {
                command.getAttributes().put("label", label.getExpressionString());
            }
        }
        if (onactivate != null) {
            if (!onactivate.isLiteralText()) {
                command.setValueExpression("onactivate", onactivate);
            } else {
                command.getAttributes().put("onactivate", onactivate.getExpressionString());
            }
        }
        if (onclick != null) {
            if (!onclick.isLiteralText()) {
                command.setValueExpression("onclick", onclick);
            } else {
                command.getAttributes().put("onclick", onclick.getExpressionString());
            }
        }
        if (onfocusin != null) {
            if (!onfocusin.isLiteralText()) {
                command.setValueExpression("onfocusin", onfocusin);
            } else {
                command.getAttributes().put("onfocusin", onfocusin.getExpressionString());
            }
        }
        if (onfocusout  != null) {
            if (!onfocusout.isLiteralText()) {
                command.setValueExpression("onfocusout", onfocusout);
            } else {
                command.getAttributes().put("onfocusout", onfocusout.getExpressionString());
            }
        }
        if (onmousedown != null) {
            if (!onmousedown.isLiteralText()) {
                command.setValueExpression("onmousedown", onmousedown);
            } else {
                command.getAttributes().put("onmousedown", onmousedown.getExpressionString());
            }
        }
        if (onmousemove != null) {
            if (!onmousemove.isLiteralText()) {
                command.setValueExpression("onmousemove", onmousemove);
            } else {
                command.getAttributes().put("onmousemove", onmousemove.getExpressionString());
            }
        }
        if (onmouseout != null) {
            if (!onmouseout.isLiteralText()) {
                command.setValueExpression("onmouseout", onmouseout);
            } else {
                command.getAttributes().put("onmouseout", onmouseout.getExpressionString());
            }
        }
        if (onmouseover != null) {
            if (!onmouseover.isLiteralText()) {
                command.setValueExpression("onmouseover", onmouseover);
            } else {
                command.getAttributes().put("onmouseover", onmouseover.getExpressionString());
            }
        }
        if (onmouseup != null) {
            if (!onmouseup.isLiteralText()) {
                command.setValueExpression("onmouseup", onmouseup);
            } else {
                command.getAttributes().put("onmouseup", onmouseup.getExpressionString());
            }
        }
        if (rx != null) {
            if (!rx.isLiteralText()) {
                command.setValueExpression("rx", rx);
            } else {
                command.getAttributes().put("rx", rx.getExpressionString());
            }
        }
        if (ry != null) {
            if (!ry.isLiteralText()) {
                command.setValueExpression("ry", ry);
            } else {
                command.getAttributes().put("ry", ry.getExpressionString());
            }
        }
        if (style != null) {
            if (!style.isLiteralText()) {
                command.setValueExpression("style", style);
            } else {
                command.getAttributes().put("style", style.getExpressionString());
            }
        }
        if (type != null) {
            if (!type.isLiteralText()) {
                command.setValueExpression("type", type);
            } else {
                command.getAttributes().put("type", type.getExpressionString());
            }
        }
        if (value != null) {
            if (!value.isLiteralText()) {
                command.setValueExpression("value", value);
            } else {
                command.setValue(value.getExpressionString());
            }
        }
        if (width != null) {
            if (!width.isLiteralText()) {
                command.setValueExpression("width", width);
            } else {
                command.getAttributes().put("width", width.getExpressionString());
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
        this.action = null;
        this.actionListener = null;
        this.immediate = null;
        this.value = null;

        // rendered attributes
        this.x = null;
        this.y = null;
        this.dx = null;
        this.dy = null;
        this.height = null;
        this.label = null;
        this.onactivate = null;
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
        this.type = null;
        this.width = null;
    }

    public String getDebugString() {
        String result = "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }

}
