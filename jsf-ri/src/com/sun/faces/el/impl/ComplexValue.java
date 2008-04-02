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
 * <p>Represents a dynamic value, which consists of a prefix and an
 * optional set of ValueSuffix elements.  A prefix is something like
 * an identifier, and a suffix is something like a "property of" or
 * "indexed element of" operator.
 * 
 * @author Nathan Abramson - Art Technology Group
 * @author Shawn Bayern
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: eburns $
 **/

public class ComplexValue
    extends Expression {
    //-------------------------------------
    // Properties
    //-------------------------------------
    // property prefix

    Expression mPrefix;

    public Expression getPrefix() {
        return mPrefix;
    }

    public void setPrefix(Expression pPrefix) {
        mPrefix = pPrefix;
    }

    //-------------------------------------
    // property suffixes

    List mSuffixes;

    public List getSuffixes() {
        return mSuffixes;
    }

    public void setSuffixes(List pSuffixes) {
        mSuffixes = pSuffixes;
    }

    //-------------------------------------
    /**
     *
     * Constructor
     **/
    public ComplexValue(
        Expression pPrefix,
        List pSuffixes) {
        mPrefix = pPrefix;
        mSuffixes = pSuffixes;
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
        buf.append(mPrefix.getExpressionString());

        for (int i = 0; mSuffixes != null && i < mSuffixes.size(); i++) {
            ValueSuffix suffix = (ValueSuffix) mSuffixes.get(i);
            buf.append(suffix.getExpressionString());
        }

        return buf.toString();
    }

    //-------------------------------------
    /**
     *
     * Evaluates by evaluating the prefix, then applying the suffixes
     **/
    public Object evaluate(ExpressionInfo exprInfo)
        throws ElException {
        Object ret = mPrefix.evaluate(exprInfo);

        // Apply the suffixes
        for (int i = 0; mSuffixes != null && i < mSuffixes.size(); i++) {
            ValueSuffix suffix = (ValueSuffix) mSuffixes.get(i);
            ret = suffix.evaluate(ret, exprInfo);
        }

        return ret;
    }

    public void setValue(ExpressionInfo exprInfo, Object newValue)
	throws ElException {
        Object ret = mPrefix.evaluate(exprInfo);

        // Apply the suffixes
        for (int i = 0; mSuffixes != null && i < mSuffixes.size() - 1; i++) {
            ValueSuffix suffix = (ValueSuffix) mSuffixes.get(i);
            ret = suffix.evaluate(ret, exprInfo);
        }
	if (mSuffixes != null && !mSuffixes.isEmpty()) {
	    // Set the value
	    ValueSuffix last = 
		(ValueSuffix) mSuffixes.get(mSuffixes.size() - 1);
	    last.setValue(ret, newValue, exprInfo);
	}
    }

    public boolean isReadOnly(ExpressionInfo exprInfo)
        throws ElException {
        Object ret = mPrefix.evaluate(exprInfo);
	boolean result = true;

        // Apply the suffixes
        for (int i = 0; mSuffixes != null && i < mSuffixes.size() - 1; i++) {
            ValueSuffix suffix = (ValueSuffix) mSuffixes.get(i);
            ret = suffix.evaluate(ret, exprInfo);
        }
	if (mSuffixes != null && !mSuffixes.isEmpty()) {
	    ValueSuffix last = 
		(ValueSuffix) mSuffixes.get(mSuffixes.size() - 1);
	    result = last.isReadOnly(ret, exprInfo);
	}
	return result;
    }

    public Class getType(ExpressionInfo exprInfo)
        throws ElException {
        Object ret = mPrefix.evaluate(exprInfo);
	Class result = ret.getClass();

        // Apply the suffixes
        for (int i = 0; mSuffixes != null && i < mSuffixes.size() - 1; i++) {
            ValueSuffix suffix = (ValueSuffix) mSuffixes.get(i);
            ret = suffix.evaluate(ret, exprInfo);
        }
	if (mSuffixes != null && !mSuffixes.isEmpty()) {
	    ValueSuffix last = 
		(ValueSuffix) mSuffixes.get(mSuffixes.size() - 1);
	    result = last.getType(ret, exprInfo);
	}
	return result;
    }

    //-------------------------------------
}
