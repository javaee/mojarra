/*
 * $Id: SelectOne_RadioGroupTag.java,v 1.5 2002/08/21 19:26:05 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectOne_RadioGroupTag.java

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
 * @version $Id: SelectOne_RadioGroupTag.java,v 1.5 2002/08/21 19:26:05 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectOne_RadioGroupTag extends SelectOne_OptionListTag
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

    public SelectOne_RadioGroupTag()
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


} // end of class SelectOne_RadioGroupTag
