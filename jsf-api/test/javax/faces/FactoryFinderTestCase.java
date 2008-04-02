/*
 * $Id: FactoryFinderTestCase.java,v 1.2 2004/02/04 23:38:39 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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

public class FactoryFinderTestCase extends TestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public FactoryFinderTestCase(String name) {
        super(name);
    }

    public static String FACTORIES[][] = {
	{ FactoryFinder.APPLICATION_FACTORY, 
	  "javax.faces.mock.MockApplicationFactory"
	},
	{ FactoryFinder.FACES_CONTEXT_FACTORY, 
	  "javax.faces.mock.MockFacesContextFactory"
	},
	{ FactoryFinder.LIFECYCLE_FACTORY, 
	  "javax.faces.mock.MockLifecycleFactory"
	},
	{ FactoryFinder.RENDER_KIT_FACTORY, 
	  "javax.faces.mock.MockRenderKitFactory"
	}
    };


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() throws Exception {
        super.setUp();
	for (int i = 0, len = FACTORIES.length; i < len; i++) {
	    System.getProperties().remove(FACTORIES[i][0]);
	}
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(FactoryFinderTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() throws Exception {
        super.tearDown();
	FactoryFinder.releaseFactories();
	for (int i = 0, len = FACTORIES.length; i < len; i++) {
	    System.getProperties().remove(FACTORIES[i][0]);
	}
    }


    // ------------------------------------------------- Individual Test Methods

    /**
     * <p>verify that the overrides specified in the faces-config.xml in
     * the user's webapp take precedence.</p>
     */

    public void testFacesConfigCase() throws Exception {
	Object factory = null;
	Class clazz = null;

	FactoryFinder.releaseFactories();
	int len, i = 0;

	// simulate the "faces implementation specific" part
	for (i = 0, len = FACTORIES.length; i < len; i++) {
	    FactoryFinder.setFactory(FACTORIES[i][0],
				     FACTORIES[i][1]);
	}

	// simulate the "WEB-INF/services" part
	// this is done by the build.xml file
	
	// simulate the "webapp faces-config.xml" part
	FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
				 "javax.faces.mock.MockFacesContextFactoryExtender2");

	for (i = 0, len = FACTORIES.length; i < len; i++) {
	    clazz = Class.forName(FACTORIES[i][0]);
	    factory = FactoryFinder.getFactory(FACTORIES[i][0]);
	    assertTrue("Factory for " + clazz.getName() + 
		       " not of expected type.",
		       clazz.isAssignableFrom(factory.getClass()));
	    clazz = Class.forName(FACTORIES[i][1]);
	    assertTrue("Factory " + FACTORIES[i][1] + " not of expected type",
		       clazz.isAssignableFrom(factory.getClass()));

	}
	// verify that the delegation works
	assertTrue(System.getProperty(FACTORIES[1][0]).equals("javax.faces.mock.MockFacesContextFactoryExtender2"));
	assertTrue(System.getProperty("oldImpl").equals("javax.faces.mock.MockFacesContextFactoryExtender"));

    }    

    /**
     * <p>In the absence of webapp faces-config.xml, verify that the
     * overrides specified in the META-INF/services take precedence.</p>
     */

    public void testServicesCase() throws Exception {
	Object factory = null;
	Class clazz = null;

	FactoryFinder.releaseFactories();
	int len, i = 0;

	// simulate the "faces implementation specific" part
	for (i = 0, len = FACTORIES.length; i < len; i++) {
	    FactoryFinder.setFactory(FACTORIES[i][0],
				     FACTORIES[i][1]);
	}

	// simulate the "WEB-INF/services" part
	// this is done by the build.xml file

	// this testcase omits the "webapp faces-config.xml" simulation
	
	for (i = 0, len = FACTORIES.length; i < len; i++) {
	    clazz = Class.forName(FACTORIES[i][0]);
	    factory = FactoryFinder.getFactory(FACTORIES[i][0]);
	    assertTrue("Factory for " + clazz.getName() + 
		       " not of expected type.",
		       clazz.isAssignableFrom(factory.getClass()));
	    clazz = Class.forName(FACTORIES[i][1]);
	    assertTrue("Factory " + FACTORIES[i][1] + " not of expected type",
		       clazz.isAssignableFrom(factory.getClass()));

	}
	// verify that the delegation works
	assertTrue(System.getProperty(FACTORIES[1][0]).equals("javax.faces.mock.MockFacesContextFactoryExtender"));
	assertTrue(System.getProperty("oldImpl").equals("javax.faces.mock.MockFacesContextFactory"));


    }

    // ------------------------------------------- helpers
    public static void printRelevantSystemProperties() {
	System.out.println("++++++Relevant System Properties: ");
	for (int i = 0, len = FACTORIES.length; i < len; i++) {
	    System.out.println(FACTORIES[i][0] + ": " + 
			       System.getProperty(FACTORIES[i][0]));
	}
    }
}
