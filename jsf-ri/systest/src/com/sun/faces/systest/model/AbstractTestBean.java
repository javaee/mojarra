/*
 * $Id: AbstractTestBean.java,v 1.1 2004/01/31 01:10:19 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;


/**
 * <p>Test JavaBean for managed object creation facility.</p>
 */

public abstract class AbstractTestBean {

    private String stringProperty = "String Property";
    public String getStringProperty() { return this.stringProperty; }
    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

}
