/*
 * $Id: Panel_GroupTag.java,v 1.3 2003/02/20 22:49:18 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import com.sun.faces.taglib.FacesTag;
import javax.servlet.jsp.JspException;

/**
 *
 * @version $Id: Panel_GroupTag.java,v 1.3 2003/02/20 22:49:18 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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
    
    //
    // Methods from FacesTag
    //
    public UIComponent createComponent() {
        return (new UIPanel());
    }
    
    public String getLocalRendererType() {
         return ("Group");
    }    

}
