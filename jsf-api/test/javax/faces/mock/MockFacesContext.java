/*
 * $Id: MockFacesContext.java,v 1.9 2003/07/20 00:41:45 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
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


}
