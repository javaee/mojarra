/*
 * $Id: SelectOne_RadioTag.java,v 1.20 2002/09/08 21:59:46 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectOne_RadioTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: SelectOne_RadioTag.java,v 1.20 2002/09/08 21:59:46 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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
    protected String border = null;


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

    public String getLayout() {
	return layout;
    }

    public void setBorder(String newBorder) {
	border = newBorder;
    }

    public String getBorder() {
	return border;
    }


//
// General Methods
//

    public String getLocalRendererType() { return "RadioRenderer"; }
    public UIComponent createComponent() {
        return (new UISelectOne());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectOne uiSelectOne = (UISelectOne) component;
	
	if (null == uiSelectOne.getAttribute("layout")) {
	    uiSelectOne.setAttribute("layout", getLayout());
	}
	if (null == uiSelectOne.getAttribute("border")) {
	    uiSelectOne.setAttribute("border", getBorder());
	}
    }
//
// Methods from TagSupport
// 


} // end of class SelectOne_RadioTag
