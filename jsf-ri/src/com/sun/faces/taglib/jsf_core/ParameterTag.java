/*
 * $Id: ParameterTag.java,v 1.20 2006/09/01 01:23:04 tony_robertson Exp $
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

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    private ValueExpression name;
    private ValueExpression value;

// Attribute Instance Variables


// Relationship Instance Variables

//
// Constructors and Initializers
//

    public ParameterTag() {
        super();
    }

//
// Class methods
//

//
// Accessors
//
    public void setName(ValueExpression name) {
        this.name = name;
    }


    public void setValue(ValueExpression value) {
        this.value = value;
    }

//
// General Methods
//

    public String getRendererType() {
        return null;
    }


    public String getComponentType() {
        return "javax.faces.Parameter";
    }


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
