/*
 * $Id: SelectItemsTag.java,v 1.10 2003/09/25 16:36:32 rlubke Exp $
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
 * <code>selectitems</code> custom tag.
 */

public class SelectItemsTag extends BaseComponentTag
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

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectItemsTag()
    {
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
        return "SelectItems"; 
    }


    //
    // Methods from BaseComponentTag
    //
    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
    }

} // end of class SelectItemsTag
