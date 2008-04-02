/*
 * $Id: SubviewTag.java,v 1.2 2004/01/27 21:04:43 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import javax.faces.webapp.UIComponentTag;

public class SubviewTag extends UIComponentTag {

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

    public SubviewTag() {
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
        return "javax.faces.NamingContainer"; 
    }

}
