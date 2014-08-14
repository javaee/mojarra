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
    protected ValueExpression noSelectionOption = null;

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

    public void setNoSelectionOption(ValueExpression noSelectionOption) {
        this.noSelectionOption = noSelectionOption;
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
        if (null != noSelectionOption) {
            if (!noSelectionOption.isLiteralText()) {
                selectItem.setValueExpression("noSelectionOption", noSelectionOption);
            } else {
                selectItem.setNoSelectionOption(
                    Boolean.valueOf(noSelectionOption.getExpressionString()).
                        booleanValue());
            }
        }
        if (null != escape) {
            if (!escape.isLiteralText()) {
                selectItem.setValueExpression("escape", escape);
            } else {
                selectItem.setItemEscaped(
                    Boolean.valueOf(escape.getExpressionString()).
                        booleanValue());            }
        }

    }

    /**
     * Holds value of property escape.
     */
    private ValueExpression escape;

    /**
     * Getter for property escape.
     * @return Value of property escape.
     */
    public ValueExpression getEscape() {
        return this.escape;
    }

    /**
     * Setter for property escape.
     * @param escape New value of property escape.
     */
    public void setEscape(ValueExpression escape) {
        this.escape = escape;
    }

} // end of class SelectItemTag
