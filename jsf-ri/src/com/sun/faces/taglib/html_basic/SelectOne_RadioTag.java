/*
 * $Id: SelectOne_RadioTag.java,v 1.33 2003/10/07 13:05:36 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;

/**
 * This class is the tag handler that evaluates the 
 * <code>selectone_radio</code> custom tag.
 */

public class SelectOne_RadioTag extends SelectOne_ListboxTag
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

    protected String layout = null;


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectOne_RadioTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    public void setLayout(String newLayout) {
	layout = newLayout;
    }

    //
    // General Methods
    //

    public String getRendererType() { 
        return "RadioButtonList"; 
    }
    public String getComponentType() { 
        return "SelectOneRadioButtonList"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectOne uiSelectOne = (UISelectOne) component;
	
        if (null != layout) {
	    uiSelectOne.getAttributes().put("layout", layout);
	}
        if (border != Integer.MIN_VALUE) {
	    uiSelectOne.getAttributes().put("border", new Integer(border));
	}
    }
    //
    // Methods from TagSupport
    // 


} // end of class SelectOne_RadioTag
