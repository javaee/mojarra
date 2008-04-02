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


public final class PanelGroupTag extends UIComponentELTag {


    // PROPERTY: captionClass 
    private javax.el.ValueExpression captionClass;
    public void setCaptionClass(javax.el.ValueExpression captionClass) {
        this.captionClass = captionClass;
    }

    // PROPERTY: captionLabel 
    private javax.el.ValueExpression captionLabel;
    public void setCaptionLabel(javax.el.ValueExpression captionLabel) {
        this.captionLabel = captionLabel;
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
        return "renderkit.xul.Group";
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
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: javax.faces.component.UIPanel.  Perhaps you're missing a tag?");
        }

        if (captionClass != null) {
            if (!captionClass.isLiteralText()) {
                panel.setValueExpression("captionClass", captionClass);
            } else {
                panel.getAttributes().put("captionClass", captionClass.getExpressionString());
            }
        }
        if (captionLabel != null) {
            if (!captionLabel.isLiteralText()) {
                panel.setValueExpression("captionLabel", captionLabel);
            } else {
                panel.getAttributes().put("captionLabel", captionLabel.getExpressionString());
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
                panel.getAttributes().put("styleClass", styleClass.getExpressionString());
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
        this.captionClass = null;
        this.captionLabel = null;
        this.style = null;
        this.styleClass = null;
    }

    public String getDebugString() {
        String result = "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }
}
