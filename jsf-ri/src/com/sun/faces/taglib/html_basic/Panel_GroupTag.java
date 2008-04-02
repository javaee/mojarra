/*
 * $Id: Panel_GroupTag.java,v 1.9 2003/09/08 20:10:15 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>panel_group</code> custom tag.
 */
public class Panel_GroupTag extends FacesTag {

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
