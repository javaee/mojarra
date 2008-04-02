/*
 * $Id: ParameterTag.java,v 1.15 2004/02/26 20:33:18 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ParameterTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;

public class ParameterTag extends BaseComponentTag {

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
            if (isValueReference(name)) {
                component.setValueBinding("name",
                                          Util.getValueBinding(name));
            } else {
                parameter.setName(name);
            }
        }
        // if component has non null value, do not call setValue().
        if (value != null) {
            if (isValueReference(value)) {
                component.setValueBinding("value",
                                          Util.getValueBinding(value));
            } else {
                parameter.setValue(value);
            }
        }
    }
}
