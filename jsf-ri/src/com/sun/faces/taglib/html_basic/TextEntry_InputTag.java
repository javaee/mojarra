/*
 * $Id: TextEntry_InputTag.java,v 1.33 2002/07/12 19:44:38 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextEntry_InputTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UITextEntry;
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
 * @version $Id: TextEntry_InputTag.java,v 1.33 2002/07/12 19:44:38 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextEntry_InputTag extends FacesTag
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

    protected String size = null;
    protected String maxlength = null;
    protected String text = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public TextEntry_InputTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

    public void setSize(String newSize) {
	size = newSize;
    }

    public String getSize() {
	return size;
    }

    public void setMaxlength(String newMaxlength) {
	maxlength = newMaxlength;
    }

    public String getMaxlength() {
	return maxlength;
    }

    public void setText(String newText) {
	text = newText;
    }

    public String getText() {
	return text;
    }

//
// General Methods
//

    public String getLocalRendererType() { return "InputRenderer"; }

    public UIComponent createComponent() {
        return (new UITextEntry());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UITextEntry textEntry = (UITextEntry) component;
	
	if (null == textEntry.getText()) {
	    textEntry.setText(getText());
	}
	if (null == textEntry.getAttribute("size")) {
	    textEntry.setAttribute("size", getSize());
	}
	if (null == textEntry.getAttribute("maxlength")) {
	    textEntry.setAttribute("maxlength", getMaxlength());
	}
    }

    
//
// Methods from TagSupport
//
    


} // end of class TextEntry_InputTag
