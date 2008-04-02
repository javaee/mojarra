/*
 * $Id: ValidateLengthTag.java,v 1.11 2004/10/12 14:39:54 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateLengthTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;

import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;

/**
 * ValidateLengthTag is the tag handler class for
 * <code>validate_length</code> tag
 */

public class ValidateLengthTag extends MaxMinValidatorTag {

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
    protected int maximum = 0;
    protected String minimum_ = null;
    protected int minimum = 0;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public ValidateLengthTag() {
        super();
    }

//
// Class methods
//

//
// General Methods
//

    public void setMaximum(String newMaximum) {
        maximumSet = true;
        maximum_ = newMaximum;
    }


    public void setMinimum(String newMinimum) {
        minimumSet = true;
        minimum_ = newMinimum;
    }

    public int doStartTag() throws JspException {
        super.setValidatorId("javax.faces.Length");
	return super.doStartTag();
    }


// 
// Methods from ValidatorTag
//

    protected Validator createValidator() throws JspException {
        LengthValidator result = null;

        result = (LengthValidator) super.createValidator();
        assert (null != result);

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
                Number numberObj = (Number) Util.evaluateVBExpression(minimum_);
                assert (null != numberObj);
                minimum = numberObj.intValue();
            } else {
                minimum = new Integer(minimum_).intValue();
            }
        }
        if (maximum_ != null) {
            if (Util.isVBExpression(maximum_)) {
                Number numberObj = (Number) Util.evaluateVBExpression(maximum_);
                assert (null != numberObj);
                maximum = numberObj.intValue();
            } else {
                maximum = new Integer(maximum_).intValue();
            }
        }
    }

} // end of class ValidateLengthTag
