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

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights 
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
import java.util.Map;

/**
 * <p>This is a test bean with a set of properties
 *
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: rlubke $
 */

public class Bean1 {


    //-------------------------------------
    // property bean1

    Bean1 mBean1;

    //-------------------------------------
    // property bean2

    Bean1 mBean2;

    //-------------------------------------
    // property boolean2

    Boolean mBoolean2;

    //-------------------------------------
    // property byte2

    Byte mByte2;

    //-------------------------------------
    // property char2

    Character mChar2;

    //-------------------------------------
    // property double2

    Double mDouble2;

    //-------------------------------------
    // property float2

    Float mFloat2;

    //-------------------------------------
    // property int2

    Integer mInt2;

    //-------------------------------------
    // property list1

    List mList1;

    //-------------------------------------
    // property long2

    Long mLong2;

    //-------------------------------------
    // property map1

    Map mMap1;

    //-------------------------------------
    // property short2

    Short mShort2;

    //-------------------------------------
    // property errorInGetter

    String mErrorInGetter;

    //-------------------------------------
    // property noGetter

    String mNoGetter;

    //-------------------------------------
    // property string1

    String mString1;

    //-------------------------------------
    // property string2

    String mString2;

    //-------------------------------------
    // property stringArray1

    String[] mStringArray1;

    //-------------------------------------
    // Properties
    //-------------------------------------
    // property boolean1

    boolean mBoolean1;

    //-------------------------------------
    // property byte1

    byte mByte1;

    //-------------------------------------
    // property char1

    char mChar1;

    //-------------------------------------
    // property double1

    double mDouble1;

    //-------------------------------------
    // property float1

    float mFloat1;

    //-------------------------------------
    // property int1

    int mInt1;

    //-------------------------------------
    // property long1

    long mLong1;

    //-------------------------------------
    // property short1

    short mShort1;


    // ------------------------------------------------------------ Constructors


    //-------------------------------------
    // Member variables
    //-------------------------------------

    //-------------------------------------
    /**
     * Constructor
     */
    public Bean1() {
    }


    // ---------------------------------------------------------- Public Methods


    public Bean1 getBean1() {

        return mBean1;

    }


    public Bean1 getBean2() {

        return mBean2;

    }


    public boolean getBoolean1() {

        return mBoolean1;

    }


    public Boolean getBoolean2() {

        return mBoolean2;

    }


    public byte getByte1() {

        return mByte1;

    }


    public Byte getByte2() {

        return mByte2;

    }


    public char getChar1() {

        return mChar1;

    }


    public Character getChar2() {

        return mChar2;

    }


    public double getDouble1() {

        return mDouble1;

    }


    public Double getDouble2() {

        return mDouble2;

    }


    public String getErrorInGetter() {

        throw new NullPointerException("Error!");

    }


    public float getFloat1() {

        return mFloat1;

    }


    public Float getFloat2() {

        return mFloat2;

    }


    //-------------------------------------
    // property indexed1

    public String getIndexed1(int pIndex) {

        return mStringArray1[pIndex];

    }


    public int getInt1() {

        return mInt1;

    }


    public Integer getInt2() {

        return mInt2;

    }


    public List getList1() {

        return mList1;

    }


    public long getLong1() {

        return mLong1;

    }


    public Long getLong2() {

        return mLong2;

    }


    public Map getMap1() {

        return mMap1;

    }


    public short getShort1() {

        return mShort1;

    }


    public Short getShort2() {

        return mShort2;

    }


    public String getString1() {

        return mString1;

    }


    public String getString2() {

        return mString2;

    }


    public String[] getStringArray1() {

        return mStringArray1;

    }


    public void setBean1(Bean1 pBean1) {

        mBean1 = pBean1;

    }


    public void setBean2(Bean1 pBean2) {

        mBean2 = pBean2;

    }


    public void setBoolean1(boolean pBoolean1) {

        mBoolean1 = pBoolean1;

    }


    public void setBoolean2(Boolean pBoolean2) {

        mBoolean2 = pBoolean2;

    }


    public void setByte1(byte pByte1) {

        mByte1 = pByte1;

    }


    public void setByte2(Byte pByte2) {

        mByte2 = pByte2;

    }


    public void setChar1(char pChar1) {

        mChar1 = pChar1;

    }


    public void setChar2(Character pChar2) {

        mChar2 = pChar2;

    }


    public void setDouble1(double pDouble1) {

        mDouble1 = pDouble1;

    }


    public void setDouble2(Double pDouble2) {

        mDouble2 = pDouble2;

    }


    public void setFloat1(float pFloat1) {

        mFloat1 = pFloat1;

    }


    public void setFloat2(Float pFloat2) {

        mFloat2 = pFloat2;

    }


    public void setInt1(int pInt1) {

        mInt1 = pInt1;

    }


    public void setInt2(Integer pInt2) {

        mInt2 = pInt2;

    }


    public void setList1(List pList1) {

        mList1 = pList1;

    }


    public void setLong1(long pLong1) {

        mLong1 = pLong1;

    }


    public void setLong2(Long pLong2) {

        mLong2 = pLong2;

    }


    public void setMap1(Map pMap1) {

        mMap1 = pMap1;

    }


    public void setNoGetter(String pNoGetter) {

        mNoGetter = pNoGetter;

    }


    public void setShort1(short pShort1) {

        mShort1 = pShort1;

    }


    public void setShort2(Short pShort2) {

        mShort2 = pShort2;

    }


    public void setString1(String pString1) {

        mString1 = pString1;

    }


    public void setString2(String pString2) {

        mString2 = pString2;

    }


    public void setStringArray1(String[] pStringArray1) {

        mStringArray1 = pStringArray1;

    }


    //-------------------------------------

}
