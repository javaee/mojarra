/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package javax.faces;

import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockServletContext;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link UISelectBooleanBase}.</p>
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
        {FactoryFinder.APPLICATION_FACTORY,
            "com.sun.faces.mock.MockApplicationFactory"
        },
        {FactoryFinder.EXTERNAL_CONTEXT_FACTORY,
            "com.sun.faces.mock.MockExternalContextFactory"
        },
        {FactoryFinder.FACES_CONTEXT_FACTORY,
            "com.sun.faces.mock.MockFacesContextFactory"
        },
        {FactoryFinder.LIFECYCLE_FACTORY,
            "com.sun.faces.mock.MockLifecycleFactory"
        },
        {FactoryFinder.RENDER_KIT_FACTORY,
            "com.sun.faces.mock.MockRenderKitFactory"
        }
    };

    // ---------------------------------------------------- Overall Test Methods
    // Set up instance variables required by this test case.
    @Override
    public void setUp() throws Exception {
        super.setUp();

        Method method = FacesContext.class.getDeclaredMethod("setCurrentInstance", FacesContext.class);
        method.setAccessible(true);
        method.invoke(null, new Object[]{null});

        for (int i = 0, len = FACTORIES.length; i < len; i++) {
            System.getProperties().remove(FACTORIES[i][0]);
        }
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(FactoryFinderTestCase.class));
    }

    // Tear down instance variables required by ths test case
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        FactoryFinder.releaseFactories();
        for (int i = 0, len = FACTORIES.length; i < len; i++) {
            System.getProperties().remove(FACTORIES[i][0]);
        }
    }

    // ------------------------------------------------- Individual Test Methods
    /**
     * <p>
     * verify that the overrides specified in the faces-config.xml in the user's
     * webapp take precedence.</p>
     *
     * @throws java.lang.Exception
     */
    public void testFacesConfigCase() throws Exception {
//        Object factory = null;
//        Class clazz = null;
//
//        FactoryFinder.releaseFactories();
//        int len, i = 0;
//
//        // simulate the "faces implementation specific" part
//        for (i = 0, len = FACTORIES.length; i < len; i++) {
//            FactoryFinder.setFactory(FACTORIES[i][0],
//                    FACTORIES[i][1]);
//        }
//
//        // simulate the "WEB-INF/services" part
//        // this is done by the build.xml file
//        File servicesDir = new File(System.getProperty("basedir"), "target/classes/META-INF/services");
//        servicesDir.mkdirs();
//
//        File servicesFile = new File(servicesDir, "javax.faces.context.FacesContextFactory");
//        PrintWriter writer = new PrintWriter(servicesFile);
//        writer.println("javax.faces.mock.MockFacesContextFactoryExtender");
//        writer.flush();
//        writer.close();
//
//        // simulate the "webapp faces-config.xml" part
//        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
//                "javax.faces.mock.MockFacesContextFactoryExtender2");
//
//        for (i = 0, len = FACTORIES.length; i < len; i++) {
//            clazz = Class.forName(FACTORIES[i][0]);
//            factory = FactoryFinder.getFactory(FACTORIES[i][0]);
//            assertTrue("Factory for " + clazz.getName()
//                    + " not of expected type.",
//                    clazz.isAssignableFrom(factory.getClass()));
//            clazz = Class.forName(FACTORIES[i][1]);
//            assertTrue("Factory " + FACTORIES[i][1] + " not of expected type",
//                    clazz.isAssignableFrom(factory.getClass()));
//
//        }
//        // verify that the delegation works
//        assertTrue(System.getProperty(FACTORIES[2][0]).equals("javax.faces.mock.MockFacesContextFactoryExtender2"));
//        assertTrue(System.getProperty("oldImpl").equals("javax.faces.mock.MockFacesContextFactoryExtender"));
//
//        // Verify IllegalStateException when factory not found 
//        FactoryFinder.releaseFactories();
//        FactoryFinder.setFactory(FACTORIES[0][0], FACTORIES[0][1]);
//        FactoryFinder.setFactory(FACTORIES[1][0], FACTORIES[1][1]);
//        FactoryFinder.setFactory(FACTORIES[2][0], FACTORIES[2][1]);
//        FactoryFinder.setFactory(FACTORIES[4][0], FACTORIES[4][1]);
//        boolean exceptionThrown = false;
//        try {
//            factory = FactoryFinder.getFactory(FACTORIES[3][0]);
//        } catch (IllegalStateException ise) {
//            exceptionThrown = true;
//        }
//        assertTrue(exceptionThrown);
//
//        servicesFile.delete();
    }

    public void testNoFacesContext() throws Exception {
        assertNull(FacesContext.getCurrentInstance());
        Object result = FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        assertNotNull(result);
        assertTrue(result instanceof FacesContextFactory);
    }

    /**
     * <p>
     * In the absence of webapp faces-config.xml, verify that the overrides
     * specified in the META-INF/services take precedence.</p>
     *
     * @throws java.lang.Exception
     */
    public void testServicesCase() throws Exception {
//        Object factory = null;
//        Class clazz = null;
//
//        FactoryFinder.releaseFactories();
//        int len, i = 0;
//
//        // simulate the "faces implementation specific" part
//        for (i = 0, len = FACTORIES.length; i < len; i++) {
//            FactoryFinder.setFactory(FACTORIES[i][0],
//                    FACTORIES[i][1]);
//        }
//
//        // simulate the "WEB-INF/services" part
//        // this is done by the build.xml file
//        File servicesDir = new File(System.getProperty("basedir"), "target/classes/META-INF/services");
//        servicesDir.mkdirs();
//
//        File servicesFile = new File(servicesDir, "javax.faces.context.FacesContextFactory");
//        PrintWriter writer = new PrintWriter(servicesFile);
//        writer.println("javax.faces.mock.MockFacesContextFactoryExtender");
//        writer.flush();
//        writer.close();
//
//        // this testcase omits the "webapp faces-config.xml" simulation
//        for (i = 0, len = FACTORIES.length; i < len; i++) {
//            clazz = Class.forName(FACTORIES[i][0]);
//            factory = FactoryFinder.getFactory(FACTORIES[i][0]);
//            assertTrue("Factory for " + clazz.getName()
//                    + " not of expected type.",
//                    clazz.isAssignableFrom(factory.getClass()));
//            clazz = Class.forName(FACTORIES[i][1]);
//            assertTrue("Factory " + FACTORIES[i][1] + " not of expected type",
//                    clazz.isAssignableFrom(factory.getClass()));
//
//        }
//        // verify that the delegation works
//        assertTrue(System.getProperty(FACTORIES[2][0]).equals("javax.faces.mock.MockFacesContextFactoryExtender"));
//        assertTrue(System.getProperty("oldImpl").equals("com.sun.faces.mock.MockFacesContextFactory"));
//
//        servicesFile.delete();
    }

    public void testNoFacesContextInitially() throws Exception {
        assertNull(FacesContext.getCurrentInstance());

        FactoryFinder.releaseFactories();
        FactoryFinder.setFactory(FACTORIES[0][0], FACTORIES[0][1]);
        FactoryFinder.setFactory(FACTORIES[1][0], FACTORIES[1][1]);
        FactoryFinder.setFactory(FACTORIES[2][0], FACTORIES[2][1]);
        FactoryFinder.setFactory(FACTORIES[3][0], FACTORIES[3][1]);
        FactoryFinder.setFactory(FACTORIES[4][0], FACTORIES[4][1]);

        FacesContextFactory fcFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        LifecycleFactory lFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Object request = new MockHttpServletRequest();
        Object response = new MockHttpServletResponse();
        Object containerContext = new MockServletContext();
        Lifecycle l = lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        FacesContext context = fcFactory.getFacesContext(containerContext, request, response, l);

        ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application app = aFactory.getApplication();
        FactoryFinder.releaseFactories();
    }

    // ------------------------------------------- helpers
    public static void printRelevantSystemProperties() {
        System.out.println("++++++Relevant System Properties: ");
        for (int i = 0, len = FACTORIES.length; i < len; i++) {
            System.out.println(FACTORIES[i][0] + ": "
                    + System.getProperty(FACTORIES[i][0]));
        }
    }
}
