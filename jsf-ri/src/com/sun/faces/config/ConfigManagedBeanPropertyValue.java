/*
 * $Id: ConfigManagedBeanPropertyValue.java,v 1.6 2003/12/17 15:13:30 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import javax.faces.FacesException;
import com.sun.faces.util.Util;
import org.apache.commons.beanutils.ConvertUtils;

public class ConfigManagedBeanPropertyValue implements Cloneable {
    public static final int VALUE_CLASS = 0;
    public static final int VALUE = 1;
    public static final int VALUE_BINDING = 2;
    public static final int NULL_VALUE = 3;

    private int valueCategory = -1;
    private Object value = null;
    private Class type;

    public int getValueCategory() {
        return valueCategory;
    }
    public void setValueCategory(int valueCategory) {        
        this.valueCategory = valueCategory;
    }

    public Object getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
	if (null != value && Util.isVBExpression(value)) {
	    this.valueCategory = VALUE_BINDING;
	}
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {      
        this.type = type;
    }
    
    public void convertValue(Class type) {
        this.type = type;
        convertValue();
    }

    public void convertValue() {
        // only perform conversion if type is VALUE
        if (valueCategory == VALUE) {         
            try {
                value = ConvertUtils.convert((String) value, type);
            } catch (Exception ex) {
                //value could not be converted. Default is String.
                Object[] obj = new Object[2];
                obj[0] = value;
                obj[1] = type;
                throw new FacesException(Util.getExceptionMessage(Util.CANT_CONVERT_VALUE_ERROR_MESSAGE_ID, obj), ex);
            }
        }
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

