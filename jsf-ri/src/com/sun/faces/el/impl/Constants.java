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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * <p>This contains all of the non-public constants, including
 * messsage strings read from the resource file.
 *
 * @author Nathan Abramson - Art Technology Group
 * @author Shawn Bayern
 *
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: ofung $
 **/

public class Constants {
    //-------------------------------------
    // Resources

    static ResourceBundle sResources =
        ResourceBundle.getBundle("com.sun.faces.el.impl.Resources");

    //-------------------------------------
    // Messages from the resource bundle
    //-------------------------------------

    public static final String EXCEPTION_GETTING_BEANINFO =
        getStringResource("EXCEPTION_GETTING_BEANINFO");

    public static final String NULL_EXPRESSION_STRING =
        getStringResource("NULL_EXPRESSION_STRING");

    public static final String PARSE_EXCEPTION =
        getStringResource("PARSE_EXCEPTION");

    public static final String CANT_GET_PROPERTY_OF_NULL =
        getStringResource("CANT_GET_PROPERTY_OF_NULL");

    public static final String NO_SUCH_PROPERTY =
        getStringResource("NO_SUCH_PROPERTY");

    public static final String NO_GETTER_METHOD =
        getStringResource("NO_GETTER_METHOD");

    public static final String ERROR_GETTING_PROPERTY =
        getStringResource("ERROR_GETTING_PROPERTY");

    public static final String CANT_GET_INDEXED_VALUE_OF_NULL =
        getStringResource("CANT_GET_INDEXED_VALUE_OF_NULL");

    public static final String CANT_GET_NULL_INDEX =
        getStringResource("CANT_GET_NULL_INDEX");

    public static final String NULL_INDEX =
        getStringResource("NULL_INDEX");

    public static final String BAD_INDEX_VALUE =
        getStringResource("BAD_INDEX_VALUE");

    public static final String EXCEPTION_ACCESSING_LIST =
        getStringResource("EXCEPTION_ACCESSING_LIST");

    public static final String EXCEPTION_ACCESSING_ARRAY =
        getStringResource("EXCEPTION_ACCESSING_ARRAY");

    public static final String CANT_FIND_INDEX =
        getStringResource("CANT_FIND_INDEX");

    public static final String TOSTRING_EXCEPTION =
        getStringResource("TOSTRING_EXCEPTION");

    public static final String BOOLEAN_TO_NUMBER =
        getStringResource("BOOLEAN_TO_NUMBER");

    public static final String STRING_TO_NUMBER_EXCEPTION =
        getStringResource("STRING_TO_NUMBER_EXCEPTION");

    public static final String COERCE_TO_NUMBER =
        getStringResource("COERCE_TO_NUMBER");

    public static final String BOOLEAN_TO_CHARACTER =
        getStringResource("BOOLEAN_TO_CHARACTER");

    public static final String EMPTY_STRING_TO_CHARACTER =
        getStringResource("EMPTY_STRING_TO_CHARACTER");

    public static final String COERCE_TO_CHARACTER =
        getStringResource("COERCE_TO_CHARACTER");

    public static final String NULL_TO_BOOLEAN =
        getStringResource("NULL_TO_BOOLEAN");

    public static final String STRING_TO_BOOLEAN =
        getStringResource("STRING_TO_BOOLEAN");

    public static final String COERCE_TO_BOOLEAN =
        getStringResource("COERCE_TO_BOOLEAN");

    public static final String COERCE_TO_OBJECT =
        getStringResource("COERCE_TO_OBJECT");

    public static final String NO_PROPERTY_EDITOR =
        getStringResource("NO_PROPERTY_EDITOR");

    public static final String PROPERTY_EDITOR_ERROR =
        getStringResource("PROPERTY_EDITOR_ERROR");

    public static final String ARITH_OP_NULL =
        getStringResource("ARITH_OP_NULL");

    public static final String ARITH_OP_BAD_TYPE =
        getStringResource("ARITH_OP_BAD_TYPE");

    public static final String ARITH_ERROR =
        getStringResource("ARITH_ERROR");

    public static final String ERROR_IN_EQUALS =
        getStringResource("ERROR_IN_EQUALS");

    public static final String UNARY_OP_BAD_TYPE =
        getStringResource("UNARY_OP_BAD_TYPE");

    public static final String NAMED_VALUE_NOT_FOUND =
        getStringResource("NAMED_VALUE_NOT_FOUND");

    public static final String CANT_GET_INDEXED_PROPERTY =
        getStringResource("CANT_GET_INDEXED_PROPERTY");

    public static final String COMPARABLE_ERROR =
        getStringResource("COMPARABLE_ERROR");

    public static final String BAD_IMPLICIT_OBJECT =
        getStringResource("BAD_IMPLICIT_OBJECT");

    public static final String ATTRIBUTE_EVALUATION_EXCEPTION =
        getStringResource("ATTRIBUTE_EVALUATION_EXCEPTION");

    public static final String ATTRIBUTE_PARSE_EXCEPTION =
        getStringResource("ATTRIBUTE_PARSE_EXCEPTION");

    public static final String UNKNOWN_FUNCTION =
        getStringResource("UNKNOWN_FUNCTION");

    public static final String INAPPROPRIATE_FUNCTION_ARG_COUNT =
        getStringResource("INAPPROPRIATE_FUNCTION_ARG_COUNT");

    public static final String FUNCTION_INVOCATION_ERROR =
        getStringResource("FUNCTION_INVOCATION_ERROR");


    //-------------------------------------
    // Getting resources
    //-------------------------------------
    /**
     *
     * 
     **/
    public static String getStringResource(String pResourceName)
        throws MissingResourceException {
        try {
            String ret = sResources.getString(pResourceName);
            if (ret == null) {
                String str = "ERROR: Unable to load resource " + pResourceName;
                System.err.println(str);
                throw new MissingResourceException
                    (
                        str,
                        "com.sun.faces.el.impl.Constants",
                        pResourceName);
            } else {
                return ret;
            }
        } catch (MissingResourceException exc) {
            System.err.println(
                "ERROR: Unable to load resource " +
                pResourceName +
                ": " +
                exc);
            throw exc;
        }
    }

    //-------------------------------------
}
