/*
 * $Id: ValidateLongRangeTag.java,v 1.3 2003/05/20 23:08:01 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateLongRangeTag.java

package com.sun.faces.taglib.jsf_core;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>ValidateLongRangeTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValidateLongRangeTag.java,v 1.3 2003/05/20 23:08:01 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ValidateLongRangeTag extends MaxMinValidatorTag
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

    protected long maximum = 0;
    protected long minimum = 0;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public ValidateLongRangeTag()
{
    super();
    super.setId("LongRange");
}

//
// Class methods
//

//
// General Methods
//

public long getMaximum()
{
    return maximum;
}

public void setMaximum(long newMaximum)
{
    maximumSet = true;
    maximum = newMaximum;
}

public long getMinimum()
{
    return minimum;
}

public void setMinimum(long newMinimum)
{
    minimumSet = true;
    minimum = newMinimum;
}

// 
// Methods from ValidatorTag
// 

protected Validator createValidator() throws JspException
{
    LongRangeValidator result = null;

    result = (LongRangeValidator) super.createValidator();
    Assert.assert_it(null != result);

    if (maximumSet) {
	result.setMaximum(getMaximum());
    }

    if (minimumSet) {
	result.setMinimum(getMinimum());
    }

    return result;
}

} // end of class ValidateLongRangeTag
