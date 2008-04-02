/*
 * $Id: ValidateLengthTag.java,v 1.3 2003/05/20 23:08:00 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateLengthTag.java

package com.sun.faces.taglib.jsf_core;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>ValidateLengthTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValidateLengthTag.java,v 1.3 2003/05/20 23:08:00 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ValidateLengthTag extends MaxMinValidatorTag
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

    protected int maximum = 0;
    protected int minimum = 0;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public ValidateLengthTag()
{
    super();
    super.setId("Length");
}

//
// Class methods
//

//
// General Methods
//

public int getMaximum()
{
    return maximum;
}

public void setMaximum(int newMaximum)
{
    maximumSet = true;
    maximum = newMaximum;
}

public int getMinimum()
{
    return minimum;
}

public void setMinimum(int newMinimum)
{
    minimumSet = true;
    minimum = newMinimum;
}

// 
// Methods from ValidatorTag
// 

protected Validator createValidator() throws JspException
{
    LengthValidator result = null;

    result = (LengthValidator) super.createValidator();
    Assert.assert_it(null != result);

    if (maximumSet) {
	result.setMaximum(getMaximum());
    }

    if (minimumSet) {
	result.setMinimum(getMinimum());
    }

    return result;
}

} // end of class ValidateLengthTag
