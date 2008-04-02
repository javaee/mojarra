/*
 * $Id: ConverterBean.java,v 1.2 2005/08/22 22:10:41 ofung Exp $
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

import com.sun.faces.systest.TestConverter01;

import javax.faces.event.AbortProcessingException;
import javax.faces.convert.Converter;


public class ConverterBean extends Object {

    public ConverterBean() {
    }

    private Converter converter = null;
    public Converter getConverter() {
        if (converter == null) {
            return new TestConverter01();
        }
        return converter;
    }
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    private Converter dateTimeConverter = null;
    public Converter getDateTimeConverter() {
        if (dateTimeConverter == null) {
            return new javax.faces.convert.DateTimeConverter();
        }
        return dateTimeConverter;
    }
    public void setDateTimeConverter(Converter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    private Converter doubleConverter = null;
    public Converter getDoubleConverter() {
        if (doubleConverter == null) {
            return new javax.faces.convert.DoubleConverter();
        }
        return doubleConverter;
    }
    public void setDoubleConverter(Converter doubleConverter) {
        this.doubleConverter = doubleConverter;
    }

    private Converter numberConverter = null;
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
