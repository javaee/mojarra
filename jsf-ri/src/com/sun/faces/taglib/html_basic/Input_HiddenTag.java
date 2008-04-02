/*
 * $Id: Input_HiddenTag.java,v 1.1 2002/10/31 17:59:18 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Input_HiddenTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
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
 * @version $Id: Input_HiddenTag.java,v 1.1 2002/10/31 17:59:18 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Input_HiddenTag extends FacesTag
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

    private String converter = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Input_HiddenTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    public void setConverter(String converter) {
        this.converter = converter;
    }

    //
    // General Methods
    //

    public String getLocalRendererType() { return "Hidden"; }

    public UIComponent createComponent() {
        return (new UIInput());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	if (null == component.getValue()) {
	    component.setValue(getValue());
	}
        if ((converter != null) &&
            (component.getAttribute("converter") == null)) {
            component.setAttribute("converter", converter);
        }
    }
    
//
// Methods from TagSupport
// 


} // end of class Input_HiddenTag
