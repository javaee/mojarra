/*
 * $Id: ConverterBean.java,v 1.3 2006/03/29 22:38:52 rlubke Exp $
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

package com.sun.faces.systest.model;

import javax.faces.convert.Converter;

import com.sun.faces.systest.TestConverter01;


public class ConverterBean extends Object {


    private Converter converter = null;

    private Converter dateTimeConverter = null;

    private Converter doubleConverter = null;

    private Converter numberConverter = null;

    // ------------------------------------------------------------ Constructors


    public ConverterBean() {
    }

    // ---------------------------------------------------------- Public Methods


    public Converter getConverter() {

        if (converter == null) {
            return new TestConverter01();
        }
        return converter;

    }


    public void setConverter(Converter converter) {

        this.converter = converter;

    }


    public Converter getDateTimeConverter() {

        if (dateTimeConverter == null) {
            return new javax.faces.convert.DateTimeConverter();
        }
        return dateTimeConverter;

    }


    public void setDateTimeConverter(Converter dateTimeConverter) {

        this.dateTimeConverter = dateTimeConverter;

    }


    public Converter getDoubleConverter() {

        if (doubleConverter == null) {
            return new javax.faces.convert.DoubleConverter();
        }
        return doubleConverter;

    }


    public void setDoubleConverter(Converter doubleConverter) {

        this.doubleConverter = doubleConverter;

    }


    public Converter getNumberConverter() {

        if (numberConverter == null) {
            return new javax.faces.convert.NumberConverter();
        }
        return numberConverter;

    }


    public void setNumberConverter(Converter numberConverter) {

        this.numberConverter = numberConverter;

    }

}
