/*
 * $Id: FactoryFinderTestCase2.java,v 1.5 2007/04/27 22:00:12 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
