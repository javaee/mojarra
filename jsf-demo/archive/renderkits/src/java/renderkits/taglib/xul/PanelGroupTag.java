/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
import javax.faces.webapp.UIComponentELTag;
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
            throw new IllegalStateException("Component "
                                            + component.toString()
                                            + " not expected type.  Expected: javax.faces.component.UIPanel.  Perhaps you're missing a tag?");
        }

        if (captionClass != null) {
            if (!captionClass.isLiteralText()) {
                panel.setValueExpression("captionClass", captionClass);
            } else {
                panel.getAttributes()
                      .put("captionClass", captionClass.getExpressionString());
            }
        }
        if (captionLabel != null) {
            if (!captionLabel.isLiteralText()) {
                panel.setValueExpression("captionLabel", captionLabel);
            } else {
                panel.getAttributes()
                      .put("captionLabel", captionLabel.getExpressionString());
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
        this.captionClass = null;
        this.captionLabel = null;
        this.style = null;
        this.styleClass = null;
    }

    public String getDebugString() {
        String result =
              "id: " + this.getId() + " class: " + this.getClass().getName();
        return result;
    }
}
