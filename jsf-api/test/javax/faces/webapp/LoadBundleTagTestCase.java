/*
 * $Id: LoadBundleTagTestCase.java,v 1.1 2003/11/04 04:08:01 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;

import junit.framework.Test;
import junit.framework.TestSuite;
import java.util.Map;

public class LoadBundleTagTestCase extends TagTestCaseBase {


    // ----------------------------------------------------- Instance Variables

    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public LoadBundleTagTestCase(String name) {

        super(name);

    }


    // -------------------------------------------------- Overall Test Methods

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(LoadBundleTagTestCase.class));

    }

    // ------------------------------------------------ Individual Test Methods


    // Test multiple tag rendering with ids
    public void testLoadBundle() throws Exception {
	LoadBundleTag tag = new LoadBundleTag();
	tag.setBasename("javax.faces.Messages");
	tag.setVar("messages");
	tag.doStartTag();
	assertEquals("Didn't get expected value", 
		     ((Map)facesContext.getExternalContext().getRequestMap().get("messages")).get("buckaroo"),
		     "banzai");
	assertEquals("Didn't get expected value", 
		     ((Map)facesContext.getExternalContext().getRequestMap().get("messages")).get("john"),
		     "bigboote");

    }
}

