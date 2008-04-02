/*
 * $Id: ConfigManagedBean.java,v 1.2 2003/04/29 20:51:33 eburns Exp $
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


/**
 * <p>Config Bean for a Managed Bean .</p>
 */
public class ConfigManagedBean extends ConfigFeature implements Cloneable {

    private String managedBeanId;
    private String managedBeanClass;
    private String managedBeanScope;
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

    public void addProperty(ConfigManagedBeanProperty property) {
        if (properties == null) {
            properties = new HashMap();
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
}
