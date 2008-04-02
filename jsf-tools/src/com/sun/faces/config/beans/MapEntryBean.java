/*
 * $Id: MapEntryBean.java,v 1.3 2004/02/04 23:46:08 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;map-entry&gt; element.</p>
 */

public class MapEntryBean implements NullValueHolder {


    // -------------------------------------------------------------- Properties


    private String key;
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }


    private String value;
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }


    // ------------------------------------------------- NullValueHolder Methods

    private boolean nullValue = false;
    public boolean isNullValue() { return nullValue; }
    public void setNullValue(boolean nullValue) { this.nullValue = nullValue; }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


}
