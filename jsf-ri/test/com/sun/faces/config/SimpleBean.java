/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.config;

public class SimpleBean {

    private String simpleProperty;


    public SimpleBean() {
    }


    public String getSimpleProperty() {
        return simpleProperty;
    }


    public void setSimpleProperty(String simpleProperty) {
        this.simpleProperty = simpleProperty;
    }


    Integer intProp = null;


    public void setIntProperty(Integer newVal) {
        intProp = newVal;
    }


    public Integer getIntProperty() {
        return intProp;
    }


    public boolean getTrueValue() {
        return true;
    }


    public boolean getFalseValue() {
        return false;
    }

    private NonManagedBean nonManagedBean = null;
    public NonManagedBean getNonManagedBean() {
        return nonManagedBean;
    }
    public void setNonManagedBean(NonManagedBean nmb) {
        nonManagedBean = nmb;
    }
}
