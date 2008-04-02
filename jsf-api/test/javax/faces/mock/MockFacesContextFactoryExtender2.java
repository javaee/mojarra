/*
 * $Id: MockFacesContextFactoryExtender2.java,v 1.1 2003/09/02 03:13:01 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContextFactory;

public class MockFacesContextFactoryExtender2 extends MockFacesContextFactory {
    public MockFacesContextFactoryExtender2() {}
    public MockFacesContextFactoryExtender2(FacesContextFactory oldImpl) {
	System.setProperty(FactoryFinder.FACES_CONTEXT_FACTORY, 
			   this.getClass().getName());
	System.setProperty("oldImpl", oldImpl.getClass().getName());
    }
}

