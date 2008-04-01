/*
 * $Id: TextEntry_SecretTag.java,v 1.26 2002/08/07 23:41:07 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextEntry_SecretTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
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
 * @version $Id: TextEntry_SecretTag.java,v 1.26 2002/08/07 23:41:07 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextEntry_SecretTag extends TextEntry_InputTag
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

public TextEntry_SecretTag()
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

    public String getLocalRendererType() { return "SecretRenderer"; }

//
// Methods from TagSupport
// 


} // end of class TextEntry_SecretTag
