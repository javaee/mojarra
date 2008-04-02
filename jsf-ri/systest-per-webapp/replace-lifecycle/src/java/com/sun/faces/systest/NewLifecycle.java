/*
 * $Id: NewLifecycle.java,v 1.2 2005/07/25 21:07:57 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;

import com.sun.faces.lifecycle.LifecycleImpl;

public class NewLifecycle extends LifecycleImpl {
    
    private String lifecycleId = null;

    public NewLifecycle(String lifecycleId) {
        this.lifecycleId = lifecycleId;
    }
    
    public String getLifecycleId() {
        return lifecycleId;
    }

}
