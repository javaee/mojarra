/*
 * $Id: MockLifecycleFactory.java,v 1.2 2004/02/04 23:39:12 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.Iterator;
import java.util.ArrayList;
import javax.faces.FactoryFinder;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;

public class MockLifecycleFactory extends LifecycleFactory {
    public MockLifecycleFactory(LifecycleFactory oldImpl) {
	System.setProperty(FactoryFinder.LIFECYCLE_FACTORY, 
			   this.getClass().getName());
    }
    public MockLifecycleFactory() {}

    public void addLifecycle(String lifecycleId,
			     Lifecycle lifecycle) {}
    public Lifecycle getLifecycle(String lifecycleId) {
	return new MockLifecycle();
    }

    public Iterator getLifecycleIds() {
	ArrayList result = new ArrayList(1);
	result.add(LifecycleFactory.DEFAULT_LIFECYCLE);
	return result.iterator();
    }

    
}
