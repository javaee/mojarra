/*
 * $Id: ValidateDoubleRangeTag.java,v 1.5 2004/01/27 21:04:43 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateDoubleRangeTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;


import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.Validator;

import javax.servlet.jsp.JspException;

/**
 * ValidateDoubleRangeTag is the tag handler class for 
 * <code>validate_doublerange</code> tag.
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

protected String maximum_ = null;
protected double maximum = 0;
protected String minimum_ = null;
protected double minimum = 0;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public ValidateDoubleRangeTag()
{
    super();
    super.setValidatorId("javax.faces.DoubleRange");
}

//
// Class methods
//

//
// General Methods
//

public void setMaximum(String newMaximum)
{
    maximumSet = true;
    maximum_ = newMaximum;
}

public void setMinimum(String newMinimum)
{
    minimumSet = true;
    minimum_ = newMinimum;
}

// 
// Methods from ValidatorTag
// 

protected Validator createValidator() throws JspException
{
    DoubleRangeValidator result = null;

    result = (DoubleRangeValidator) super.createValidator();
    Util.doAssert(null != result);

    evaluateExpressions();
    if (maximumSet) {
	result.setMaximum(maximum);
    }

    if (minimumSet) {
	result.setMinimum(minimum);
    }

    return result;
}

/* Evaluates expressions as necessary */
private void evaluateExpressions() throws JspException {
   
    if (minimum_ != null) {
        if (Util.isVBExpression(minimum_)) {  
            Double doubleObj = (Double)Util.evaluateVBExpression(minimum_);
            Util.doAssert(null != doubleObj);
            minimum = doubleObj.doubleValue();
        } else {
            minimum = new Double(minimum_).doubleValue();
        }
    }
    if (maximum_ != null) {
        if (Util.isVBExpression(maximum_)) {  
            Double doubleObj = (Double)Util.evaluateVBExpression(maximum_);
            Util.doAssert(null != doubleObj);
            maximum = doubleObj.doubleValue();
        } else {
            maximum = new Double(maximum_).doubleValue();
        }
    }
}

} // end of class ValidateDoubleRangeTag
