/*
 * $Id: ConfigManagedPropertyMap.java,v 1.2 2003/04/29 20:51:34 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

public class ConfigManagedPropertyMap implements Cloneable {

    public static final int VALUE = 0;
    public static final int VALUE_REF = 1;
    public static final int NULL_VALUE = 2;

    private String key = null;
    private int valueCategory = -1;
    private String value = null;

    public int getValueCategory() {
        return valueCategory;
    }
    public void setValueCategory(int valueCategory) {
        this.valueCategory = valueCategory;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public Object clone() {
        ConfigManagedPropertyMap cmpm = null;
        try {
            cmpm = (ConfigManagedPropertyMap)super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return cmpm;
    }
}

