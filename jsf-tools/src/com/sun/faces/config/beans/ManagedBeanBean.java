/*
 * $Id: ManagedBeanBean.java,v 1.2 2004/01/27 20:13:40 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.Map;
import java.util.TreeMap;


/**
 * <p>Configuration bean for <code>&lt;managed-bean&gt; element.</p>
 */

public class ManagedBeanBean extends FeatureBean
    implements ListEntriesHolder, MapEntriesHolder {


    // -------------------------------------------------------------- Properties


    private String managedBeanClass;
    public String getManagedBeanClass() { return managedBeanClass; }
    public void setManagedBeanClass(String managedBeanClass)
    { this.managedBeanClass = managedBeanClass; }


    private String managedBeanName;
    public String getManagedBeanName() { return managedBeanName; }
    public void setManagedBeanName(String managedBeanName)
    { this.managedBeanName = managedBeanName; }


    private String managedBeanScope;
    public String getManagedBeanScope() { return managedBeanScope; }
    public void setManagedBeanScope(String managedBeanScope)
    { this.managedBeanScope = managedBeanScope; }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------- ListEntriesHolder Methods

    private ListEntriesBean listEntries;
    public ListEntriesBean getListEntries() { return listEntries; }
    public void setListEntries(ListEntriesBean listEntries)
    { this.listEntries = listEntries; }


    // ------------------------------------------- ManagedPropertyHolder Methods


    private Map managedProperties = new TreeMap();


    public void addManagedProperty(ManagedPropertyBean descriptor) {
        managedProperties.put(descriptor.getPropertyName(), descriptor);
    }


    public ManagedPropertyBean getManagedProperty(String name) {
        return ((ManagedPropertyBean) managedProperties.get(name));
    }


    public ManagedPropertyBean[] getManagedProperties() {
        ManagedPropertyBean results[] =
            new ManagedPropertyBean[managedProperties.size()];
        return ((ManagedPropertyBean[]) managedProperties.values().
                toArray(results));
    }


    public void removeManagedProperty(ManagedPropertyBean descriptor) {
        managedProperties.remove(descriptor.getPropertyName());
    }


    // ------------------------------------------------ MapEntriesHolder Methods

    private MapEntriesBean mapEntries;
    public MapEntriesBean getMapEntries() { return mapEntries; }
    public void setMapEntries(MapEntriesBean mapEntries)
    { this.mapEntries = mapEntries; }



    // ----------------------------------------------------------------- Methods


}
