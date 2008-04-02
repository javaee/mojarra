/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package renderkits.taglib.xul;

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

    // PROPERTY: onclick
    private javax.el.ValueExpression onclick;
    public void setOnclick(javax.el.ValueExpression onclick) {
        this.onclick = onclick;
    }

    // PROPERTY: oncommand
    private javax.el.ValueExpression oncommand;
    public void setOnactivate(javax.el.ValueExpression oncommand) {
        this.oncommand = oncommand;
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

    // General Methods
    public String getRendererType() {
        return "renderkit.xul.Button";
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
        if (oncommand != null) {
            if (!oncommand.isLiteralText()) {
                command.setValueExpression("oncommand", oncommand);
            } else {
                command.getAttributes().put("oncommand", oncommand.getExpressionString());
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
        if (style != null) {
            if (!style.isLiteralText()) {
                command.setValueExpression("style", style);
            } else {
                command.getAttributes().put("style", style.getExpressionString());
            }
        }
        if (styleClass != null) {
            if (!styleClass.isLiteralText()) {
                command.setValueExpression("styleClass", styleClass);
            } else {
                command.getAttributes().put("styleClass", styleClass.getExpressionString());
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
        this.label = null;
        this.onclick = null;
        this.oncommand = null;
        this.onfocusin = null;
        this.onfocusout = null;
        this.onmousedown = null;
        this.onmousemove = null;
        this.onmouseout = null;
        this.onmouseover = null;
        this.onmouseup = null;
        this.style = null;
        this.styleClass = null;
        this.type = null;
    }

    public String getDebugString() {
        String result = "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }

}
