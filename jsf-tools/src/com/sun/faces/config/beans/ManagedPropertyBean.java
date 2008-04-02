/*
 * $Id: ManagedPropertyBean.java,v 1.2 2004/01/27 20:13:40 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;managed-property&gt; element.</p>
 */

public class ManagedPropertyBean extends FeatureBean
    implements ListEntriesHolder, MapEntriesHolder, NullValueHolder {


    // -------------------------------------------------------------- Properties


    private String propertyClass;
    public String getPropertyClass() { return propertyClass; }
    public void setPropertyClass(String propertyClass)
    { this.propertyClass = propertyClass; }


    private String propertyName;
    public String getPropertyName() { return propertyName; }
    public void setPropertyName(String propertyName)
    { this.propertyName = propertyName; }


    private String value;
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }


    // ----------------------------------------------- ListEntriesHolder Methods

    private ListEntriesBean listEntries;
    public ListEntriesBean getListEntries() { return listEntries; }
    public void setListEntries(ListEntriesBean listEntries)
    { this.listEntries = listEntries; }


    // ------------------------------------------------ MapEntriesHolder Methods

    private MapEntriesBean mapEntries;
    public MapEntriesBean getMapEntries() { return mapEntries; }
    public void setMapEntries(MapEntriesBean mapEntries)
    { this.mapEntries = mapEntries; }


    // ---------------------------------------------- NullValueHolder Properties


    private boolean nullValue = false;
    public boolean isNullValue() { return nullValue; }
    public void setNullValue(boolean nullValue) { this.nullValue = nullValue; }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


}
