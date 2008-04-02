/*
 * $Id: Input_TextAreaTag.java,v 1.7 2003/07/09 19:04:24 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Input_TextAreaTag.java

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;


/**
 *
 * @version $Id: Input_TextAreaTag.java,v 1.7 2003/07/09 19:04:24 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Input_TextAreaTag extends Input_TextTag
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

public Input_TextAreaTag()
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

    public String getLocalRendererType() { return "Textarea"; }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIInput inputTextArea = (UIInput) component;
	
	if (null != getRows()) {
	    inputTextArea.setAttribute("rows", getRows());
	}
	if (null != getCols()) {
	    inputTextArea.setAttribute("cols", getCols());
	}
    }
//
// Methods from TagSupport
// 


} // end of class Input_TextAreaTag
