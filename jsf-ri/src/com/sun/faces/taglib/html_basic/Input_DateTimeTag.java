/*
 * $Id: Input_DateTimeTag.java,v 1.1 2002/08/22 00:09:15 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Input_DateTimeTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

/**
 *
 * @version $Id: Input_DateTimeTag.java,v 1.1 2002/08/22 00:09:15 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Input_DateTimeTag extends InputTag
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

public Input_DateTimeTag()
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

    public String getLocalRendererType() { return "DateTimeRenderer"; }

    public UIComponent createComponent() {
        return (new UIInput());
    }

//
// Methods from TagSupport
//
    


} // end of class Input_DateTimeTag
