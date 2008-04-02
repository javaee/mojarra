/*
 * $Id: ParameterTag.java,v 1.18 2006/03/29 22:38:41 rlubke Exp $
 */

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

// ParameterTag.java

package com.sun.faces.taglib.jsf_core;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.webapp.UIComponentELTag;

public class ParameterTag extends UIComponentELTag {


    private ValueExpression name;
    private ValueExpression value;

    // ------------------------------------------------------------ Constructors


    public ParameterTag() {

        super();

    }

    // ---------------------------------------------------------- Public Methods


    public void setName(ValueExpression name) {

        this.name = name;

    }


    public void setValue(ValueExpression value) {

        this.value = value;

    }


    public String getComponentType() {

        return "javax.faces.Parameter";

    }


    public String getRendererType() {

        return null;

    }

    // ------------------------------------------------------- Protected Methods


    protected void setProperties(UIComponent component) {

        super.setProperties(component);
        UIParameter parameter = (UIParameter) component;

        if (name != null) {
            if (!name.isLiteralText()) {
                parameter.setValueExpression("name", name);
            } else {
                parameter.setName(name.getExpressionString());
            }
        }
        // if component has non null value, do not call setValue().
        if (value != null) {
            if (!value.isLiteralText()) {
                component.setValueExpression("value", value);
            } else {
                parameter.setValue(value.getExpressionString());
            }
        }

    }

}
