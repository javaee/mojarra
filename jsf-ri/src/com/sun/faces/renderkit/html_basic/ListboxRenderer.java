/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * $Id: ListboxRenderer.java,v 1.16 2003/11/01 02:52:49 jvisvanathan Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */


package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;

/**
 * <B>ListRenderer</B> is a class that renders the current value of 
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of 
 * options.
 */

public class ListboxRenderer extends MenuRenderer {
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

    public ListboxRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //
    protected void getDisplaySize(int itemCount, UIComponent component) {
        // Listbox will display all items in the list, so override  size
        // attribute is specified.
        component.getAttributes().put("size", new Integer(itemCount));
    }

} // end of class ListboxRenderer
