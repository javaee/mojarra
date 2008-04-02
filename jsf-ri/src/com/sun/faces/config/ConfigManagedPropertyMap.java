/*
 * $Id: ConfigManagedPropertyMap.java,v 1.5 2003/08/19 14:50:51 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import javax.faces.FacesException;
import com.sun.faces.util.Util;
import org.apache.commons.beanutils.ConvertUtils;

public class ConfigManagedPropertyMap implements Cloneable {

    public static final int VALUE = 0;
    public static final int VALUE_REF = 1;
    public static final int NULL_VALUE = 2;

    private Object key = null;
    private int valueCategory = -1;
    private Object value = null;

    public int getValueCategory() {
        return valueCategory;
    }
    public void setValueCategory(int valueCategory) {
        this.valueCategory = valueCategory;
    }

    public Object getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void convertKey(Class valueClass) {
	try {
            key = ConvertUtils.convert((String) key, valueClass);
	} catch (Exception ex) {
            //value could not be converted. Default is String.
            Object[] obj = new Object[2];
            obj[0] = key; 
            obj[1] = valueClass;
            throw new FacesException(Util.getExceptionMessage(Util.CANT_CONVERT_VALUE_ERROR_MESSAGE_ID, obj), ex);
        }
    }

    public Object getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void convertValue(Class valueClass) {
        if (valueCategory == VALUE) {
            try {
                value = ConvertUtils.convert((String) value, valueClass);
            } catch (Exception ex) {
//value could not be converted. Default is String.
                Object[] obj = new Object[2];
                obj[0] = value;
                obj[1] = valueClass;
                throw new FacesException(Util.getExceptionMessage(Util.CANT_CONVERT_VALUE_ERROR_MESSAGE_ID, obj), ex);
            }
        }
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

