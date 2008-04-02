/*
 * $Id: ListEntriesBean.java,v 1.2 2004/01/27 20:13:38 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.ArrayList;
import java.util.List;


/**
 * <p>Configuration bean for <code>&lt;list-entries&gt; element.</p>
 */

public class ListEntriesBean {


    // -------------------------------------------------------------- Properties


    private String valueClass;
    public String getValueClass() { return valueClass; }
    public void setValueClass(String valueClass)
    { this.valueClass = valueClass; }


    // Set of unconverted String and/or null entries for the list
    private List values = new ArrayList();
    public String[] getValues() {
        String results[] = new String[values.size()];
        return ((String[]) values.toArray(results));
    }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


    public void addNullValue() {
        values.add((String) null);
    }


    public void addValue(String value) {
        values.add(value);
    }


}
