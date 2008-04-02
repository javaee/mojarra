/*
 * $Id: MockFacesContext.java,v 1.10 2003/07/28 22:22:33 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.application.Application;
import com.sun.faces.context.FacesContextImpl;

// Mock Object for FacesContext
public class MockFacesContext extends FacesContextImpl {


    public MockFacesContext() {
        super();
    }


    public MockFacesContext(ExternalContext econtext, Lifecycle lifecycle) {
        super(econtext, lifecycle);
        setCurrentInstance(this);
    }

    protected Application app = null;

    public void setApplication(Application newApp) {
	app = newApp;
    }
    public Application getApplication() {
	return app;
    }

}
