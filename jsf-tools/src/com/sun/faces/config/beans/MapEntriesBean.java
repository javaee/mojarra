/*
 * $Id: MapEntriesBean.java,v 1.3 2004/02/04 23:46:08 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.ArrayList;
import java.util.List;


/**
 * <p>Configuration bean for <code>&lt;map-entries&gt; element.</p>
 */

public class MapEntriesBean {


    // -------------------------------------------------------------- Properties


    private String keyClass;
    public String getKeyClass() { return keyClass; }
    public void setKeyClass(String keyClass)
    { this.keyClass = keyClass; }


    private List mapEntries = new ArrayList();
    public MapEntryBean[] getMapEntries() {
        MapEntryBean results[] =
            new MapEntryBean[mapEntries.size()];
        return ((MapEntryBean[]) mapEntries.toArray(results));
    }


    private String valueClass;
    public String getValueClass() { return valueClass; }
    public void setValueClass(String valueClass)
    { this.valueClass = valueClass; }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


    public void addMapEntry(MapEntryBean mapEntry) {
        mapEntries.add(mapEntry);
    }


}
