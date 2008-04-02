/*
 * $Id: Output_ErrorsTag.java,v 1.10 2003/07/09 19:04:25 rlubke Exp $
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

 * @version $Id: Output_ErrorsTag.java,v 1.10 2003/07/09 19:04:25 rlubke Exp $
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
    public String getComponentType() {return "Output"; }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        UIOutput output = (UIOutput) component;
        if (null != forValue) {
            component.setAttribute("for", forValue);
        }
        if (null != getColor()) {
            component.setAttribute("color", getColor());
        }
    }
    //
    // Methods from TagSupport
    //


} // end of class Output_ErrorsTag
