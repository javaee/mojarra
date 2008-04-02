/*
 * $Id: MockApplicationFactory.java,v 1.3 2003/07/29 00:42:33 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;


public class MockApplicationFactory extends ApplicationFactory {


    private Application application = null;

    public Application getApplication() {
        if (application == null) {
            application = new MockApplication();
        }
        return (application);
    }

    public void setApplication(Application application) {
        this.application = application;
    }


}

