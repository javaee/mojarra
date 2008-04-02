/*
 * $Id: ValidateDoubleRangeTag.java,v 1.2 2003/02/20 22:49:33 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateDoubleRangeTag.java

package com.sun.faces.taglib.jsf_core;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.Validator;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>ValidateDoubleRangeTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValidateDoubleRangeTag.java,v 1.2 2003/02/20 22:49:33 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ValidateDoubleRangeTag extends MaxMinValidatorTag
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

    protected double maximum = 0;
    protected double minimum = 0;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public ValidateDoubleRangeTag()
{
    super();
    super.setType("javax.faces.validator.DoubleRangeValidator");
}

//
// Class methods
//

//
// General Methods
//

public double getMaximum()
{
    return maximum;
}

public void setMaximum(double newMaximum)
{
    maximumSet = true;
    maximum = newMaximum;
}

public double getMinimum()
{
    return minimum;
}

public void setMinimum(double newMinimum)
{
    minimumSet = true;
    minimum = newMinimum;
}

// 
// Methods from ValidatorTag
// 

protected Validator createValidator() throws JspException
{
    DoubleRangeValidator result = null;

    result = (DoubleRangeValidator) super.createValidator();
    Assert.assert_it(null != result);

    if (maximumSet) {
	result.setMaximum(getMaximum());
    }

    if (minimumSet) {
	result.setMinimum(getMinimum());
    }

    return result;
}

} // end of class ValidateDoubleRangeTag
