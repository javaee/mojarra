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


/*
 * ******* GENERATED CODE - DO NOT EDIT *******
 */


public final class OutputTextTag extends UIComponentELTag {


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

    // PROPERTY: escape
    private javax.el.ValueExpression escape;
    public void setEscape(javax.el.ValueExpression escape) {
        this.escape = escape;
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

    // PROPERTY: textAnchor 
    private javax.el.ValueExpression textAnchor;
    public void setTextAnchor(javax.el.ValueExpression textAnchor) {
        this.textAnchor =  textAnchor;
    }

    // PROPERTY: rotate 
    private javax.el.ValueExpression rotate;
    public void setRotate(javax.el.ValueExpression rotate) {
        this.rotate = rotate;
    }

    // PROPERTY: textLength 
    private javax.el.ValueExpression textLength;
    public void setTextLength(javax.el.ValueExpression textLength) {
        this.textLength = textLength;
    }

    // PROPERTY: textAdjust 
    private javax.el.ValueExpression textAdjust;
    public void setTextAdjust(javax.el.ValueExpression textAdjust) {
        this.textAdjust = textAdjust;
    }

    // General Methods
    public String getRendererType() {
        return "renderkit.svg.Text";
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
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: javax.faces.component.UIOutput.  Perhaps you're missing a tag?");
        }

        if (converter != null) {
            if (!converter.isLiteralText()) {
                output.setValueExpression("converter", converter);
            } else {
                Converter conv = FacesContext.getCurrentInstance().getApplication().createConverter(converter.getExpressionString());
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

        if (escape != null) {
            if (!escape.isLiteralText()) {
                output.setValueExpression("escape", escape);
            } else {
                output.getAttributes().put("escape", java.lang.Boolean.valueOf(escape.getExpressionString()));
            }
        }
        if (style != null) {
            if (!style.isLiteralText()) {
                output.setValueExpression("style", style);
            } else {
                output.getAttributes().put("style", style.getExpressionString());
            }
        }
        if (styleClass != null) {
            if (!styleClass.isLiteralText()) {
                output.setValueExpression("styleClass", styleClass);
            } else {
                output.getAttributes().put("styleClass", styleClass.getExpressionString());
            }
        }
        if (x != null) {
            if (!x.isLiteralText()) {
                output.setValueExpression("x", x);
            } else {
                output.getAttributes().put("x", x.getExpressionString());
            }
        }
        if (y != null) {
            if (!y.isLiteralText()) {
                output.setValueExpression("y", y);
            } else {
                output.getAttributes().put("y", y.getExpressionString());
            }
        }
        if (dx != null) {
            if (!dx.isLiteralText()) {
                output.setValueExpression("dx", dx);
            } else {
                output.getAttributes().put("dx", dx.getExpressionString());
            }
        }
        if (dy != null) {
            if (!dy.isLiteralText()) {
                output.setValueExpression("dy", dy);
            } else {
                output.getAttributes().put("dy", dy.getExpressionString());
            }
        }
        if (textAnchor != null) {
            if (!textAnchor.isLiteralText()) {
                output.setValueExpression("textAnchor", textAnchor);
            } else {
                output.getAttributes().put("textAnchor", textAnchor.getExpressionString());
            }
        }
        if (rotate != null) {
            if (!rotate.isLiteralText()) {
                output.setValueExpression("rotate", rotate);
            } else {
                output.getAttributes().put("rotate", rotate.getExpressionString());
            }
        }
        if (textLength != null) {
            if (!textLength.isLiteralText()) {
                output.setValueExpression("textLength", textLength);
            } else {
                output.getAttributes().put("textLength", textLength.getExpressionString());
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
        this.escape = null;
        this.style = null;
        this.styleClass = null;
    }

    public String getDebugString() {
        String result = "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }

}
