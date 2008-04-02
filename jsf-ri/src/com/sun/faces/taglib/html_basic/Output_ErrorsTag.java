/*
 * $Id: Output_ErrorsTag.java,v 1.3 2002/12/18 20:55:05 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_ErrorsTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 *

 *  <B>Output_ErrorsTag</B> is a convenience tag allowing content
 *  authors flexible error reporting.  See the tld for
 *  attribute description.

 *

 * @version $Id: Output_ErrorsTag.java,v 1.3 2002/12/18 20:55:05 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_ErrorsTag extends FacesTag {

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

    protected String clientId = null;
    protected String color = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public Output_ErrorsTag() {
        super();
    }

//
// Class methods
//

//
// Accessors
//

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String newClientId) {
        clientId = newClientId;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String newColor) {
        color = newColor;
    }

//
// General Methods
//

    public String getLocalRendererType() {return "Errors"; }

    public UIComponent createComponent() {
        return (new UIOutput());
    }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        UIOutput output = (UIOutput) component;

        if (null == component.getAttribute("clientId")) {
            component.setAttribute("clientId", getClientId());
        }
        if (null == component.getAttribute("color")) {
            component.setAttribute("color", getColor());
        }
    }
//
// Methods from TagSupport
//


} // end of class Output_ErrorsTag
