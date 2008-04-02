/*
 * $Id: TestBeanSubclass.java,v 1.1 2004/01/31 01:10:20 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;


/**
 * <p>Test JavaBean for managed object creation facility.</p>
 */

public class TestBeanSubclass extends AbstractTestBean {

    private String extraProperty = "Extra Property";
    public String getExtraProperty() { return this.extraProperty; }
    public void setExtraProperty(String extraProperty) {
        this.extraProperty = extraProperty;
    }

}
