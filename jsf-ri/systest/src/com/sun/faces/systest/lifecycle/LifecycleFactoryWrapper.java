/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.lifecycle;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.FacesException;
import java.util.Iterator;

public class LifecycleFactoryWrapper extends LifecycleFactory {
    
    private LifecycleFactory oldFactory = null;
    
    public LifecycleFactoryWrapper(LifecycleFactory yourOldFactory) {
	oldFactory = yourOldFactory;
    }
    
    public void addLifecycle(String lifecycleId,
			     Lifecycle lifecycle) {
	oldFactory.addLifecycle(lifecycleId, lifecycle);
    }

    public Lifecycle getLifecycle(String lifecycleId) {
	return oldFactory.getLifecycle(lifecycleId);
    }

    public Iterator getLifecycleIds() {
	return oldFactory.getLifecycleIds();
    }

    public String toString() {
	return "LifecycleFactoryWrapper";
    }


    
}
