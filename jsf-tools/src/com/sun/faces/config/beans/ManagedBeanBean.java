/*
 * $Id: ManagedBeanBean.java,v 1.4 2004/04/28 01:41:31 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


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


    private List managedProperties = new ArrayList();


    public void addManagedProperty(ManagedPropertyBean descriptor) {
        managedProperties.add(descriptor);
    }


    public ManagedPropertyBean getManagedProperty(String name) {
	Iterator iter = managedProperties.iterator();
	ManagedPropertyBean cur = null;
	String  curName = null;
	while (iter.hasNext()) {
	    cur = (ManagedPropertyBean) iter.next();
	    if (null == cur) {
		continue;
	    }
	    curName = cur.getPropertyName();
	    // if the name is null, and we're looking for null
	    if (null == curName && null == name) {
		return cur;
	    }
	    // not a match
	    if (null == curName || null == name) {
		continue;
	    }
	    // guaranteed that both are non-null
	    if (curName.equals(name)) {
		return cur;
	    }
	}
	    
        return null;
    }


    public ManagedPropertyBean[] getManagedProperties() {
        ManagedPropertyBean results[] =
            new ManagedPropertyBean[managedProperties.size()];
        return ((ManagedPropertyBean[]) managedProperties.toArray(results));
    }


    public void removeManagedProperty(ManagedPropertyBean descriptor) {
	if (null == descriptor) {
	    return;
	}
	ManagedPropertyBean toRemove = 
	    getManagedProperty(descriptor.getPropertyName());
	if (null != toRemove) {
	    managedProperties.remove(toRemove);
	}
    }

    // ------------------------------------------------ MapEntriesHolder Methods

    private MapEntriesBean mapEntries;
    public MapEntriesBean getMapEntries() { return mapEntries; }
    public void setMapEntries(MapEntriesBean mapEntries)
    { this.mapEntries = mapEntries; }



    // ----------------------------------------------------------------- Methods


}
