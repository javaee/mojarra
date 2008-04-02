/*
 * $Id: ConfigLifecycle.java,v 1.1 2003/09/26 14:26:49 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

/**
 * <p>Config Bean for Lifecycle Rule .</p>
 */
public class ConfigLifecycle {

    private String phaseListener = null;

    public String getPhaseListener() {
        return (this.phaseListener);
    }
    public void setPhaseListener(String phaseListener) {
        this.phaseListener = phaseListener;
    }

    public String toString() {
	return ("Phase Listener: " + getPhaseListener());
    }
}
