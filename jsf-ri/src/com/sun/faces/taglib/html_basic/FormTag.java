/*
 * $Id: FormTag.java,v 1.32 2002/10/22 21:26:58 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
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
 * @version $Id: FormTag.java,v 1.32 2002/10/22 21:26:58 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class FormTag extends FacesTag
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

    public String formName = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public FormTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

    public String getFormName() { return formName; }
    public void setFormName(String newFormName) { 
	formName = newFormName;
    }

//
// General Methods
//

    public String getLocalRendererType() { return "Form"; }

    public UIComponent createComponent() {
        return (new UIForm());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIForm form = (UIForm) component;

	if (null == form.getFormName()) {
	    form.setFormName(getFormName());
	}
    }
    
//
// Methods from TagSupport
// 


} // end of class FormTag
