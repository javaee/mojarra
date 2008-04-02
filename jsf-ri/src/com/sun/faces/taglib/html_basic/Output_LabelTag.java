/*
 * $Id: Output_LabelTag.java,v 1.12 2003/09/25 16:36:30 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;

import com.sun.faces.taglib.BaseComponentTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>output_label</code> custom tag.
 */

public class Output_LabelTag extends BaseComponentTag
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

    public void setFor(String newForValue)
    {
        forValue = newForValue;
    }

    //
    // General Methods
    //

    public String getRendererType() { return "Label"; }
    public String getComponentType() { return "Output"; }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);

	if (null != forValue) {
	    // PENDING(edburns): We should do something more intelligent
	    // here.  For now, however, we'll just store what we get
	    // from JSP.
	    component.getAttributes().put("for",forValue);
	}
    }
    
    //
    // Methods from TagSupport
    // 


} // end of class Output_LabelTag
