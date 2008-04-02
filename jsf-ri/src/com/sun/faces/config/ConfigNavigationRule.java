/*
 * $Id: ConfigNavigationRule.java,v 1.2 2003/07/08 15:38:30 eburns Exp $
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

    private String fromTreeId = null;

    public String getFromTreeId() {
        return (this.fromTreeId);
    }
    public void setFromTreeId(String fromTreeId) {
        this.fromTreeId = fromTreeId;
    }
}
