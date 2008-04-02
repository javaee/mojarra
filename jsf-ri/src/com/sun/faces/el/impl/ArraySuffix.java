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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 *
 * <p>Represents an operator that obtains a Map entry, an indexed
 * value, a property value, or an indexed property value of an object.
 * The following are the rules for evaluating this operator:
 *
 * <ul><pre>
 * Evaluating a[b] (assuming a.b == a["b"])
 *   a is null
 *     return null
 *   b is null
 *     return null
 *   a is Map
 *     !a.containsKey (b)
 *       return null
 *     a.get(b) == null
 *       return null
 *     otherwise
 *       return a.get(b)
 *   a is List or array
 *     coerce b to int (using coercion rules)
 *     coercion couldn't be performed
 *       error
 *     a.get(b) or Array.get(a, b) throws ArrayIndexOutOfBoundsException or IndexOutOfBoundsException
 *       return null
 *     a.get(b) or Array.get(a, b) throws other exception
 *       error
 *     return a.get(b) or Array.get(a, b)
 * 
 *   coerce b to String
 *   b is a readable property of a
 *     getter throws an exception
 *       error
 *     otherwise
 *       return result of getter call
 *
 *   otherwise
 *     error
 * </pre></ul>
 * 
 * @author Nathan Abramson - Art Technology Group
 * @author Shawn Bayern
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: rlubke $
 **/

public class ArraySuffix
    extends ValueSuffix {

    //-------------------------------------
    // Constants
    //-------------------------------------
    private static Log log = LogFactory.getLog(ArraySuffix.class);

    // Zero-argument array
    static Object[] sNoArgs = new Object[0];

    //-------------------------------------
    // Properties
    //-------------------------------------
    // property index

    Expression mIndex;

    public Expression getIndex() {
        return mIndex;
    }

    public void setIndex(Expression pIndex) {
        mIndex = pIndex;
    }

    //-------------------------------------
    /**
     *
     * Constructor
     **/
    public ArraySuffix(Expression pIndex) {
        mIndex = pIndex;
    }

    //-------------------------------------
    /**
     *
     * Gets the value of the index
     **/
    protected Object evaluateIndex(
        ExpressionInfo exprInfo)
        throws ElException {
        return mIndex.evaluate(exprInfo);
    }

    //-------------------------------------
    /**
     *
     * Returns the operator symbol
     **/
    protected String getOperatorSymbol() {
        return "[]";
    }

    //-------------------------------------
    // ValueSuffix methods
    //-------------------------------------
    /**
     *
     * Returns the expression in the expression language syntax
     **/
    public String getExpressionString() {
        return "[" + mIndex.getExpressionString() + "]";
    }

    //-------------------------------------
    /**
     *
     * Evaluates the expression in the given context, operating on the
     * given value.
     **/
    public Object evaluate(Object pValue, ExpressionInfo exprInfo)
        throws ElException {
        Object indexVal;
        String indexStr;
        BeanInfoProperty property;
        BeanInfoIndexedProperty ixproperty;

        // Check for null value
        if (pValue == null) {
            if (log.isWarnEnabled()) {
                log.warn(
                    MessageUtil.getMessageWithArgs(
                        Constants.CANT_GET_INDEXED_VALUE_OF_NULL, getOperatorSymbol()));
                return null;
            }
        }

        // Evaluate the index
        else if ((indexVal = evaluateIndex(exprInfo))
            == null) {
            if (log.isWarnEnabled()) {
                log.warn(
                    MessageUtil.getMessageWithArgs(
                        Constants.CANT_GET_NULL_INDEX, getOperatorSymbol()));
                return null;
            }
        }

        // See if it's a Map
        else if (pValue instanceof Map) {
            Map val = (Map) pValue;
            return val.get(indexVal);
        }

        // See if it's a List or array
        else if (pValue instanceof List ||
            pValue.getClass().isArray()) {
            Integer indexObj = Coercions.coerceToInteger(indexVal);
            if (indexObj == null) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.BAD_INDEX_VALUE,
                        getOperatorSymbol(), indexVal.getClass().getName());
                    log.error(message);
                    throw new ElException(message);
                }
                return null;
            } else if (pValue instanceof List) {
                try {
                    return ((List) pValue).get(indexObj.intValue());
                } catch (ArrayIndexOutOfBoundsException aob) {
                    if (log.isWarnEnabled()) {
                        log.warn(
                            MessageUtil.getMessageWithArgs(
                                Constants.EXCEPTION_ACCESSING_LIST, indexObj), aob);
                    }
                    return null;
                } catch (IndexOutOfBoundsException iob) {
                    if (log.isWarnEnabled()) {
                        log.warn(
                            MessageUtil.getMessageWithArgs(
                                Constants.EXCEPTION_ACCESSING_LIST, indexObj), iob);
                    }
                    return null;
                } catch (Throwable t) {
                    if (log.isErrorEnabled()) {
                        String message = MessageUtil.getMessageWithArgs(
                            Constants.EXCEPTION_ACCESSING_LIST,
                            indexObj);
                        log.error(message, t);
                        throw new ElException(message, t);
                    }
                    return null;
                }
            } else {
                try {
                    return Array.get(pValue, indexObj.intValue());
                } catch (ArrayIndexOutOfBoundsException aob) {
                    if (log.isWarnEnabled()) {
                        log.warn(
                            MessageUtil.getMessageWithArgs(
                                Constants.EXCEPTION_ACCESSING_ARRAY, indexObj), aob);
                    }
                    return null;
                } catch (IndexOutOfBoundsException iob) {
                    if (log.isWarnEnabled()) {
                        log.warn(
                            MessageUtil.getMessageWithArgs(
                                Constants.EXCEPTION_ACCESSING_ARRAY, indexObj), iob);
                    }
                    return null;
                } catch (Throwable t) {
                    if (log.isErrorEnabled()) {
                        String message = MessageUtil.getMessageWithArgs(
                            Constants.EXCEPTION_ACCESSING_ARRAY,
                            indexObj);
                        log.error(message, t);
                        throw new ElException(message, t);
                    }
                    return null;
                }
            }
        }

        // Coerce to a String for property access

        else if ((indexStr = Coercions.coerceToString(indexVal)) ==
            null) {
            return null;
        }

        // Look for a JavaBean property
        else if ((property = BeanInfoManager.getBeanInfoProperty
            (pValue.getClass(), indexStr)) != null &&
            property.getReadMethod() != null) {
            try {
                return property.getReadMethod().invoke(pValue, sNoArgs);
            } catch (InvocationTargetException exc) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.ERROR_GETTING_PROPERTY, indexStr, pValue.getClass().getName());
                    Throwable t = exc.getTargetException();
                    log.warn(message, t);
                    throw new ElException(message, t);
                }
                return null;
            } catch (Throwable t) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.ERROR_GETTING_PROPERTY, indexStr, pValue.getClass().getName());
                    log.warn(message, t);
                    throw new ElException(message, t);
                }
                return null;
            }
        } else {
            if (log.isErrorEnabled()) {
                String message = MessageUtil.getMessageWithArgs(
                    Constants.CANT_FIND_INDEX,
                    indexVal,
                    pValue.getClass().getName(),
                    getOperatorSymbol());
                log.error(message);
                throw new ElException(message);
            }
            return null;
        }
        return null;
    }

    //-------------------------------------
}
