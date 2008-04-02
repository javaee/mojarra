/*
 * $Id: FactoryFinderTestCase2.java,v 1.1 2003/09/02 03:12:57 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;


import java.io.IOException;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Unit tests for {@link UISelectBooleanBase}.</p>
 */

public class FactoryFinderTestCase2 extends TestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public FactoryFinderTestCase2(String name) {
        super(name);
    }

    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() throws Exception {
        super.setUp();
	for (int i = 0, len = FactoryFinderTestCase.FACTORIES.length; i < len; i++) {
	    System.getProperties().remove(FactoryFinderTestCase.FACTORIES[i][0]);
	}
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(FactoryFinderTestCase2.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() throws Exception {
        super.tearDown();
	FactoryFinder.releaseFactories();
	for (int i = 0, len = FactoryFinderTestCase.FACTORIES.length; i < len; i++) {
	    System.getProperties().remove(FactoryFinderTestCase.FACTORIES[i][0]);
	}
    }


    // ------------------------------------------------- Individual Test Methods
    /**
     * <p>In the absence of webapp faces-config.xml and
     * META-INF/services, verify that the overrides specified in
     * the implementation faces-config.xml take precedence.</p>
     */

    public void testJSFImplCase() throws Exception {
	Object factory = null;
	Class clazz = null;

	FactoryFinder.releaseFactories();
	int len, i = 0;
	
	// this testcase only simulates the "faces implementation
	// specific" part

	for (i = 0, len = FactoryFinderTestCase.FACTORIES.length; i < len; i++) {
	    FactoryFinder.setFactory(FactoryFinderTestCase.FACTORIES[i][0],
				     FactoryFinderTestCase.FACTORIES[i][1]);
	}
	
	for (i = 0, len = FactoryFinderTestCase.FACTORIES.length; i < len; i++) {
	    clazz = Class.forName(FactoryFinderTestCase.FACTORIES[i][0]);
	    factory = FactoryFinder.getFactory(FactoryFinderTestCase.FACTORIES[i][0]);
	    assertTrue("Factory for " + clazz.getName() + 
		       " not of expected type.",
		       clazz.isAssignableFrom(factory.getClass()));
	    clazz = Class.forName(FactoryFinderTestCase.FACTORIES[i][1]);
	    assertTrue("Factory " + FactoryFinderTestCase.FACTORIES[i][1] + " not of expected type",
		       clazz.isAssignableFrom(factory.getClass()));

	}
    }
}
