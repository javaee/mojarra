/*
 * $Id: ValidateRequiredTag.java,v 1.4 2003/06/26 19:08:43 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateRequiredTag.java

package com.sun.faces.taglib.jsf_core;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

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
 * @version $Id: ValidateRequiredTag.java,v 1.4 2003/06/26 19:08:43 horwat Exp $
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
    super.setId("Required");
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
