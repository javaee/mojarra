/*
 * $Id: ValidateLongRangeTag.java,v 1.13 2005/05/05 20:51:27 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateLongRangeTag.java

package com.sun.faces.taglib.jsf_core;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;

import com.sun.faces.util.Util;

/**
 * ValidateLongRangeTag is the tag handler class for
 * <code>validate_longrange</code> tag.
 */

public class ValidateLongRangeTag extends MaxMinValidatorTag {

    private static ValueExpression VALIDATOR_ID_EXPR = null;

// Attribute Instance Variables
    protected ValueExpression maximumExpression = null;
    protected ValueExpression minimumExpression = null;

    protected long maximum = 0;
    protected long minimum = 0;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public ValidateLongRangeTag() {
        super();
        if (VALIDATOR_ID_EXPR == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            ExpressionFactory factory =
                FacesContext.getCurrentInstance().getApplication().
                    getExpressionFactory();
            VALIDATOR_ID_EXPR =
                factory.createValueExpression(context.getELContext(),  
                                              "javax.faces.LongRange",
                                              String.class);
        }
    }

//
// Class methods
//

//
// General Methods
//

    public void setMaximum(ValueExpression newMaximum) {
        maximumSet = true;
        maximumExpression = newMaximum;
    }


    public void setMinimum(ValueExpression newMinimum) {
        minimumSet = true;
        minimumExpression = newMinimum;
    }

    public int doStartTag() throws JspException {
        super.setValidatorId(VALIDATOR_ID_EXPR);
        return super.doStartTag();
    }


// 
// Methods from ValidatorTag
//

    protected Validator createValidator() throws JspException {

        LongRangeValidator result = (LongRangeValidator)
            super.createValidator();
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
    private void evaluateExpressions() {

        ELContext context = FacesContext.getCurrentInstance().getELContext();

        if (minimumExpression != null) {
            if (!minimumExpression.isLiteralText()) {
                minimum = ((Number)
                              Util.evaluateValueExpression(minimumExpression,
                                  context)).longValue();
            } else {
                minimum =
                Integer.valueOf(minimumExpression.getExpressionString()).
                    longValue();
            }
        }
        if (maximumExpression != null) {
            if (!maximumExpression.isLiteralText()) {
                maximum = ((Number)
                              Util.evaluateValueExpression(maximumExpression,
                                  context)).longValue();
            } else {
                maximum =
                Integer.valueOf(maximumExpression.getExpressionString()).
                    longValue();
            }
        }
    }

} // end of class ValidateLongRangeTag
