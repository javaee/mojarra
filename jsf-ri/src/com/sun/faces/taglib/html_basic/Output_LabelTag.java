/*
 * $Id: Output_LabelTag.java,v 1.5 2003/07/07 20:53:04 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_LabelTag.java

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
 * @version $Id: Output_LabelTag.java,v 1.5 2003/07/07 20:53:04 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_LabelTag extends FacesTag
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

    protected String forValue = null;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public Output_LabelTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

public String getFor()
{
    return forValue;
}

public void setFor(String newForValue)
{
    forValue = newForValue;
}

//
// General Methods
//

    public String getLocalRendererType() { return "Label"; }
    public String getComponentType() { return "Output"; }

    public UIComponent createComponent() {
        return (new UIOutput());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);

	if (null != getFor()) {
	    // PENDING(edburns): We should do something more intelligent
	    // here.  For now, however, we'll just store what we get
	    // from JSP.
	    component.setAttribute("for", getFor());
	}
    }
    
//
// Methods from TagSupport
// 


} // end of class Output_LabelTag
