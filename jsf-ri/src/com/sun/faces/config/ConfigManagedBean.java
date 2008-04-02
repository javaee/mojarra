/*
 * $Id: ConfigManagedBean.java,v 1.4 2003/05/04 21:39:37 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.faces.util.Util;

import javax.faces.FacesException;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.Introspector;
import java.beans.IntrospectionException;

/**
 * <p>Config Bean for a Managed Bean .</p>
 */
public class ConfigManagedBean extends ConfigFeature implements Cloneable {

    private String managedBeanId;
    private String managedBeanClass;
    private String managedBeanScope;
    private String managedBeanCreate;

    private HashMap properties = null;

    public String getManagedBeanId() {
        return (this.managedBeanId);
    }
    public void setManagedBeanId(String managedBeanId) {
        this.managedBeanId = managedBeanId;
    }

    public String getManagedBeanClass() {
        return (this.managedBeanClass);
    }
    public void setManagedBeanClass(String managedBeanClass) {
        this.managedBeanClass = managedBeanClass;
    }

    public String getManagedBeanScope() {
        return (this.managedBeanScope);
    }
    public void setManagedBeanScope(String managedBeanScope) {
        this.managedBeanScope = managedBeanScope;
    }

    public String getManagedBeanCreate() {
        return (this.managedBeanCreate);
    }
    public void setManagedBeanCreate(String managedBeanCreate) {
        this.managedBeanCreate = managedBeanCreate;
    }
    
    public void addProperty(ConfigManagedBeanProperty property) throws FacesException {
        if (properties == null) {
            properties = new HashMap();
        }
        Class propertyType = getPropertyType(property);
        if (propertyType != null) {
            property.convertValue(propertyType.getName());
        }
        properties.put(property.getPropertyName(), property);
    }
    public Map getProperties() {
        if (properties == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (properties);
        }
    }

    public Object clone() {
        ConfigManagedBean cmb = null;
        try {
            cmb = (ConfigManagedBean)super.clone();
            if (properties != null) {
                cmb.properties = (HashMap)properties.clone();
            }
        } catch (CloneNotSupportedException e) {
        }
        return cmb;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ID:"+getManagedBeanId()+
            "\nCLASS:"+getManagedBeanClass()+
            "\nSCOPE:"+getManagedBeanScope()+
            "\nCREATE:"+getManagedBeanCreate()+
            "\nPROPERTIES...");
        if (properties.size() > 0) {
            Iterator iter = properties.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String)iter.next();
                ConfigManagedBeanProperty cmbp = (ConfigManagedBeanProperty)properties.get(key);
                String name = cmbp.getPropertyName();
                sb.append("\n    NAME:"+cmbp.getPropertyName());
                if (cmbp.hasValuesArray()) {
                    sb.append("\n    VALUES:");
                    List values = cmbp.getValues();
                    for (int i=0; i<values.size(); i++) {
                        ConfigManagedBeanPropertyValue cmbpv = 
                            (ConfigManagedBeanPropertyValue)values.get(i);
                        sb.append("\n      VALUE:CATEGORY:"+cmbpv.getValueCategory()+
                            " : VALUE:"+cmbpv.getValue());
                    }
                } else if (cmbp.hasMapEntries()) {
                    sb.append("\n    MAP KEY CLASS:"+cmbp.getMapKeyClass());
                    sb.append("\n    MAP VALUE CLASS:"+cmbp.getMapValueClass());
                    sb.append("\n    MAP ENTRIES:");
                    List mapEntries = cmbp.getMapEntries();
                    for (int i=0; i<mapEntries.size(); i++) {
                        ConfigManagedPropertyMap cmpm = 
                            (ConfigManagedPropertyMap)mapEntries.get(i);
                        sb.append("\n      KEY:"+cmpm.getKey()+
                            " : VALUE:CATEGORY:"+cmpm.getValueCategory()+
                            " : VALUE:"+cmpm.getValue());
                    }
                } else {
                    ConfigManagedBeanPropertyValue cmbpv = cmbp.getValue();
                    sb.append("\n    VALUE:CATEGORY:"+cmbpv.getValueCategory()+
                        " : VALUE:"+cmbpv.getValue());
                }
            }
        }
        return sb.toString();
    }

    private Class getPropertyType(ConfigManagedBeanProperty property) 
        throws FacesException {

        Class propertyType = null;

        // indexed and mapped properties have explicit types
        if (!property.hasValuesArray() && !property.hasMapEntries()) {
            PropertyDescriptor descs[] = null;
            try {
                Class clazz = Util.loadClass
                    (managedBeanClass, this);
                BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
                descs = beanInfo.getPropertyDescriptors();
            } catch (ClassNotFoundException ex) {
                Object[] obj = new Object[1];
                obj[0] = managedBeanClass;
                throw new FacesException(Util.getExceptionMessage(Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, obj), ex);
            } catch (IntrospectionException ex) {
                Object[] obj = new Object[1];
                obj[0] = managedBeanClass;
                throw new FacesException(Util.getExceptionMessage(Util.CANT_INTROSPECT_CLASS_ERROR_MESSAGE_ID, obj), ex);
            }
            PropertyDescriptor desc = null;

            for (int i = 0; i < descs.length; i++) {
               if (property.getPropertyName().equals(descs[i].getName())) {
                   desc = descs[i];
                   break;
               }
            }
            if (desc == null) {
                Object[] obj = new Object[1];
                obj[0] = managedBeanClass;
                throw new FacesException(Util.getExceptionMessage(Util.CANT_INTROSPECT_CLASS_ERROR_MESSAGE_ID, obj));
            }

            boolean isIndexed;
            if (desc instanceof IndexedPropertyDescriptor) {
               isIndexed = true;
               propertyType = 
                   ((IndexedPropertyDescriptor) desc).getIndexedPropertyType();
            } else {
               isIndexed = false;
               propertyType = desc.getPropertyType();
            }
        }

        return propertyType;
    }

}
