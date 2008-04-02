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


import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * <p>This is the superclass for all relational operators (except ==
 * or !=)
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: ofung $
 **/

public abstract class RelationalOperator
    extends BinaryOperator {

    //-------------------------------------
    /**
     *
     * Applies the operator to the given value
     **/
    public Object apply(Object pLeft, Object pRight)
        throws ElException {
        return Coercions.applyRelationalOperator(pLeft, pRight, this);
    }

    //-------------------------------------
    /**
     *
     * Applies the operator to the given double values
     **/
    public abstract boolean apply(
        double pLeft,
        double pRight
                                  );
  
    //-------------------------------------
    /**
     *
     * Applies the operator to the given long values
     **/
    public abstract boolean apply(
        long pLeft,
        long pRight
                                  );
  
    //-------------------------------------
    /**
     *
     * Applies the operator to the given String values
     **/
    public abstract boolean apply(
        String pLeft,
        String pRight
                                  );

    //-------------------------------------


    /**
     * Applies the operator to the given BigDecimal values, returning a BigDecimal
     **/
    public abstract boolean apply(BigDecimal pLeft, BigDecimal pRight);

    //-------------------------------------

    /**
     * Applies the operator to the given BigDecimal values, returning a BigDecimal
     **/
    public abstract boolean apply(BigInteger pLeft, BigInteger pRight);

    //-------------------------------------


    /**
     * Test return value of BigInteger/BigDecimal A.compareTo(B).
     * @param val - result of BigInteger/BigDecimal compareTo() call
     * @return - true if result is less than 0, otherwise false
     */
    protected boolean isLess(int val) {
        if (val < 0)
            return true;
        else
            return false;
    }

    /**
     * Test return value of BigInteger/BigDecimal A.compareTo(B).
     * @param val - result of BigInteger/BigDecimal compareTo() call
     * @return - true if result is equal to 0, otherwise false
     */
    protected boolean isEqual(int val) {
        if (val == 0)
            return true;
        else
            return false;
    }

    /**
     * Test return value of BigInteger/BigDecimal A.compareTo(B).
     * @param val - result of BigInteger/BigDecimal compareTo() call
     * @return - true if result is greater than 0, otherwise false
     */
    protected boolean isGreater(int val) {
        if (val > 0)
            return true;
        else
            return false;
    }
}
