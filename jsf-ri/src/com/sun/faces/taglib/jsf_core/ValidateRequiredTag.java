/*
 * $Id: ValidateRequiredTag.java,v 1.1 2002/09/20 00:59:47 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateRequiredTag.java

package com.sun.faces.taglib.jsf_core;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.validator.RequiredValidator;
import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>ValidateRequiredTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValidateRequiredTag.java,v 1.1 2002/09/20 00:59:47 eburns Exp $
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
    super.setType("javax.faces.validator.RequiredValidator");
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
