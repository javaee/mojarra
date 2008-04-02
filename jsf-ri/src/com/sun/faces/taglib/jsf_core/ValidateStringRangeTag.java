/*
 * $Id: ValidateStringRangeTag.java,v 1.5 2003/10/07 22:02:09 rlubke Exp $
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

import com.sun.faces.util.Util;


/**
 *
 *  <B>ValidateStringRangeTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValidateStringRangeTag.java,v 1.5 2003/10/07 22:02:09 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ValidateStringRangeTag extends MaxMinValidatorTag {
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
    protected String maximum_ = null;
    protected String minimum = null;
    protected String minimum_ = null;


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ValidateStringRangeTag() {
        super();
        super.setId("StringRange");
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String newMaximum) {
        maximumSet = true;
        maximum_ = newMaximum;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String newMinimum) {
        minimumSet = true;
        minimum_ = newMinimum;
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

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (maximum_ != null) {
            maximum = Util.evaluateElExpression(maximum_, pageContext);
 	}
        if (minimum_ != null) {
            minimum= Util.evaluateElExpression(minimum_, pageContext);
 	}
    }

    //
    // Methods from TagSupport
    //

    public int doStartTag() throws JspException {
        // evaluate any expressions that we were passed
        evaluateExpressions();

        // chain to the parent implementation
        return super.doStartTag();
    }

} // end of class ValidateStringRangeTag
