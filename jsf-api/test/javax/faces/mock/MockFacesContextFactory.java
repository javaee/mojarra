/*
 * $Id: MockFacesContextFactory.java,v 1.2 2004/02/04 23:39:11 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;

public class MockFacesContextFactory extends FacesContextFactory {
    public MockFacesContextFactory(FacesContextFactory oldImpl) {
	System.setProperty(FactoryFinder.FACES_CONTEXT_FACTORY, 
			   this.getClass().getName());
    }
    public MockFacesContextFactory() {}
    
    public FacesContext getFacesContext(Object context, Object request,
					Object response, 
					Lifecycle lifecycle) throws FacesException {
	return new MockFacesContext();
    }
}

