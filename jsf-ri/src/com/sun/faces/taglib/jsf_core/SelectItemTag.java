/*
 * $Id: SelectItemTag.java,v 1.14 2006/03/29 22:38:41 rlubke Exp $
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


/**
 * This class is the tag handler that evaluates the
 * <code>selectitem</code> custom tag.
 */

public class SelectItemTag extends UIComponentELTag {


    protected ValueExpression itemDescription;
    protected ValueExpression itemDisabled;
    protected ValueExpression itemLabel;
    protected ValueExpression itemValue;
    protected ValueExpression value;

    /** Holds value of property escape. */
    private ValueExpression escape;

    // ------------------------------------------------------------ Constructors


    public SelectItemTag() {

        super();

    }

    // ---------------------------------------------------------- Public Methods


    /**
     * Getter for property escape.
     *
     * @return Value of property escape.
     */
    public ValueExpression getEscape() {

        return this.escape;

    }


    /**
     * Setter for property escape.
     *
     * @param escape New value of property escape.
     */
    public void setEscape(ValueExpression escape) {

        this.escape = escape;

    }


    public void setItemDescription(ValueExpression itemDescription) {

        this.itemDescription = itemDescription;

    }


    public void setItemDisabled(ValueExpression itemDisabled) {

        this.itemDisabled = itemDisabled;

    }


    public void setItemLabel(ValueExpression label) {

        this.itemLabel = label;

    }


    public void setItemValue(ValueExpression value) {

        this.itemValue = value;

    }


    public void setValue(ValueExpression value) {

        this.value = value;

    }


    public String getComponentType() {

        return "javax.faces.SelectItem";

    }


    public String getRendererType() {

        return null;

    }

    // ------------------------------------------------------- Protected Methods


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
        if (null != escape) {
            if (!escape.isLiteralText()) {
                selectItem.setValueExpression("escape", escape);
            } else {
                selectItem.setItemEscaped(
                      Boolean.valueOf(escape.getExpressionString()).
                            booleanValue());
            }
        }

    }

} // end of class SelectItemTag
