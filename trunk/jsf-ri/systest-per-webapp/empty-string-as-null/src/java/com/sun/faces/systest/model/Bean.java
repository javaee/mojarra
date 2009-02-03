package com.sun.faces.systest.model;

import javax.faces.event.ValueChangeEvent;

public class Bean {

    Object integerPropertySet = "";
    Integer integerProperty;
    Object stringPropertySet = "";
    String stringProperty;
    boolean vceFired;
    boolean vce2Fired;

    public Integer getIntegerProperty() {
        return integerProperty;
    }

    public void setIntegerProperty(Integer integerProperty) {
        integerPropertySet = integerProperty;
        this.integerProperty = integerProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        stringPropertySet = stringProperty;
        this.stringProperty = stringProperty;
    }

    public boolean isVceFired() {
        boolean tmp = vceFired;
        vceFired = false;
        return tmp;
    }

    public boolean isVce2Fired() {
        boolean tmp = vce2Fired;
        vce2Fired = false;
        return tmp;
    }

    public boolean isStringNull() {
        boolean isNull = (this.stringPropertySet == null);
        this.stringPropertySet = "";
        return (isNull);
    }

    public boolean isIntegerNull() {
        boolean isNull = (this.integerPropertySet == null);
        this.integerPropertySet = "";
        return (isNull);
    }

    public void valueChange(ValueChangeEvent event) {
        vceFired = true;
    }

    public void valueChange2(ValueChangeEvent event) {
        vce2Fired = true;
    }
}
