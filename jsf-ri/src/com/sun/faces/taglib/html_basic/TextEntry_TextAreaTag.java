/*
 * $Id: TextEntry_TextAreaTag.java,v 1.29 2002/08/07 23:41:08 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextEntry_TextAreaTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
//import javax.faces.component.UITextEntry;
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
 * @version $Id: TextEntry_TextAreaTag.java,v 1.29 2002/08/07 23:41:08 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextEntry_TextAreaTag extends TextEntry_InputTag
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

    protected String rows = null;
    protected String cols = null;
    protected String wrap = null;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public TextEntry_TextAreaTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

    public void setRows(String newRows) {
	rows = newRows;
    }

    public String getRows() {
	return rows;
    }

    public void setCols(String newCols) {
	cols = newCols;
    }

    public String getCols() {
	return cols;
    }

    public void setWrap(String newWrap) {
	wrap = newWrap;
    }

    public String getWrap() {
	return wrap;
    }

//
// General Methods
//

    public String getLocalRendererType() { return "TextAreaRenderer"; }
    public UIComponent createComponent() {
//        return (new UITextEntry());
        return null;
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
/*
	UITextEntry textEntry = (UITextEntry) component;
	
	if (null == textEntry.getAttribute("rows")) {
	    textEntry.setAttribute("rows", getRows());
	}
	if (null == textEntry.getAttribute("cols")) {
	    textEntry.setAttribute("cols", getCols());
	}
	if (null == textEntry.getAttribute("wrap")) {
	    textEntry.setAttribute("wrap", getWrap());
	}
*/
    }
//
// Methods from TagSupport
// 


} // end of class TextEntry_TextAreaTag
