/*
 * $Id: ListEntriesBean.java,v 1.3 2004/02/04 23:46:07 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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
