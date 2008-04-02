/*
 * $Id: SelectItemTag.java,v 1.12 2005/08/22 22:10:25 ofung Exp $
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

package com.sun.faces.taglib.jsf_core;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;


/**
 * This class is the tag handler that evaluates the
 * <code>selectitem</code> custom tag.
 */

public class SelectItemTag extends UIComponentELTag {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    protected ValueExpression itemValue;
    protected ValueExpression itemLabel;
    protected ValueExpression itemDescription;
    protected ValueExpression itemDisabled;
    protected ValueExpression value;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectItemTag() {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    public void setItemValue(ValueExpression value) {
        this.itemValue = value;
    }


    public void setItemLabel(ValueExpression label) {
        this.itemLabel = label;
    }


    public void setItemDescription(ValueExpression itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemDisabled(ValueExpression itemDisabled) {
        this.itemDisabled = itemDisabled;
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
        return "javax.faces.SelectItem";
    }
    
    //
    // Methods from BaseComponentTag
    //

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        UISelectItem selectItem = (UISelectItem) component;

        if (null != value) {
            if (!value.isLiteralText()) {
                selectItem.setValueExpression("value", value);
            } else {
                selectItem.setValue(value.getExpressionString());
            }
        }

        if (null != itemValue) {
            if (!itemValue.isLiteralText()) {
                selectItem.setValueExpression("itemValue", itemValue);
            } else {
                selectItem.setItemValue(itemValue.getExpressionString());
            }
        }
        if (null != itemLabel) {
            if (!itemLabel.isLiteralText()) {
                selectItem.setValueExpression("itemLabel", itemLabel);
            } else {
                selectItem.setItemLabel(itemLabel.getExpressionString());
            }
        }
        if (null != itemDescription) {
            if (!itemDescription.isLiteralText()) {
                selectItem.setValueExpression("itemDescription",
                                              itemDescription);
            } else {
                selectItem.setItemDescription(
                    itemDescription.getExpressionString());
            }
        }
        if (null != itemDisabled) {
            if (!itemDisabled.isLiteralText()) {
                selectItem.setValueExpression("itemDisabled", itemDisabled);
            } else {
                selectItem.setItemDisabled(
                    Boolean.valueOf(itemDisabled.getExpressionString()).
                        booleanValue());
            }
        }

    }

} // end of class SelectItemTag
