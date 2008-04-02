/*
 * $Id: ParameterTag.java,v 1.8 2003/09/05 14:34:53 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ParameterTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.taglib.FacesTag;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;


public class ParameterTag extends FacesTag {

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

    public String getRendererType() { return null; }
    public String getComponentType() { return "Parameter"; }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        UIParameter parameter = (UIParameter)component;

        if (name != null) {
            parameter.setName(name);
        }
        // if component has non null value, do not call setValue().
        if (value != null) {
            parameter.setValue(value);
        }
    }
}
