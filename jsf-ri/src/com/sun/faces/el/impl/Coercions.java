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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * <p>This class contains the logic for coercing data types before
 * operators are applied to them.
 * <p/>
 * <p>The following is the list of rules applied for various type
 * conversions.
 * <p/>
 * <ul><pre>
 * Applying arithmetic operator
 *   Binary operator - A {+,-,*} B
 *     if A and B are null
 *       return 0
 *     if A or B is BigDecimal, coerce both to BigDecimal and then:
 *       if operator is +, return <code>A.add(B)</code>
 *       if operator is -, return <code>A.subtract(B)</code>
 *       if operator is *, return <code>A.multiply(B)</code>
 *     if A or B is Float, Double, or String containing ".", "e", or "E"
 *       if A or B is BigInteger, coerce both A and B to BigDecimal and apply operator
 *       coerce both A and B to Double and apply operator
 *     if A or B is BigInteger, coerce both to BigInteger and then:
 *       if operator is +, return <code>A.add(B)</code>
 *       if operator is -, return <code>A.subtract(B)</code>
 *       if operator is *, return <code>A.multiply(B)</code>
 *     otherwise
 *       coerce both A and B to Long
 *       apply operator
 *     if operator results in exception (such as divide by 0), error
 * <p/>
 *   Binary operator - A {/,div} B
 *     if A and B are null
 *       return 0
 *     if A or B is a BigDecimal or BigInteger, coerce both to BigDecimal and
 *      return <code>A.divide(B, BigDecimal.ROUND_HALF_UP)</code>
 *     otherwise
 *       coerce both A and B to Double
 *       apply operator
 *     if operator results in exception (such as divide by 0), error
 * <p/>
 *   Binary operator - A {%,mod} B
 *     if A and B are null
 *       return 0
 *     if A or B is BigDecimal, Float, Double, or String containing ".", "e" or "E"
 *       coerce both to Double
 *       apply operator
 *     if A or B is BigInteger, coerce both to BigInteger and return
 *      <code>A.remainder(B)</code>
 *     otherwise
 *       coerce both A and B to Long
 *       apply operator
 *     if operator results in exception (such as divide by 0), error
 * <p/>
 *   Unary minus operator - -A
 *     if A is null
 *       return 0
 *     if A is BigInteger or BigDecimal, return <code>A.negate()</code>
 *     if A is String
 *       if A contains ".", "e", or "E"
 *         coerce to Double, apply operator
 *       otherwise
 *         coerce to a Long and apply operator
 *     if A is Byte,Short,Integer,Long,Float,Double
 *       retain type, apply operator
 *     if operator results in exception, error
 *     otherwise
 *       error
 * <p/>
 * Applying "empty" operator - empty A
 *   if A is null
 *     return true
 *   if A is zero-length String
 *     return true
 *   if A is zero-length array
 *     return true
 *   if A is List and ((List) A).isEmpty()
 *     return true
 *   if A is Map and ((Map) A).isEmpty()
 *     return true
 *   if A is Collection an ((Collection) A).isEmpty()
 *     return true
 *   otherwise
 *     return false
 * <p/>
 * Applying logical operators
 *   Binary operator - A {and,or} B
 *     coerce both A and B to Boolean, apply operator
 *   NOTE - operator stops as soon as expression can be determined, i.e.,
 *     A and B and C and D - if B is false, then only A and B is evaluated
 *   Unary not operator - not A
 *     coerce A to Boolean, apply operator
 * <p/>
 * Applying relational operator
 *   A {<,>,<=,>=,lt,gt,lte,gte} B
 *     if A==B
 *       if operator is >= or <=
 *         return true
 *       otherwise
 *         return false
 *     if A or B is null
 *       return false
 *     if A or B is BigDecimal, coerce both A and B to BigDecimal and use the
 *      return value of <code>A.compareTo(B)</code>
 *     if A or B is Float or Double
 *       coerce both A and B to Double
 *       apply operator
 *     if A or B is BigInteger, coerce both A and B to BigInteger and use the
 *      return value of <code>A.compareTo(B)</code>
 *     if A or B is Byte,Short,Character,Integer,Long
 *       coerce both A and B to Long
 *       apply operator
 *     if A or B is String
 *       coerce both A and B to String, compare lexically
 *     if A is Comparable
 *       if A.compareTo (B) throws exception
 *         error
 *       otherwise
 *         use result of A.compareTo(B)
 *     if B is Comparable
 *       if B.compareTo (A) throws exception
 *         error
 *       otherwise
 *         use result of B.compareTo(A)
 *     otherwise
 *       error
 * <p/>
 * Applying equality operator
 *   A {==,!=} B
 *     if A==B
 *       apply operator
 *     if A or B is null
 *       return false for ==, true for !=
 *     if A or B is BigDecimal, coerce both A and B to BigDecimal and then:
 *       if operator is == or eq, return <code>A.equals(B)</code>
 *       if operator is != or ne, return <code>!A.equals(B)</code>
 *     if A or B is Float or Double
 *       coerce both A and B to Double
 *       apply operator
 *     if A or B is BigInteger, coerce both A and B to BigInteger and then:
 *       if operator is == or eq, return <code>A.equals(B)</code>
 *       if operator is != or ne, return <code>!A.equals(B)</code>
 *     if A or B is Byte,Short,Character,Integer,Long
 *       coerce both A and B to Long
 *       apply operator
 *     if A or B is Boolean
 *       coerce both A and B to Boolean
 *       apply operator
 *     if A or B is String
 *       coerce both A and B to String, compare lexically
 *     otherwise
 *       if an error occurs while calling A.equals(B)
 *         error
 *       apply operator to result of A.equals(B)
 * <p/>
 * coercions
 * <p/>
 *   coerce A to String
 *     A is String
 *       return A
 *     A is null
 *       return ""
 *     A.toString throws exception
 *       error
 *     otherwise
 *       return A.toString
 * <p/>
 *   coerce A to Number type N
 *     A is null or ""
 *       return 0
 *     A is Character
 *       convert to short, apply following rules
 *     A is Boolean
 *       error
 *     A is Number type N
 *       return A
 *     A is Number, coerce quietly to type N using the following algorithm
 *         If N is BigInteger
 *             If A is BigDecimal, return <code>A.toBigInteger()</code>
 *             Otherwise, return <code>BigInteger.valueOf(A.longValue())</code>
 *        if N is BigDecimal
 *             If A is a BigInteger, return <code>new BigDecimal(A)</code>
 *             Otherwise, return <code>new BigDecimal(A.doubleValue())</code>
 *        If N is Byte, return <code>new Byte(A.byteValue())</code>
 *        If N is Short, return <code>new Short(A.shortValue())</code>
 *        If N is Integer, return <code>new Integer(A.integerValue())</code>
 *        If N is Long, return <code>new Long(A.longValue())</code>
 *        If N is Float, return <code>new Float(A.floatValue())</code>
 *        If N is Double, return <code>new Double(A.doubleValue())</code>
 *        otherwise ERROR
 *     A is String
 *       If N is BigDecimal then:
 *            If <code>new BigDecimal(A)</code> throws an exception then ERROR
 *            Otherwise, return <code>new BigDecimal(A)</code>
 *       If N is BigInteger then:
 *            If <code>new BigInteger(A)</code> throws an exception, then ERROR
 *            Otherwise, return <code>new BigInteger(A)</code>
 *       new <code>N.valueOf(A)</code> throws exception
 *         error
 *       return <code>N.valueOf(A)</code>
 *     otherwise
 *       error
 * <p/>
 *   coerce A to Character should be
 *     A is null or ""
 *       return (char) 0
 *     A is Character
 *       return A
 *     A is Boolean
 *       error
 *     A is Number with less precision than short
 *       coerce quietly - return (char) A
 *     A is Number with greater precision than short
 *       coerce quietly - return (char) A
 *     A is String
 *       return A.charAt (0)
 *     otherwise
 *       error
 * <p/>
 *   coerce A to Boolean
 *     A is null or ""
 *       return false
 *     A is Boolean
 *       return A
 *     A is String
 *       Boolean.valueOf(A) throws exception
 *         error
 *       return Boolean.valueOf(A)
 *     otherwise
 *       error
 * <p/>
 *   coerce A to any other type T
 *     A is null
 *       return null
 *     A is assignable to T
 *       coerce quietly
 *     A is String
 *       T has no PropertyEditor
 *         if A is "", return null
 *         otherwise error
 *       T's PropertyEditor throws exception
 *         if A is "", return null
 *         otherwise error
 *       otherwise
 *         apply T's PropertyEditor
 *     otherwise
 *       error
 * </pre></ul>
 *
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: rlubke $
 */

