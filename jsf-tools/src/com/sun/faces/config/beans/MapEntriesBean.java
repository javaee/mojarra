/*
 * $Id: MapEntriesBean.java,v 1.2 2004/01/27 20:13:40 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
