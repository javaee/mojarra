/*
 * $Id: SelectItemsTag.java,v 1.8 2005/05/05 20:51:27 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;

/**
 * This class is the tag handler that evaluates the
 * <code>selectitems</code> custom tag.
 */

public class SelectItemsTag extends UIComponentELTag {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private ValueExpression value;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectItemsTag() {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

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
        return "javax.faces.SelectItems";
    }


    //
    // Methods from BaseComponentTag
    //
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        
        if (null != value) {
            if (!value.isLiteralText()) {
                component.setValueExpression("value", value);
            } else {
                ((UISelectItems) component).setValue(
                    value.getExpressionString());
            }
        }
    }

} // end of class SelectItemsTag
