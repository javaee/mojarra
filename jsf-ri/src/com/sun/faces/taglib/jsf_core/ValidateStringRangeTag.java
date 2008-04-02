/*
 * $Id: ValidateStringRangeTag.java,v 1.2 2003/02/20 22:49:33 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateStringRangeTag.java

package com.sun.faces.taglib.jsf_core;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.validator.StringRangeValidator;
import javax.faces.validator.Validator;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>ValidateStringRangeTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValidateStringRangeTag.java,v 1.2 2003/02/20 22:49:33 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ValidateStringRangeTag extends MaxMinValidatorTag
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

    protected String maximum = null;
    protected String minimum = null;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public ValidateStringRangeTag()
{
    super();
    super.setType("javax.faces.validator.StringRangeValidator");
}

//
// Class methods
//

//
// General Methods
//

public String getMaximum()
{
    return maximum;
}

public void setMaximum(String newMaximum)
{
    maximumSet = true;
    maximum = newMaximum;
}

public String getMinimum()
{
    return minimum;
}

public void setMinimum(String newMinimum)
{
    minimumSet = true;
    minimum = newMinimum;
}

// 
// Methods from ValidatorTag
// 

protected Validator createValidator() throws JspException
{
    StringRangeValidator result = null;

    result = (StringRangeValidator) super.createValidator();
    Assert.assert_it(null != result);

    if (maximumSet) {
	result.setMaximum(getMaximum());
    }

    if (minimumSet) {
	result.setMinimum(getMinimum());
    }

    return result;
}

} // end of class ValidateStringRangeTag
