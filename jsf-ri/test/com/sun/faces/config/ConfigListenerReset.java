/*
 * $Id: ConfigListenerReset.java,v 1.1 2003/09/12 19:48:46 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

// This class is used by the test framework to manually
// reset the Set of Classloaders used by ConfigListener.
// The Set of loaders is used to ensure that the contextInitialized method
// only executes once per webapp.

public class ConfigListenerReset {

    public void resetConfigListenerSet() {
        ConfigListener.clearLoaderSet();
    }
}
