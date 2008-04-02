/*
 * $Id: Panel_GroupTag.java,v 1.10 2003/09/25 16:36:31 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.BaseComponentTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>panel_group</code> custom tag.
 */
public class Panel_GroupTag extends BaseComponentTag {

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
    
    public Panel_GroupTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    public String getRendererType() { 
        return ("Group"); 
    }

    public String getComponentType() { 
        return ("Panel"); 
    }    

}
