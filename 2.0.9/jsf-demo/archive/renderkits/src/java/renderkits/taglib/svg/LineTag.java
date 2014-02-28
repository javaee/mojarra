/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package renderkits.taglib.svg;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;


public final class LineTag extends UIComponentELTag {


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

    // PROPERTY: style
    private javax.el.ValueExpression style;

    public void setStyle(javax.el.ValueExpression style) {
        this.style = style;
    }

    // PROPERTY: x1
    private javax.el.ValueExpression x1;

    public void setX1(javax.el.ValueExpression x1) {
        this.x1 = x1;
    }

    // PROPERTY: y1
    private javax.el.ValueExpression y1;

    public void setY1(javax.el.ValueExpression y1) {
        this.y1 = y1;
    }

    // PROPERTY: x2
    private javax.el.ValueExpression x2;

    public void setX2(javax.el.ValueExpression x2) {
        this.x2 = x2;
    }

    // PROPERTY: y2
    private javax.el.ValueExpression y2;

    public void setY2(javax.el.ValueExpression y2) {
        this.y2 = y2;
    }

    // General Methods
    public String getRendererType() {
        return "renderkit.svg.Line";
    }

    public String getComponentType() {
        return "Line";
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        renderkits.components.svg.Line line = null;
        try {
            line = (renderkits.components.svg.Line) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component "
                                            + component.toString()
                                            + " not expected type.  Expected: renderkits.components.svg.Line.  Perhaps you're missing a tag?");
        }

        if (onclick != null) {
            if (!onclick.isLiteralText()) {
                line.setValueExpression("onclick", onclick);
            } else {
                line.getAttributes()
                      .put("onclick", onclick.getExpressionString());
            }
        }
        if (onfocusin != null) {
            if (!onfocusin.isLiteralText()) {
                line.setValueExpression("onfocusin", onfocusin);
            } else {
                line.getAttributes()
                      .put("onfocusin", onfocusin.getExpressionString());
            }
        }
        if (onfocusout != null) {
            if (!onfocusout.isLiteralText()) {
                line.setValueExpression("onfocusout", onfocusout);
            } else {
                line.getAttributes()
                      .put("onfocusout", onfocusout.getExpressionString());
            }
        }
        if (onmousedown != null) {
            if (!onmousedown.isLiteralText()) {
                line.setValueExpression("onmousedown", onmousedown);
            } else {
                line.getAttributes()
                      .put("onmousedown", onmousedown.getExpressionString());
            }
        }
        if (onmousemove != null) {
            if (!onmousemove.isLiteralText()) {
                line.setValueExpression("onmousemove", onmousemove);
            } else {
                line.getAttributes()
                      .put("onmousemove", onmousemove.getExpressionString());
            }
        }
        if (onmouseout != null) {
            if (!onmouseout.isLiteralText()) {
                line.setValueExpression("onmouseout", onmouseout);
            } else {
                line.getAttributes()
                      .put("onmouseout", onmouseout.getExpressionString());
            }
        }
        if (onmouseover != null) {
            if (!onmouseover.isLiteralText()) {
                line.setValueExpression("onmouseover", onmouseover);
            } else {
                line.getAttributes()
                      .put("onmouseover", onmouseover.getExpressionString());
            }
        }
        if (onmouseup != null) {
            if (!onmouseup.isLiteralText()) {
                line.setValueExpression("onmouseup", onmouseup);
            } else {
                line.getAttributes()
                      .put("onmouseup", onmouseup.getExpressionString());
            }
        }
        if (style != null) {
            if (!style.isLiteralText()) {
                line.setValueExpression("style", style);
            } else {
                line.getAttributes().put("style", style.getExpressionString());
            }
        }
        if (x1 != null) {
            if (!x1.isLiteralText()) {
                line.setValueExpression("x1", x1);
            } else {
                line.getAttributes().put("x1", x1.getExpressionString());
            }
        }
        if (y1 != null) {
            if (!y1.isLiteralText()) {
                line.setValueExpression("y1", y1);
            } else {
                line.getAttributes().put("y1", y1.getExpressionString());
            }
        }
        if (x2 != null) {
            if (!x2.isLiteralText()) {
                line.setValueExpression("x2", x2);
            } else {
                line.getAttributes().put("x2", x2.getExpressionString());
            }
        }
        if (y2 != null) {
            if (!y2.isLiteralText()) {
                line.setValueExpression("y2", y2);
            } else {
                line.getAttributes().put("y2", y2.getExpressionString());
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
        this.onclick = null;
        this.onfocusin = null;
        this.onfocusout = null;
        this.onmousedown = null;
        this.onmousemove = null;
        this.onmouseout = null;
        this.onmouseover = null;
        this.onmouseup = null;
        this.style = null;
        this.x1 = null;
        this.y1 = null;
        this.x2 = null;
        this.y2 = null;
    }

    public String getDebugString() {
        String result =
              "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }

}
