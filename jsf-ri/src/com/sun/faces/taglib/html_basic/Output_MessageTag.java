/*
 * $Id: Output_MessageTag.java,v 1.6 2003/04/29 20:52:07 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_MessageTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
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
 * @version $Id: Output_MessageTag.java,v 1.6 2003/04/29 20:52:07 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_MessageTag extends FacesTag
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

public Output_MessageTag()
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

    public String getLocalRendererType() { return "Message"; }
    public String getComponentType() { return "Output"; }

    public UIComponent createComponent() {
        return (new UIOutput());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIOutput textOutput = (UIOutput) component;
        // if component has non null value, do not call setValue().	
	if (null == textOutput.getValue()) {
	    textOutput.setValue(getValue());
	}
    }
    
//
// Methods from TagSupport
// 


} // end of class Output_MessageTag
