/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

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


/**
 * <p>Represents an expression String consisting of a mixture of
 * Strings and Expressions.
 *
 * @author Nathan Abramson - Art Technology Group
 * @author Shawn Bayern
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: rlubke $
 */

public class ExpressionString {

    //-------------------------------------
    // Properties
    //-------------------------------------
    // property elements

    Object[] mElements;


    public Object[] getElements() {
        return mElements;
    }


    public void setElements(Object[] pElements) {
        mElements = pElements;
    }

    //-------------------------------------
    /**
     * Constructor
     */
    public ExpressionString(Object[] pElements) {
        mElements = pElements;
    }

    //-------------------------------------
    /**
     * Evaluates the expression string by evaluating each element,
     * converting it to a String (using toString, or "" for null values)
     * and concatenating the results into a single String.
     */
    public String evaluate(ExpressionInfo exprInfo)
        throws ElException {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < mElements.length; i++) {
            Object elem = mElements[i];
            if (elem instanceof String) {
                buf.append((String) elem);
            } else if (elem instanceof Expression) {
                Object val =
                    ((Expression) elem).evaluate(exprInfo);
                if (val != null) {
                    buf.append(val.toString());
                }
            }
        }
        return buf.toString();
    }

    //-------------------------------------
    /**
     * Returns the expression in the expression language syntax
     */
    public String getExpressionString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < mElements.length; i++) {
            Object elem = mElements[i];
            if (elem instanceof String) {
                buf.append((String) elem);
            } else if (elem instanceof Expression) {
                buf.append("${");
                buf.append(((Expression) elem).getExpressionString());
                buf.append("}");
            }
        }
        return buf.toString();
    }

    //-------------------------------------
}
