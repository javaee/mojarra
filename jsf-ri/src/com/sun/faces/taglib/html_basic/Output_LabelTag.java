/*
 * $Id: Output_LabelTag.java,v 1.7 2003/07/16 00:00:11 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>output_label</code> custom tag.
 */

public class Output_LabelTag extends FacesTag
{
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


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Output_LabelTag()
    {
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

    //
    // General Methods
    //

    public String getLocalRendererType() { return "Label"; }
    public String getComponentType() { return "Output"; }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);

	if (null != forValue) {
	    // PENDING(edburns): We should do something more intelligent
	    // here.  For now, however, we'll just store what we get
	    // from JSP.
	    component.setAttribute("for",forValue);
	}
    }
    
    //
    // Methods from TagSupport
    // 


} // end of class Output_LabelTag
