/*
 * $Id: ConfigManagedBeanPropertyValue.java,v 1.2 2003/04/29 20:51:34 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

public class ConfigManagedBeanPropertyValue implements Cloneable {
    public static final int VALUE_CLASS = 0;
    public static final int VALUE = 1;
    public static final int VALUE_REF = 2;
    public static final int NULL_VALUE = 3;

    private int valueCategory = -1;
    private String value = null;

    public int getValueCategory() {
        return valueCategory;
    }
    public void setValueCategory(int valueCategory) {
        this.valueCategory = valueCategory;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value= value;
    }

    public Object clone() {
        ConfigManagedBeanPropertyValue cmbpv = null;
        try {
            cmbpv = (ConfigManagedBeanPropertyValue)super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return cmbpv;
    }
}

