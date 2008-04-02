/*
 * $Id: Output_ErrorsTag.java,v 1.7 2003/03/21 18:28:47 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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

 * @version $Id: Output_ErrorsTag.java,v 1.7 2003/03/21 18:28:47 jvisvanathan Exp $
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

    protected String forValue = null;
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

    public String getFor()
    {
        return forValue;
    }

    public void setFor(String newForValue)
    {
        forValue = newForValue;
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
        if (null == component.getAttribute("for") && null != forValue) {
            component.setAttribute("for", forValue);
        }
        if (null == component.getAttribute("color")) {
            component.setAttribute("color", getColor());
        }
    }
    //
    // Methods from TagSupport
    //


} // end of class Output_ErrorsTag
