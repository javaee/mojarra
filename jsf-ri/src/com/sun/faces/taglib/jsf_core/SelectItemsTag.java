/*
 * $Id: SelectItemsTag.java,v 1.10 2006/03/29 22:38:41 rlubke Exp $
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
import javax.faces.component.UISelectItems;
import javax.faces.webapp.UIComponentELTag;

/**
 * This class is the tag handler that evaluates the
 * <code>selectitems</code> custom tag.
 */

public class SelectItemsTag extends UIComponentELTag {


    private ValueExpression value;

    // ------------------------------------------------------------ Constructors


    public SelectItemsTag() {

        super();

    }

    // ---------------------------------------------------------- Public Methods


    public void setValue(ValueExpression value) {

        this.value = value;

    }


    public String getComponentType() {

        return "javax.faces.SelectItems";

    }


    public String getRendererType() {

        return null;

    }

    // ------------------------------------------------------- Protected Methods


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