public class Coercions {

    //-------------------------------------
    // Constants
    //-------------------------------------
    private static final Number ZERO = new Integer(0);
    private static Log log = LogFactory.getLog(Coercions.class);


    //------------------------------------
    // Constructors
    //------------------------------------
    private Coercions() {

        throw new IllegalStateException();

    }
    

    //-------------------------------------
    /**
     * Coerces the given value to the specified class.
     */
    public static Object coerce(Object pValue,
                                Class pClass)
        throws ElException {
        if (pClass == String.class) {
            return coerceToString(pValue);
        } else if (isNumberClass(pClass)) {
            return coerceToPrimitiveNumber(pValue, pClass);
        } else if (pClass == Character.class ||
            pClass == Character.TYPE) {
            return coerceToCharacter(pValue);
        } else if (pClass == Boolean.class ||
            pClass == Boolean.TYPE) {
            return coerceToBoolean(pValue);
        } else {
            return coerceToObject(pValue, pClass);
        }
    }

    //-------------------------------------
    /**
     * Returns true if the given class is Byte, Short, Integer, Long,
     * Float, Double, BigInteger, or BigDecimal
     */
    static boolean isNumberClass(Class pClass) {
        return
            pClass == Byte.class ||
            pClass == Byte.TYPE ||
            pClass == Short.class ||
            pClass == Short.TYPE ||
            pClass == Integer.class ||
            pClass == Integer.TYPE ||
            pClass == Long.class ||
            pClass == Long.TYPE ||
            pClass == Float.class ||
            pClass == Float.TYPE ||
            pClass == Double.class ||
            pClass == Double.TYPE ||
            pClass == BigInteger.class ||
            pClass == BigDecimal.class;
    }

