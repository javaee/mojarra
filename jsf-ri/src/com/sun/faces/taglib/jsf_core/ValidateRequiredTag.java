/*
 * $Id: ValidateRequiredTag.java,v 1.5 2003/12/17 15:14:14 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateRequiredTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;


//PENDING: FIX_ME - replace RequiredValidator with Validator
import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>ValidateRequiredTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValidateRequiredTag.java,v 1.5 2003/12/17 15:14:14 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ValidateRequiredTag extends ValidatorTag
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

public ValidateRequiredTag()
{
    super();
    super.setValidatorId("Required");
}

//
// Class methods
//

//
// General Methods
//

// 
// Methods from ValidatorTag
// 

} // end of class ValidateRequiredTag
