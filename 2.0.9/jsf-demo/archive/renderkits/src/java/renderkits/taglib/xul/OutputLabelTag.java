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

package renderkits.taglib.xul;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;

/*
* ******* GENERATED CODE - DO NOT EDIT *******
*/


public final class OutputLabelTag extends UIComponentELTag {


    // Setter Methods
    // PROPERTY: converter
    private javax.el.ValueExpression converter;

    public void setConverter(javax.el.ValueExpression converter) {
        this.converter = converter;
    }

    // PROPERTY: value
    private javax.el.ValueExpression value;

    public void setValue(javax.el.ValueExpression value) {
        this.value = value;
    }

    // PROPERTY: accesskey
    private javax.el.ValueExpression accesskey;

    public void setAccesskey(javax.el.ValueExpression accesskey) {
        this.accesskey = accesskey;
    }

    // PROPERTY: boxClass 
    private javax.el.ValueExpression boxClass;

    public void setBoxClass(javax.el.ValueExpression boxClass) {
        this.boxClass = boxClass;
    }

    // PROPERTY: boxStyle
    private javax.el.ValueExpression boxStyle;

    public void setBoxStyle(javax.el.ValueExpression boxStyle) {
        this.boxStyle = boxStyle;
    }

    // PROPERTY: escape
    private javax.el.ValueExpression escape;

    public void setEscape(javax.el.ValueExpression escape) {
        this.escape = escape;
    }

    // PROPERTY: for
    private javax.el.ValueExpression _for;

    public void setFor(javax.el.ValueExpression _for) {
        this._for = _for;
    }

    // PROPERTY: onblur
    private javax.el.ValueExpression onblur;

