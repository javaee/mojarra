/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.lifecycle;

import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;

public class ApplicationFactoryWrapper extends ApplicationFactory {
    
    private ApplicationFactory oldFactory = null;
    
    public ApplicationFactoryWrapper(ApplicationFactory yourOldFactory) {
	oldFactory = yourOldFactory;
    }
    
    public Application getApplication() {
	return oldFactory.getApplication();
    }
    
    public void setApplication(Application application) {
	oldFactory.setApplication(application);
    }

    public String toString() {
	return "ApplicationFactoryWrapper";
    }

}
