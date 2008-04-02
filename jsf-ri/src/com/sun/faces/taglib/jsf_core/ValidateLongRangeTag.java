/*
 * $Id: ValidateLongRangeTag.java,v 1.16 2006/03/29 22:38:42 rlubke Exp $
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

// ValidateLongRangeTag.java

package com.sun.faces.taglib.jsf_core;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
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

// Attribute Instance Variables
    protected ValueExpression maximumExpression = null;
    protected ValueExpression minimumExpression = null;

    protected long maximum = 0;
    protected long minimum = 0;

    private static final long serialVersionUID = 292617728229736800L;
    private static ValueExpression VALIDATOR_ID_EXPR = null;

    // ------------------------------------------------------------ Constructors


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

    // ---------------------------------------------------------- Public Methods


    public int doStartTag() throws JspException {

        super.setValidatorId(VALIDATOR_ID_EXPR);
        return super.doStartTag();

    }


    public void setMaximum(ValueExpression newMaximum) {

        maximumSet = true;
        maximumExpression = newMaximum;

    }


    public void setMinimum(ValueExpression newMinimum) {

        minimumSet = true;
        minimumExpression = newMinimum;

    }

    // ------------------------------------------------------- Protected Methods


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

    // --------------------------------------------------------- Private Methods

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