    //-------------------------------------
    /**
     * Coerces the specified value to a String
     */
    public static String coerceToString(Object pValue)
        throws ElException {
        if (pValue == null) {
            return "";
        } else if (pValue instanceof String) {
            return (String) pValue;
        } else {
            try {
                return pValue.toString();
            } catch (Exception exc) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.TOSTRING_EXCEPTION,
                        pValue.getClass().getName());
                    log.error(message, exc);
                    throw new ElException(exc);
                }
                return "";
            }
        }
    }

    //-------------------------------------
    /**
     * Coerces a value to the given primitive number class
     */
    public static Number coerceToPrimitiveNumber(Object pValue,
                                                 Class pClass)
        throws ElException {
        if (pValue == null ||
            "".equals(pValue)) {
            return coerceToPrimitiveNumber(ZERO, pClass);
        } else if (pValue instanceof Character) {
            char val = ((Character) pValue).charValue();
            return coerceToPrimitiveNumber(new Short((short) val), pClass);
        } else if (pValue instanceof Boolean) {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.BOOLEAN_TO_NUMBER, pValue, pClass.getName());
                log.error(message);
                throw new ElException(message);
            }
            return coerceToPrimitiveNumber(ZERO, pClass);
        } else if (pValue.getClass() == pClass) {
            return (Number) pValue;
        } else if (pValue instanceof Number) {
            return coerceToPrimitiveNumber((Number) pValue, pClass);
        } else if (pValue instanceof String) {
            try {
                return coerceToPrimitiveNumber((String) pValue, pClass);
            } catch (Exception exc) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.STRING_TO_NUMBER_EXCEPTION,
                        (String) pValue, pClass.getName());
                    log.error(message);
                    throw new ElException(message);
                }
                return coerceToPrimitiveNumber(ZERO, pClass);
            }
        } else {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.COERCE_TO_NUMBER,
                    pValue.getClass().getName(),
                    pClass.getName());
                log.error(message);
                throw new ElException(message);
            }
            return coerceToPrimitiveNumber(0, pClass);
        }
    }

    //-------------------------------------
    /**
     * Coerces a value to an Integer, returning null if the coercion
     * isn't possible.
     */
    public static Integer coerceToInteger(Object pValue)
        throws ElException {
        if (pValue == null) {
            return null;
        } else if (pValue instanceof Character) {
            return PrimitiveObjects.getInteger
                ((int) (((Character) pValue).charValue()));
        } else if (pValue instanceof Boolean) {
            if (log.isWarnEnabled()) {
                log.warn(
                    MessageUtil.getMessageWithArgs(Constants.BOOLEAN_TO_NUMBER,
                                                   pValue,
                                                   Integer.class.getName()));
            }
            return PrimitiveObjects.getInteger
                (((Boolean) pValue).booleanValue() ? 1 : 0);
        } else if (pValue instanceof Integer) {
            return (Integer) pValue;
        } else if (pValue instanceof Number) {
            return PrimitiveObjects.getInteger(((Number) pValue).intValue());
        } else if (pValue instanceof String) {
            try {
                return Integer.valueOf((String) pValue);
            } catch (Exception exc) {
                if (log.isWarnEnabled()) {
                    log.warn(MessageUtil.getMessageWithArgs(
                        Constants.STRING_TO_NUMBER_EXCEPTION,
                        (String) pValue,
                        Integer.class.getName()));
                }
                return null;
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn(MessageUtil.getMessageWithArgs(
                    Constants.COERCE_TO_NUMBER,
                    pValue.getClass().getName(),
                    Integer.class.getName()));
            }
            return null;
        }
    }

    //-------------------------------------
    /**
     * Coerces a long to the given primitive number class
     */
    static Number coerceToPrimitiveNumber(long pValue,
                                          Class pClass)
        throws ElException {
        if (pClass == Byte.class || pClass == Byte.TYPE) {
            return PrimitiveObjects.getByte((byte) pValue);
        } else if (pClass == Short.class || pClass == Short.TYPE) {
            return PrimitiveObjects.getShort((short) pValue);
        } else if (pClass == Integer.class || pClass == Integer.TYPE) {
            return PrimitiveObjects.getInteger((int) pValue);
        } else if (pClass == Long.class || pClass == Long.TYPE) {
            return PrimitiveObjects.getLong(pValue);
        } else if (pClass == Float.class || pClass == Float.TYPE) {
            return PrimitiveObjects.getFloat((float) pValue);
        } else if (pClass == Double.class || pClass == Double.TYPE) {
            return PrimitiveObjects.getDouble((double) pValue);
        } else {
            return PrimitiveObjects.getInteger(0);
        }
    }

    //-------------------------------------
    /**
     * Coerces a double to the given primitive number class
     */
    static Number coerceToPrimitiveNumber(double pValue,
                                          Class pClass)
        throws ElException {
        if (pClass == Byte.class || pClass == Byte.TYPE) {
            return PrimitiveObjects.getByte((byte) pValue);
        } else if (pClass == Short.class || pClass == Short.TYPE) {
            return PrimitiveObjects.getShort((short) pValue);
        } else if (pClass == Integer.class || pClass == Integer.TYPE) {
            return PrimitiveObjects.getInteger((int) pValue);
        } else if (pClass == Long.class || pClass == Long.TYPE) {
            return PrimitiveObjects.getLong((long) pValue);
        } else if (pClass == Float.class || pClass == Float.TYPE) {
            return PrimitiveObjects.getFloat((float) pValue);
        } else if (pClass == Double.class || pClass == Double.TYPE) {
            return PrimitiveObjects.getDouble(pValue);
        } else {
            return PrimitiveObjects.getInteger(0);
        }
    }

    //-------------------------------------
    /**
     * Coerces a Number to the given primitive number class
     */
    static Number coerceToPrimitiveNumber(Number pValue, Class pClass)
        throws ElException {
        if (pClass == Byte.class || pClass == Byte.TYPE) {
            return PrimitiveObjects.getByte(pValue.byteValue());
        } else if (pClass == Short.class || pClass == Short.TYPE) {
            return PrimitiveObjects.getShort(pValue.shortValue());
        } else if (pClass == Integer.class || pClass == Integer.TYPE) {
            return PrimitiveObjects.getInteger(pValue.intValue());
        } else if (pClass == Long.class || pClass == Long.TYPE) {
            return PrimitiveObjects.getLong(pValue.longValue());
        } else if (pClass == Float.class || pClass == Float.TYPE) {
            return PrimitiveObjects.getFloat(pValue.floatValue());
        } else if (pClass == Double.class || pClass == Double.TYPE) {
            return PrimitiveObjects.getDouble(pValue.doubleValue());
        } else if (pClass == BigInteger.class) {
            if (pValue instanceof BigDecimal)
                return ((BigDecimal) pValue).toBigInteger();
            else
                return BigInteger.valueOf(pValue.longValue());
        } else if (pClass == BigDecimal.class) {
            if (pValue instanceof BigInteger)
                return new BigDecimal((BigInteger) pValue);
            else
                return new BigDecimal(pValue.doubleValue());
        } else {
            return PrimitiveObjects.getInteger(0);
        }
    }

    //-------------------------------------
    /**
     * Coerces a String to the given primitive number class
     */
    static Number coerceToPrimitiveNumber(String pValue, Class pClass)
        throws ElException {
        if (pClass == Byte.class || pClass == Byte.TYPE) {
            return Byte.valueOf(pValue);
        } else if (pClass == Short.class || pClass == Short.TYPE) {
            return Short.valueOf(pValue);
        } else if (pClass == Integer.class || pClass == Integer.TYPE) {
            return Integer.valueOf(pValue);
        } else if (pClass == Long.class || pClass == Long.TYPE) {
            return Long.valueOf(pValue);
        } else if (pClass == Float.class || pClass == Float.TYPE) {
            return Float.valueOf(pValue);
        } else if (pClass == Double.class || pClass == Double.TYPE) {
            return Double.valueOf(pValue);
        } else if (pClass == BigInteger.class) {
            return new BigInteger(pValue);
        } else if (pClass == BigDecimal.class) {
            return new BigDecimal(pValue);
        } else {
            return PrimitiveObjects.getInteger(0);
        }
    }

    //-------------------------------------
    /**
     * Coerces a value to a Character
     */
    public static Character coerceToCharacter(Object pValue)
        throws ElException {
        if (pValue == null ||
            "".equals(pValue)) {
            return PrimitiveObjects.getCharacter((char) 0);
        } else if (pValue instanceof Character) {
            return (Character) pValue;
        } else if (pValue instanceof Boolean) {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.BOOLEAN_TO_CHARACTER, pValue);
                log.error(message);
                throw new ElException(message);
            }
            return PrimitiveObjects.getCharacter((char) 0);
        } else if (pValue instanceof Number) {
            return PrimitiveObjects.getCharacter
                ((char) ((Number) pValue).shortValue());
        } else if (pValue instanceof String) {
            String str = (String) pValue;
            return PrimitiveObjects.getCharacter(str.charAt(0));
        } else {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.COERCE_TO_CHARACTER,
                    pValue.getClass().getName());
                log.error(message);
                throw new ElException(message);
            }
            return PrimitiveObjects.getCharacter((char) 0);
        }
    }

    //-------------------------------------
    /**
     * Coerces a value to a Boolean
     */
    public static Boolean coerceToBoolean(Object pValue)
        throws ElException {
        if (pValue == null ||
            "".equals(pValue)) {
            return Boolean.FALSE;
        } else if (pValue instanceof Boolean) {
            return (Boolean) pValue;
        } else if (pValue instanceof String) {
            String str = (String) pValue;
            try {
                return Boolean.valueOf(str);
            } catch (Exception exc) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.STRING_TO_BOOLEAN, (String) pValue);
                    log.error(message, exc);
                    throw new ElException(message, exc);
                }
                return Boolean.FALSE;
            }
        } else {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.COERCE_TO_BOOLEAN,
                    pValue.getClass().getName());
                log.error(message);
                throw new ElException(message);
            }
            return Boolean.TRUE;
        }
    }

    //-------------------------------------
    /**
     * Coerces a value to the specified Class that is not covered by any
     * of the above cases
     */
    public static Object coerceToObject(Object pValue, Class pClass)
        throws ElException {
        if (pValue == null) {
            return null;
        } else if (pClass.isAssignableFrom(pValue.getClass())) {
            return pValue;
        } else if (pValue instanceof String) {
            String str = (String) pValue;
            PropertyEditor pe = PropertyEditorManager.findEditor(pClass);
            if (pe == null) {
                if ("".equals(str)) {
                    return null;
                } else {
                    if (log.isErrorEnabled()) {
                        String message = MessageUtil.getMessageWithArgs(
                            Constants.NO_PROPERTY_EDITOR,
                            str,
                            pClass.getName());
                        log.error(message);
                        throw new ElException(message);
                    }
                    return null;
                }
            }
            try {
                pe.setAsText(str);
                return pe.getValue();
            } catch (IllegalArgumentException exc) {
                if ("".equals(str)) {
                    return null;
                } else {
                    if (log.isErrorEnabled()) {
                        String message = MessageUtil.getMessageWithArgs(
                            Constants.PROPERTY_EDITOR_ERROR,
                            pValue,
                            pClass.getName());
                        log.error(message, exc);
                        throw new ElException(message, exc);
                    }
                    return null;
                }
            }
        } else {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.COERCE_TO_OBJECT,
                    pValue.getClass().getName(),
                    pClass.getName());
                log.error(message);
                throw new ElException(message);
            }
            return null;
        }
    }

    //-------------------------------------
    // Applying operators
    //-------------------------------------
    /**
     * Performs all of the necessary type conversions, then calls on the
     * appropriate operator.
     */
    public static Object applyArithmeticOperator
        (Object pLeft,
         Object pRight,
         ArithmeticOperator pOperator)
        throws ElException {
        if (pLeft == null &&
            pRight == null) {
            if (log.isWarnEnabled()) {
                log.warn(MessageUtil.getMessageWithArgs(
                    Constants.ARITH_OP_NULL,
                    pOperator.getOperatorSymbol()));
            }
            return PrimitiveObjects.getInteger(0);
        } else if (isBigDecimal(pLeft) || isBigDecimal(pRight)) {
            BigDecimal left = (BigDecimal)
                coerceToPrimitiveNumber(pLeft, BigDecimal.class);
            BigDecimal right = (BigDecimal)
                coerceToPrimitiveNumber(pRight, BigDecimal.class);
            return pOperator.apply(left, right);
        } else if (isFloatingPointType(pLeft) ||
            isFloatingPointType(pRight) ||
            isFloatingPointString(pLeft) ||
            isFloatingPointString(pRight)) {
            if (isBigInteger(pLeft) || isBigInteger(pRight)) {
                BigDecimal left = (BigDecimal)
                    coerceToPrimitiveNumber(pLeft, BigDecimal.class);
                BigDecimal right = (BigDecimal)
                    coerceToPrimitiveNumber(pRight, BigDecimal.class);
                return pOperator.apply(left, right);
            } else {
                double left =
                    coerceToPrimitiveNumber(pLeft, Double.class).
                    doubleValue();
                double right =
                    coerceToPrimitiveNumber(pRight, Double.class).
                    doubleValue();
                return
                    PrimitiveObjects.getDouble(pOperator.apply(left, right));
            }
        } else if (isBigInteger(pLeft) || isBigInteger(pRight)) {
            BigInteger left = (BigInteger)
                coerceToPrimitiveNumber(pLeft, BigInteger.class);
            BigInteger right = (BigInteger)
                coerceToPrimitiveNumber(pRight, BigInteger.class);
            return pOperator.apply(left, right);
        } else {
            long left =
                coerceToPrimitiveNumber(pLeft, Long.class).
                longValue();
            long right =
                coerceToPrimitiveNumber(pRight, Long.class).
                longValue();
            return
                PrimitiveObjects.getLong(pOperator.apply(left, right));
        }
    }

    //-------------------------------------
    /**
     * Performs all of the necessary type conversions, then calls on the
     * appropriate operator.
     */
    public static Object applyRelationalOperator
        (Object pLeft,
         Object pRight,
         RelationalOperator pOperator)
        throws ElException {
        if (isBigDecimal(pLeft) || isBigDecimal(pRight)) {
            BigDecimal left = (BigDecimal)
                coerceToPrimitiveNumber(pLeft, BigDecimal.class);
            BigDecimal right = (BigDecimal)
                coerceToPrimitiveNumber(pRight, BigDecimal.class);
            return PrimitiveObjects.getBoolean(pOperator.apply(left, right));
        } else if (isFloatingPointType(pLeft) ||
            isFloatingPointType(pRight)) {
            double left =
                coerceToPrimitiveNumber(pLeft, Double.class).
                doubleValue();
            double right =
                coerceToPrimitiveNumber(pRight, Double.class).
                doubleValue();
            return
                PrimitiveObjects.getBoolean(pOperator.apply(left, right));
        } else if (isBigInteger(pLeft) || isBigInteger(pRight)) {
            BigInteger left = (BigInteger)
                coerceToPrimitiveNumber(pLeft, BigInteger.class);
            BigInteger right = (BigInteger)
                coerceToPrimitiveNumber(pRight, BigInteger.class);
            return PrimitiveObjects.getBoolean(pOperator.apply(left, right));
        } else if (isIntegerType(pLeft) ||
            isIntegerType(pRight)) {
            long left =
                coerceToPrimitiveNumber(pLeft, Long.class).
                longValue();
            long right =
                coerceToPrimitiveNumber(pRight, Long.class).
                longValue();
            return
                PrimitiveObjects.getBoolean(pOperator.apply(left, right));
        } else if (pLeft instanceof String ||
            pRight instanceof String) {
            String left = coerceToString(pLeft);
            String right = coerceToString(pRight);
            return
                PrimitiveObjects.getBoolean(pOperator.apply(left, right));
        } else if (pLeft instanceof Comparable) {
            try {
                int result = ((Comparable) pLeft).compareTo(pRight);
                return
                    PrimitiveObjects.getBoolean
                    (pOperator.apply(result, -result));
            } catch (Exception exc) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.COMPARABLE_ERROR,
                        pLeft.getClass().getName(),
                        (pRight == null) ?
                        "null" : pRight.getClass().getName(),
                        pOperator.getOperatorSymbol());
                    log.error(message, exc);
                    throw new ElException(message, exc);
                }
                return Boolean.FALSE;
            }
        } else if (pRight instanceof Comparable) {
            try {
                int result = ((Comparable) pRight).compareTo(pLeft);
                return
                    PrimitiveObjects.getBoolean
                    (pOperator.apply(-result, result));
            } catch (Exception exc) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.COMPARABLE_ERROR,
                        pRight.getClass().getName(),
                        (pLeft == null) ? "null" : pLeft.getClass().getName(),
                        pOperator.getOperatorSymbol());
                    log.error(message, exc);
                    throw new ElException(message, exc);
                }
                return Boolean.FALSE;
            }
        } else {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.ARITH_OP_BAD_TYPE,
                    pOperator.getOperatorSymbol(),
                    pLeft.getClass().getName(),
                    pRight.getClass().getName());
                log.error(message);
                throw new ElException(message);
            }
            return Boolean.FALSE;
        }
    }

    //-------------------------------------
    /**
     * Performs all of the necessary type conversions, then calls on the
     * appropriate operator.
     */
    public static Object applyEqualityOperator
        (Object pLeft,
         Object pRight,
         EqualityOperator pOperator)
        throws ElException {
        if (pLeft == pRight) {
            return PrimitiveObjects.getBoolean(pOperator.apply(true));
        } else if (pLeft == null ||
            pRight == null) {
            return PrimitiveObjects.getBoolean(pOperator.apply(false));
        } else if (isBigDecimal(pLeft) || isBigDecimal(pRight)) {
            BigDecimal left = (BigDecimal)
                coerceToPrimitiveNumber(pLeft, BigDecimal.class);
            BigDecimal right = (BigDecimal)
                coerceToPrimitiveNumber(pRight, BigDecimal.class);
            return PrimitiveObjects.getBoolean(
                pOperator.apply(left.equals(right)));
        } else if (isFloatingPointType(pLeft) ||
            isFloatingPointType(pRight)) {
            double left =
                coerceToPrimitiveNumber(pLeft, Double.class).
                doubleValue();
            double right =
                coerceToPrimitiveNumber(pRight, Double.class).
                doubleValue();
            return
                PrimitiveObjects.getBoolean
                (pOperator.apply(left == right));
        } else if (isBigInteger(pLeft) || isBigInteger(pRight)) {
            BigInteger left = (BigInteger)
                coerceToPrimitiveNumber(pLeft, BigInteger.class);
            BigInteger right = (BigInteger)
                coerceToPrimitiveNumber(pRight, BigInteger.class);
            return PrimitiveObjects.getBoolean(
                pOperator.apply(left.equals(right)));
        } else if (isIntegerType(pLeft) ||
            isIntegerType(pRight)) {
            long left =
                coerceToPrimitiveNumber(pLeft, Long.class).
                longValue();
            long right =
                coerceToPrimitiveNumber(pRight, Long.class).
                longValue();
            return
                PrimitiveObjects.getBoolean
                (pOperator.apply(left == right));
        } else if (pLeft instanceof Boolean ||
            pRight instanceof Boolean) {
            boolean left = coerceToBoolean(pLeft).booleanValue();
            boolean right = coerceToBoolean(pRight).booleanValue();
            return
                PrimitiveObjects.getBoolean
                (pOperator.apply(left == right));
        } else if (pLeft instanceof String ||
            pRight instanceof String) {
            String left = coerceToString(pLeft);
            String right = coerceToString(pRight);
            return
                PrimitiveObjects.getBoolean
                (pOperator.apply(left.equals(right)));
        } else {
            try {
                return
                    PrimitiveObjects.getBoolean
                    (pOperator.apply(pLeft.equals(pRight)));
            } catch (Exception exc) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.ERROR_IN_EQUALS,
                        pLeft.getClass().getName(),
                        pRight.getClass().getName(),
                        pOperator.getOperatorSymbol());
                    log.error(message, exc);
                    throw new ElException(message, exc);
                }
                return Boolean.FALSE;
            }
        }
    }

    //-------------------------------------
    /**
     * Returns true if the given Object is of a floating point type
     */
    public static boolean isFloatingPointType(Object pObject) {
        return
            pObject != null &&
            isFloatingPointType(pObject.getClass());
    }

    //-------------------------------------
    /**
     * Returns true if the given class is of a floating point type
     */
    public static boolean isFloatingPointType(Class pClass) {
        return
            pClass == Float.class ||
            pClass == Float.TYPE ||
            pClass == Double.class ||
            pClass == Double.TYPE;
    }

    //-------------------------------------
    /**
     * Returns true if the given string might contain a floating point
     * number - i.e., it contains ".", "e", or "E"
     */
    public static boolean isFloatingPointString(Object pObject) {
        if (pObject instanceof String) {
            String str = (String) pObject;
            int len = str.length();
            for (int i = 0; i < len; i++) {
                char ch = str.charAt(i);
                if (ch == '.' ||
                    ch == 'e' ||
                    ch == 'E') {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    //-------------------------------------
    /**
     * Returns true if the given Object is of an integer type
     */
    public static boolean isIntegerType(Object pObject) {
        return
            pObject != null &&
            isIntegerType(pObject.getClass());
    }

    //-------------------------------------
    /**
     * Returns true if the given class is of an integer type
     */
    public static boolean isIntegerType(Class pClass) {
        return
            pClass == Byte.class ||
            pClass == Byte.TYPE ||
            pClass == Short.class ||
            pClass == Short.TYPE ||
            pClass == Character.class ||
            pClass == Character.TYPE ||
            pClass == Integer.class ||
            pClass == Integer.TYPE ||
            pClass == Long.class ||
            pClass == Long.TYPE;
    }

    //-------------------------------------

    /**
     * Returns true if the given object is BigInteger.
     *
     * @param pObject - Object to evaluate
     *
     * @return - true if the given object is BigInteger
     */
    public static boolean isBigInteger(Object pObject) {
        return
            pObject != null && pObject instanceof BigInteger;
    }


    /**
     * Returns true if the given object is BigDecimal.
     *
     * @param pObject - Object to evaluate
     *
     * @return - true if the given object is BigDecimal
     */
    public static boolean isBigDecimal(Object pObject) {
        return
            pObject != null && pObject instanceof BigDecimal;
    }
}
