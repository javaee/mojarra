/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


/**
 * $Id: SelectMany_MenuTag.java,v 1.10 2003/08/15 19:15:18 rlubke Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>selectmany_menu</code> custom tag.
 */

public class SelectMany_MenuTag extends FacesTag
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

    public SelectMany_MenuTag()
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
        return "Menu"; 
    } 
    public String getComponentType() { 
        return "SelectMany"; 
    }

    //
    // Methods from TagSupport
    // 

    public int doEndTag() throws JspException {
	UISelectMany component = (UISelectMany) getComponent();
	int rc = super.doEndTag();
	return rc;
    }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);

        if (null != size) {
            component.setAttribute("size", size);
        }
        if (null != onselect) {
            component.setAttribute("onselect", onselect);
        }
        if (null != onchange) {
            component.setAttribute("onchange", onchange);
        }
    }

} // end of class SelectMany_MenuTag
