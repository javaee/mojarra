/*
 * $Id: ParameterTag.java,v 1.16 2005/05/05 20:51:26 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ParameterTag.java

package com.sun.faces.taglib.jsf_core;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;

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
