/*
 * $Id: ConverterBean.java,v 1.1 2004/12/02 18:42:27 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
