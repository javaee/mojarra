/*
 * $Id: ConfigNavigationRule.java,v 1.3 2003/08/22 16:49:41 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


/**
 * <p>Config Bean for a Navigation Rule .</p>
 */
public class ConfigNavigationRule {

    private String fromViewId = null;

    public String getFromViewId() {
        return (this.fromViewId);
    }
    public void setFromViewId(String fromViewId) {
        this.fromViewId = fromViewId;
    }
}