    public void setOnblur(javax.el.ValueExpression onblur) {
        this.onblur = onblur;
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

    // PROPERTY: onfocus
    private javax.el.ValueExpression onfocus;

    public void setOnfocus(javax.el.ValueExpression onfocus) {
        this.onfocus = onfocus;
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

    // PROPERTY: pack 
    private javax.el.ValueExpression pack;

    public void setPack(javax.el.ValueExpression pack) {
        this.pack = pack;
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
        return "renderkit.xul.Label";
    }

    public String getComponentType() {
        return "javax.faces.Output";
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        javax.faces.component.UIOutput output = null;
        try {
            output = (javax.faces.component.UIOutput) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component "
                                            + component.toString()
                                            + " not expected type.  Expected: javax.faces.component.UIOutput.  Perhaps you're missing a tag?");
        }

        if (converter != null) {
            if (!converter.isLiteralText()) {
                output.setValueExpression("converter", converter);
            } else {
                Converter conv = FacesContext.getCurrentInstance()
                      .getApplication()
                      .createConverter(converter.getExpressionString());
                output.setConverter(conv);
            }
        }

        if (value != null) {
            if (!value.isLiteralText()) {
                output.setValueExpression("value", value);
            } else {
                output.setValue(value.getExpressionString());
            }
        }

        if (accesskey != null) {
            if (!accesskey.isLiteralText()) {
                output.setValueExpression("accesskey", accesskey);
            } else {
                output.getAttributes()
                      .put("accesskey", accesskey.getExpressionString());
            }
        }
        if (boxClass != null) {
            if (!boxClass.isLiteralText()) {
                output.setValueExpression("boxClass", boxClass);
            } else {
                output.getAttributes()
                      .put("boxClass", boxClass.getExpressionString());
            }
        }
        if (boxStyle != null) {
            if (!boxStyle.isLiteralText()) {
                output.setValueExpression("boxStyle", boxStyle);
            } else {
                output.getAttributes()
                      .put("boxStyle", boxStyle.getExpressionString());
            }
        }
        if (escape != null) {
            if (!escape.isLiteralText()) {
                output.setValueExpression("escape", escape);
            } else {
                output.getAttributes().put("escape",
                                           java.lang.Boolean.valueOf(escape.getExpressionString()));
            }
        }
        if (_for != null) {
            if (!_for.isLiteralText()) {
                output.setValueExpression("__for", _for);
            } else {
                output.getAttributes().put("for", _for.getExpressionString());
            }
        }
        if (onblur != null) {
            if (!onblur.isLiteralText()) {
                output.setValueExpression("onblur", onblur);
            } else {
                output.getAttributes()
                      .put("onblur", onblur.getExpressionString());
            }
        }
        if (onclick != null) {
            if (!onclick.isLiteralText()) {
                output.setValueExpression("onclick", onclick);
            } else {
                output.getAttributes()
                      .put("onclick", onclick.getExpressionString());
            }
        }
        if (ondblclick != null) {
            if (!ondblclick.isLiteralText()) {
                output.setValueExpression("ondblclick", ondblclick);
            } else {
                output.getAttributes()
                      .put("ondblclick", ondblclick.getExpressionString());
            }
        }
        if (onfocus != null) {
            if (!onfocus.isLiteralText()) {
                output.setValueExpression("onfocus", onfocus);
            } else {
                output.getAttributes()
                      .put("onfocus", onfocus.getExpressionString());
            }
        }
        if (onkeydown != null) {
            if (!onkeydown.isLiteralText()) {
                output.setValueExpression("onkeydown", onkeydown);
            } else {
                output.getAttributes()
                      .put("onkeydown", onkeydown.getExpressionString());
            }
        }
        if (onkeypress != null) {
            if (!onkeypress.isLiteralText()) {
                output.setValueExpression("onkeypress", onkeypress);
            } else {
                output.getAttributes()
                      .put("onkeypress", onkeypress.getExpressionString());
            }
        }
        if (onkeyup != null) {
            if (!onkeyup.isLiteralText()) {
                output.setValueExpression("onkeyup", onkeyup);
            } else {
                output.getAttributes()
                      .put("onkeyup", onkeyup.getExpressionString());
            }
        }
        if (onmousedown != null) {
            if (!onmousedown.isLiteralText()) {
                output.setValueExpression("onmousedown", onmousedown);
            } else {
                output.getAttributes()
                      .put("onmousedown", onmousedown.getExpressionString());
            }
        }
        if (onmousemove != null) {
            if (!onmousemove.isLiteralText()) {
                output.setValueExpression("onmousemove", onmousemove);
            } else {
                output.getAttributes()
                      .put("onmousemove", onmousemove.getExpressionString());
            }
        }
        if (onmouseout != null) {
            if (!onmouseout.isLiteralText()) {
                output.setValueExpression("onmouseout", onmouseout);
            } else {
                output.getAttributes()
                      .put("onmouseout", onmouseout.getExpressionString());
            }
        }
        if (onmouseover != null) {
            if (!onmouseover.isLiteralText()) {
                output.setValueExpression("onmouseover", onmouseover);
            } else {
                output.getAttributes()
                      .put("onmouseover", onmouseover.getExpressionString());
            }
        }
        if (onmouseup != null) {
            if (!onmouseup.isLiteralText()) {
                output.setValueExpression("onmouseup", onmouseup);
            } else {
                output.getAttributes()
                      .put("onmouseup", onmouseup.getExpressionString());
            }
        }
        if (pack != null) {
            if (!pack.isLiteralText()) {
                output.setValueExpression("pack", pack);
            } else {
                output.getAttributes().put("pack", pack.getExpressionString());
            }
        }
        if (style != null) {
            if (!style.isLiteralText()) {
                output.setValueExpression("style", style);
            } else {
                output.getAttributes()
                      .put("style", style.getExpressionString());
            }
        }
        if (styleClass != null) {
            if (!styleClass.isLiteralText()) {
                output.setValueExpression("styleClass", styleClass);
            } else {
                output.getAttributes()
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
        this.converter = null;
        this.value = null;

        // rendered attributes
        this.boxClass = null;
        this.boxStyle = null;
        this.escape = null;
        this._for = null;
        this.onblur = null;
        this.onclick = null;
        this.ondblclick = null;
        this.onfocus = null;
        this.onkeydown = null;
        this.onkeypress = null;
        this.onkeyup = null;
        this.onmousedown = null;
        this.onmousemove = null;
        this.onmouseout = null;
        this.onmouseover = null;
        this.onmouseup = null;
        this.pack = null;
        this.style = null;
        this.styleClass = null;
    }

    public String getDebugString() {
        String result =
              "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }

}
