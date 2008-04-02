/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.sun.faces.el.impl;

import java.util.List;


/**
 *
 * <p>An expression representing a binary operator on a value
 * 
 * @author Nathan Abramson - Art Technology Group
 * @author Shawn Bayern
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: rlubke $
 **/

public class BinaryOperatorExpression
    extends Expression {
    //-------------------------------------
    // Properties
    //-------------------------------------
    // property expression

    Expression mExpression;

    public Expression getExpression() {
        return mExpression;
    }

    public void setExpression(Expression pExpression) {
        mExpression = pExpression;
    }

    //-------------------------------------
    // property operators

    List mOperators;

    public List getOperators() {
        return mOperators;
    }

    public void setOperators(List pOperators) {
        mOperators = pOperators;
    }

    //-------------------------------------
    // property expressions

    List mExpressions;

    public List getExpressions() {
        return mExpressions;
    }

    public void setExpressions(List pExpressions) {
        mExpressions = pExpressions;
    }

    //-------------------------------------
    /**
     *
     * Constructor
     **/
    public BinaryOperatorExpression(
        Expression pExpression,
        List pOperators,
        List pExpressions) {
        mExpression = pExpression;
        mOperators = pOperators;
        mExpressions = pExpressions;
    }

    //-------------------------------------
    // Expression methods
    //-------------------------------------
    /**
     *
     * Returns the expression in the expression language syntax
     **/
    public String getExpressionString() {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        buf.append(mExpression.getExpressionString());
        for (int i = 0, size = mOperators.size(); i < size; i++) {
            BinaryOperator operator = (BinaryOperator) mOperators.get(i);
            Expression expression = (Expression) mExpressions.get(i);
            buf.append(" ");
            buf.append(operator.getOperatorSymbol());
            buf.append(" ");
            buf.append(expression.getExpressionString());
        }
        buf.append(")");

        return buf.toString();
    }

    //-------------------------------------
    /**
     *
     * Evaluates to the literal value
     **/
    public Object evaluate(
        ExpressionInfo exprInfo)
        throws ElException {
        Object value = mExpression.evaluate(exprInfo);
        for (int i = 0, size = mOperators.size(); i < size; i++) {
            BinaryOperator operator = (BinaryOperator) mOperators.get(i);

            // For the And/Or operators, we need to coerce to a boolean
            // before testing if we shouldEvaluate
            if (operator.shouldCoerceToBoolean()) {
                value = Coercions.coerceToBoolean(value);
            }

            if (operator.shouldEvaluate(value)) {
                Expression expression = (Expression) mExpressions.get(i);
                Object nextValue = expression.evaluate(
                    exprInfo);

                value = operator.apply(value, nextValue);
            }
        }
        return value;
    }

    //-------------------------------------
}
