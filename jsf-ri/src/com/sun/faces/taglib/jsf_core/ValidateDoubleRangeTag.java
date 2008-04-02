/*
 * $Id: ValidateDoubleRangeTag.java,v 1.17 2006/03/29 23:03:52 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// ValidateDoubleRangeTag.java

package com.sun.faces.taglib.jsf_core;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;

import com.sun.faces.util.Util;

/**
 * ValidateDoubleRangeTag is the tag handler class for
 * <code>validate_doublerange</code> tag.
 */

public class ValidateDoubleRangeTag extends MaxMinValidatorTag {

    private static final long serialVersionUID = 1677210040390032609L;
    private static ValueExpression VALIDATOR_ID_EXPR = null;

//
// Instance Variables
//

// Attribute Instance Variables

    protected ValueExpression maximumExpression = null;
    protected ValueExpression minimumExpression = null;

    protected double maximum = 0;
    protected double minimum = 0;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public ValidateDoubleRangeTag() {
        super();
        if (VALIDATOR_ID_EXPR == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            ExpressionFactory factory =
                context.getApplication().getExpressionFactory();
            VALIDATOR_ID_EXPR =
                factory.createValueExpression(context.getELContext(), 
                    "javax.faces.DoubleRange",String.class);
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

        DoubleRangeValidator result = (DoubleRangeValidator)
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
                                  context)).doubleValue();
            } else {
                minimum =
                Double.valueOf(minimumExpression.getExpressionString()).
                    doubleValue();
            }
        }
        if (maximumExpression != null) {
            if (!maximumExpression.isLiteralText()) {
                maximum = ((Number)
                              Util.evaluateValueExpression(maximumExpression,
                                  context)).doubleValue();
            } else {
                maximum =
                Double.valueOf(maximumExpression.getExpressionString()).
                    doubleValue();
            }
        }
    }

} // end of class ValidateDoubleRangeTag
