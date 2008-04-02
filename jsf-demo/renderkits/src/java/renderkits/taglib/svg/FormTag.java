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



public final class FormTag extends UIComponentELTag {


    // Setter Methods
    // PROPERTY: prependId
    private javax.el.ValueExpression prependId;
    public void setPrependId(javax.el.ValueExpression prependId) {
        this.prependId = prependId;
    }

    // PROPERTY: accept
    private javax.el.ValueExpression accept;
    public void setAccept(javax.el.ValueExpression accept) {
        this.accept = accept;
    }

    // PROPERTY: acceptcharset
    private javax.el.ValueExpression acceptcharset;
    public void setAcceptcharset(javax.el.ValueExpression acceptcharset) {
        this.acceptcharset = acceptcharset;
    }

    // PROPERTY: enctype
    private javax.el.ValueExpression enctype;
    public void setEnctype(javax.el.ValueExpression enctype) {
        this.enctype = enctype;
    }

    // PROPERTY: onclick
    private javax.el.ValueExpression onclick;
    public void setOnclick(javax.el.ValueExpression onclick) {
        this.onclick = onclick;
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

    // General Methods
    public String getRendererType() {
        return "renderkit.svg.Form";
    }

    public String getComponentType() {
        return "javax.faces.Form";
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        javax.faces.component.UIForm form = null;
        try {
            form = (javax.faces.component.UIForm) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: javax.faces.component.UIForm.  Perhaps you're missing a tag?");
        }

        if (prependId != null) {
            if (!prependId.isLiteralText()) {
                form.setValueExpression("prependId", prependId);
            } else {
                form.setPrependId(java.lang.Boolean.valueOf(prependId.getExpressionString()).booleanValue());
            }
        }

        if (accept != null) {
            if (!accept.isLiteralText()) {
                form.setValueExpression("accept", accept);
            } else {
                form.getAttributes().put("accept", accept.getExpressionString());
            }
        }
        if (acceptcharset != null) {
            if (!acceptcharset.isLiteralText()) {
                form.setValueExpression("acceptcharset", acceptcharset);
            } else {
                form.getAttributes().put("acceptcharset", acceptcharset.getExpressionString());
            }
        }
        if (enctype != null) {
            if (!enctype.isLiteralText()) {
                form.setValueExpression("enctype", enctype);
            } else {
                form.getAttributes().put("enctype", enctype.getExpressionString());
            }
        }
        if (onclick != null) {
            if (!onclick.isLiteralText()) {
                form.setValueExpression("onclick", onclick);
            } else {
                form.getAttributes().put("onclick", onclick.getExpressionString());
            }
        }
        if (onmousedown != null) {
            if (!onmousedown.isLiteralText()) {
                form.setValueExpression("onmousedown", onmousedown);
            } else {
                form.getAttributes().put("onmousedown", onmousedown.getExpressionString());
            }
        }
        if (onmousemove != null) {
            if (!onmousemove.isLiteralText()) {
                form.setValueExpression("onmousemove", onmousemove);
            } else {
                form.getAttributes().put("onmousemove", onmousemove.getExpressionString());
            }
        }
        if (onmouseout != null) {
            if (!onmouseout.isLiteralText()) {
                form.setValueExpression("onmouseout", onmouseout);
            } else {
                form.getAttributes().put("onmouseout", onmouseout.getExpressionString());
            }
        }
        if (onmouseover != null) {
            if (!onmouseover.isLiteralText()) {
                form.setValueExpression("onmouseover", onmouseover);
            } else {
                form.getAttributes().put("onmouseover", onmouseover.getExpressionString());
            }
        }
        if (onmouseup != null) {
            if (!onmouseup.isLiteralText()) {
                form.setValueExpression("onmouseup", onmouseup);
            } else {
                form.getAttributes().put("onmouseup", onmouseup.getExpressionString());
            }
        }
        if (style != null) {
            if (!style.isLiteralText()) {
                form.setValueExpression("style", style);
            } else {
                form.getAttributes().put("style", style.getExpressionString());
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
        this.prependId = null;

        // rendered attributes
        this.accept = null;
        this.acceptcharset = null;
        this.enctype = null;
        this.onclick = null;
        this.onmousedown = null;
        this.onmousemove = null;
        this.onmouseout = null;
        this.onmouseover = null;
        this.onmouseup = null;
        this.style = null;
    }

    public String getDebugString() {
        String result = "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }

}
