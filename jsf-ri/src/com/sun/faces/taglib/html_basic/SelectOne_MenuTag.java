/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


/**
 * $Id: SelectOne_MenuTag.java,v 1.7 2003/07/09 19:04:27 rlubke Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */


// SelectMany_MenuTag.java

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: SelectOne_MenuTag.java,v 1.7 2003/07/09 19:04:27 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 * 
 *
 */

public class SelectOne_MenuTag extends FacesTag
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

public SelectOne_MenuTag()
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

    public String getLocalRendererType() { return "Menu"; } 
    public String getComponentType() { return "SelectOne"; }

//
// Methods from TagSupport
// 

    public int doEndTag() throws JspException {
	UISelectOne component = (UISelectOne) getComponent();

	int rc = super.doEndTag();

	return rc;
    }


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);

        if (null != getSize()) {
            component.setAttribute("size", getSize());
        }
        if (null != getOnselect()) {
            component.setAttribute("onselect", getOnselect());
        }
        if (null != getOnchange()) {
            component.setAttribute("onchange", getOnchange());
        }
    }

} // end of class SelectMany_MenuTag
