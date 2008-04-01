/*
 * $Id: Output_NumberTag.java,v 1.1 2002/08/13 18:29:52 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_NumberTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.servlet.ServletContext;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;

import com.sun.faces.taglib.FacesTag;
import com.sun.faces.renderkit.FormatPool;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: Output_NumberTag.java,v 1.1 2002/08/13 18:29:52 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_NumberTag extends FacesTag
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

    public Output_NumberTag()
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

    public String getLocalRendererType() { return "NumberRenderer"; }

    public UIComponent createComponent() {
        return (new UIOutput());
    }
    
     protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIOutput output = (UIOutput) component;
	
	if (null == output.getValue() && null != getValue()) {
	    output.setValue(getValue());
	}
     }   
    
    
    //
    // Methods from TagSupport
    // 


} // end of class Output_NumberTag
