/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;

import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;

public class NewApplicationFactory extends ApplicationFactory {
    
    private ApplicationFactory oldFactory = null;

    private NewApplication newApp = null;
    
    public NewApplicationFactory(ApplicationFactory yourOldFactory) {
	oldFactory = yourOldFactory;
    }
    
    public Application getApplication() {
	if (null == newApp) {
	    newApp = new NewApplication(oldFactory.getApplication());
	}
	return newApp;
    }
    
    public void setApplication(Application application) {
	newApp = (NewApplication) application;
    }

    public String toString() {
	return "NewApplicationFactory";
    }

}
