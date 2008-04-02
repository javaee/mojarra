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
 *
 * <p>This converts primitive values to their Object counterparts.
 * For bytes and chars, values from 0 to 255 are cached.  For shorts,
 * ints, and longs, values -1000 to 1000 are cached.
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: ofung $
 **/

class PrimitiveObjects {
    //-------------------------------------
    // Constants
    //-------------------------------------

    static int BYTE_LOWER_BOUND = 0;
    static int BYTE_UPPER_BOUND = 255;
    static int CHARACTER_LOWER_BOUND = 0;
    static int CHARACTER_UPPER_BOUND = 255;
    static int SHORT_LOWER_BOUND = -1000;
    static int SHORT_UPPER_BOUND = 1000;
    static int INTEGER_LOWER_BOUND = -1000;
    static int INTEGER_UPPER_BOUND = 1000;
    static int LONG_LOWER_BOUND = -1000;
    static int LONG_UPPER_BOUND = 1000;

    //-------------------------------------
    // Member variables
    //-------------------------------------

    static Byte[] mBytes = createBytes();
    static Character[] mCharacters = createCharacters();
    static Short[] mShorts = createShorts();
    static Integer[] mIntegers = createIntegers();
    static Long[] mLongs = createLongs();

    //-------------------------------------
    // Getting primitive values
    //-------------------------------------
    public static Boolean getBoolean(boolean pValue) {
        return
            pValue ?
            Boolean.TRUE :
            Boolean.FALSE;
    }

    //-------------------------------------
    public static Byte getByte(byte pValue) {
        if (pValue >= BYTE_LOWER_BOUND &&
            pValue <= BYTE_UPPER_BOUND) {
            return mBytes[((int) pValue) - BYTE_LOWER_BOUND];
        } else {
            return new Byte(pValue);
        }
    }

    //-------------------------------------
    public static Character getCharacter(char pValue) {
        if (pValue >= CHARACTER_LOWER_BOUND &&
            pValue <= CHARACTER_UPPER_BOUND) {
            return mCharacters[((int) pValue) - CHARACTER_LOWER_BOUND];
        } else {
            return new Character(pValue);
        }
    }

    //-------------------------------------
    public static Short getShort(short pValue) {
        if (pValue >= SHORT_LOWER_BOUND &&
            pValue <= SHORT_UPPER_BOUND) {
            return mShorts[((int) pValue) - SHORT_LOWER_BOUND];
        } else {
            return new Short(pValue);
        }
    }

    //-------------------------------------
    public static Integer getInteger(int pValue) {
        if (pValue >= INTEGER_LOWER_BOUND &&
            pValue <= INTEGER_UPPER_BOUND) {
            return mIntegers[((int) pValue) - INTEGER_LOWER_BOUND];
        } else {
            return new Integer(pValue);
        }
    }

    //-------------------------------------
    public static Long getLong(long pValue) {
        if (pValue >= LONG_LOWER_BOUND &&
            pValue <= LONG_UPPER_BOUND) {
            return mLongs[((int) pValue) - LONG_LOWER_BOUND];
        } else {
            return new Long(pValue);
        }
    }

    //-------------------------------------
    public static Float getFloat(float pValue) {
        return new Float(pValue);
    }

    //-------------------------------------
    public static Double getDouble(double pValue) {
        return new Double(pValue);
    }

    //-------------------------------------
    // Object class equivalents of primitive classes
    //-------------------------------------
    /**
     *
     * If the given class is a primitive class, returns the object
     * version of that class.  Otherwise, the class is just returned.
     **/
    public static Class getPrimitiveObjectClass(Class pClass) {
        if (pClass == Boolean.TYPE) {
            return Boolean.class;
        } else if (pClass == Byte.TYPE) {
            return Byte.class;
        } else if (pClass == Short.TYPE) {
            return Short.class;
        } else if (pClass == Character.TYPE) {
            return Character.class;
        } else if (pClass == Integer.TYPE) {
            return Integer.class;
        } else if (pClass == Long.TYPE) {
            return Long.class;
        } else if (pClass == Float.TYPE) {
            return Float.class;
        } else if (pClass == Double.TYPE) {
            return Double.class;
        } else {
            return pClass;
        }
    }

    //-------------------------------------
    // Initializing the cached values
    //-------------------------------------
    static Byte[] createBytes() {
        int len = BYTE_UPPER_BOUND - BYTE_LOWER_BOUND + 1;
        Byte[] ret = new Byte[len];
        byte val = (byte) BYTE_LOWER_BOUND;
        for (int i = 0; i < len; i++, val++) {
            ret[i] = new Byte(val);
        }
        return ret;
    }

    //-------------------------------------
    static Character[] createCharacters() {
        int len = CHARACTER_UPPER_BOUND - CHARACTER_LOWER_BOUND + 1;
        Character[] ret = new Character[len];
        char val = (char) CHARACTER_LOWER_BOUND;
        for (int i = 0; i < len; i++, val++) {
            ret[i] = new Character(val);
        }
        return ret;
    }

    //-------------------------------------
    static Short[] createShorts() {
        int len = SHORT_UPPER_BOUND - SHORT_LOWER_BOUND + 1;
        Short[] ret = new Short[len];
        short val = (short) SHORT_LOWER_BOUND;
        for (int i = 0; i < len; i++, val++) {
            ret[i] = new Short(val);
        }
        return ret;
    }

    //-------------------------------------
    static Integer[] createIntegers() {
        int len = INTEGER_UPPER_BOUND - INTEGER_LOWER_BOUND + 1;
        Integer[] ret = new Integer[len];
        int val = (int) INTEGER_LOWER_BOUND;
        for (int i = 0; i < len; i++, val++) {
            ret[i] = new Integer(val);
        }
        return ret;
    }

    //-------------------------------------
    static Long[] createLongs() {
        int len = LONG_UPPER_BOUND - LONG_LOWER_BOUND + 1;
        Long[] ret = new Long[len];
        long val = (long) LONG_LOWER_BOUND;
        for (int i = 0; i < len; i++, val++) {
            ret[i] = new Long(val);
        }
        return ret;
    }

    //-------------------------------------

}
