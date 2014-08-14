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
import javax.faces.component.UISelectItems;
import javax.faces.webapp.UIComponentELTag;

/**
 * This class is the tag handler that evaluates the
 * <code>selectitems</code> custom tag.
 */

public class SelectItemsTag extends UIComponentELTag {

    private ValueExpression value;
    private String var;
    private ValueExpression itemValue;
    private ValueExpression itemLabel;
    private ValueExpression itemDescription;
    private ValueExpression itemDisabled;
    private ValueExpression itemLabelEscaped;
    private ValueExpression noSelectionOption;
    

    // ---------------------------------------------------------- Tag Attributes


    public void setValue(ValueExpression value) {
        this.value = value;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setItemValue(ValueExpression itemValue) {
        this.itemValue = itemValue;
    }

    public void setItemLabel(ValueExpression itemLabel) {
        this.itemLabel = itemLabel;
    }

    public void setItemDescription(ValueExpression itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemDisabled(ValueExpression itemDisabled) {
        this.itemDisabled = itemDisabled;
    }

    public void setItemLabelEscaped(ValueExpression itemLabelEscaped) {
        this.itemLabelEscaped = itemLabelEscaped;
    }

    public void setNoSelectionOption(ValueExpression noSelectionOption) {
        this.noSelectionOption = noSelectionOption;
    }
    
    // ----------------------------------------- Methods from UIComponentTagBase


    /**
     * @see javax.faces.webapp.UIComponentELTag#getRendererType()
     */
    public String getRendererType() {
        return null;
    }


    /**
     * @see javax.faces.webapp.UIComponentELTag#getComponentType()
     * @see javax.faces.component.UISelectItems#COMPONENT_TYPE
     */
    public String getComponentType() {
        return UISelectItems.COMPONENT_TYPE;
    }


    // ------------------------------------------- Methods from UIComponentELTag


    /**
     * @see javax.faces.webapp.UIComponentELTag#setProperties(javax.faces.component.UIComponent)
     * @param component
     */
    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        if (value != null) {
            component.setValueExpression("value", value);
        }
        if (var != null) {
            component.getAttributes().put("var", var);
        }
        if (itemValue != null) {
            component.setValueExpression("itemValue", itemValue);
        }
        if (itemLabel != null) {
            component.setValueExpression("itemLabel", itemLabel);
        }
        if (itemDescription != null) {
            component.setValueExpression("itemDescription", itemDescription);
        }
        if (itemDisabled != null) {
            component.setValueExpression("itemDisabled", itemDisabled);
        }
        if (itemLabelEscaped != null) {
            component.setValueExpression("itemLabelEscaped", itemLabelEscaped);
        }
        if (noSelectionOption != null) {
            component.setValueExpression("noSelectionOption", noSelectionOption);
        }

    }

} // end of class SelectItemsTag
