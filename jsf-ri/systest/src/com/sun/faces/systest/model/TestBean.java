/*
 * $Id: TestBean.java,v 1.1 2003/05/10 00:48:05 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;


/**
 * <p>Test JavaBean for managed object creation facility.</p>
 */

public class TestBean {


    private boolean booleanProperty = true;
    public boolean getBooleanProperty() {
        return (this.booleanProperty);
    }
    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }


    private byte byteProperty = 12;
    public byte getByteProperty() {
        return (this.byteProperty);
    }
    public void setByteProperty(byte byteProperty) {
        this.byteProperty = byteProperty;
    }


    private double doubleProperty = 123.45;
    public double getDoubleProperty() {
        return (this.doubleProperty);
    }
    public void setDoubleProperty(double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }


    private float floatProperty = (float) 12.34;
    public float getFloatProperty() {
        return (this.floatProperty);
    }
    public void setFloatProperty(float floatProperty) {
        this.floatProperty = floatProperty;
    }


    private int intProperty = 123;
    public int getIntProperty() {
        return (this.intProperty);
    }
    public void setIntProperty(int intProperty) {
        this.intProperty = intProperty;
    }


    private long longProperty = 12345;
    public long getLongProperty() {
        return (this.longProperty);
    }
    public void setLongProperty(long longProperty) {
        this.longProperty = longProperty;
    }


    private short shortProperty = 1234;
    public short getShortProperty() {
        return (this.shortProperty);
    }
    public void setShortProperty(short shortProperty) {
        this.shortProperty = shortProperty;
    }


    private String stringProperty = "This is a String property";
    public String getStringProperty() {
        return (this.stringProperty);
    }
    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }



}
