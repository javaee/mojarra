/*
 * $Id: TestNavigationHandler.java,v 1.2 2003/05/10 01:05:41 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestNavigationHandler.java

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.config.ConfigBase;
import com.sun.faces.config.ConfigListener;
import com.sun.faces.config.ConfigNavigationCase;
import com.sun.faces.tree.SimpleTreeImpl;

import com.sun.faces.util.DebugUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.apache.cactus.server.ServletContextWrapper;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.CallMethodRule;
import org.apache.commons.digester.CallParamRule;
import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;


import com.sun.faces.ServletFacesTestCase;

/**
 *
 * This class test the <code>NavigationHandlerImpl</code> functionality.
 * It uses two xml files:
 *     1) faces-navigation.xml --> contains the navigation cases themselves.
 *     2) navigation-cases.xml --> contains the test cases including expected
 *        tree identifier outcomes for this test to validate against. 
 * Both files exist under <code>web/test/WEB-INF</code>.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestNavigationHandler.java,v 1.2 2003/05/10 01:05:41 rkitain Exp $
 * 
 */

public class TestNavigationHandler extends ServletFacesTestCase
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    private List testResultList= null;
    protected Digester digester = null;


// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestNavigationHandler() {super("TestNavigationHandler");}
    public TestNavigationHandler(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//
    protected void configureRules(Digester digester) {

    }

    private void loadConfigFile() {
        config.getServletContext().removeAttribute(RIConstants.CONFIG_ATTR);
	// clear out the renderKit factory
	FactoryFinder.releaseFactories();

        final String paramVal = "WEB-INF/faces-navigation.xml";

        // work around a bug in cactus where calling
        // config.setInitParameter() doesn't cause
        // servletContext.getInitParameter() to relfect the call.

        ServletContextWrapper sc =
            new ServletContextWrapper(config.getServletContext()) {
                public String getInitParameter(String theName) {
                    if (null != theName &&
                        theName.equals(RIConstants.CONFIG_FILES_INITPARAM)) {
                        return paramVal;
                    }
                    return super.getInitParameter(theName);
                }
            };

        ConfigListener configListener = new ConfigListener();
        ServletContextEvent e =
            new ServletContextEvent(sc);
        configListener.contextInitialized(e);

        System.out.println("NAV CASES LOADED:");
        ConfigBase cbase = (ConfigBase)config.getServletContext().getAttribute(RIConstants.CONFIG_ATTR);
        List navs = cbase.getNavigationCases();
        for (int i=0; i<navs.size(); i++) {
            ConfigNavigationCase nc = (ConfigNavigationCase)navs.get(i);
            System.out.println("<from-tree-id>:"+nc.getFromTreeId());
            System.out.println(" : <from-action-ref>:"+nc.getFromActionRef());
            System.out.println(" : <from-outcome>:"+nc.getFromOutcome());
            System.out.println(" : <to-tree-id>:"+nc.getToTreeId());
            System.out.println("----------------------------------------------");
        }
    }

    private void loadTestResultList() {
        Digester digester = new Digester();
        digester.setUseContextClassLoader(true);
        try {
            digester.setValidating(false);
        } catch (Throwable t) {
            System.out.println("Error creating Digester instance...");
            assertTrue(false);
        }

        digester.addRule("*/test", new CallMethodRule("createAndAccrueTestResult", 4));
        digester.addRule("*/test", new CallParamRule(0, "fromTreeId"));
        digester.addRule("*/test", new CallParamRule(1, "fromActionRef"));
        digester.addRule("*/test", new CallParamRule(2, "fromOutcome"));
        digester.addRule("*/test", new CallParamRule(3, "toTreeId"));

        String fileName = "/WEB-INF/navigation-cases.xml";
        InputStream input = null;
        try {
            input = config.getServletContext().getResourceAsStream(fileName);
        } catch (Throwable t) {
            System.out.println("Error Opening File:"+fileName);
            assertTrue(false);
        }
        try {
            digester.push(this);
            digester.parse(input);
        } catch (Throwable t) {
            if (null != t) {
                t.printStackTrace();
            }
            System.out.println("Unable to parse file:"+t.getMessage());
            assertTrue(false);
        }
    }

    public void createAndAccrueTestResult(String fromTreeId, String fromActionRef,
        String fromOutcome, String toTreeId) {
        if (testResultList == null) {
            testResultList = new ArrayList();
        }
        TestResult testResult = new TestResult();
        testResult.fromTreeId = fromTreeId;
        testResult.fromActionRef = fromActionRef;
        testResult.fromOutcome = fromOutcome;
        testResult.toTreeId = toTreeId;
        testResultList.add(testResult);
    }

    public void testNavigationHandler() {
        loadTestResultList();
        loadConfigFile();
        FacesContext context = getFacesContext();
        NavigationHandlerImpl navHandler = new NavigationHandlerImpl();
        ConfigBase cbase = (ConfigBase)config.getServletContext().getAttribute(RIConstants.CONFIG_ATTR);
        navHandler.initialize(cbase);

        TestResult testResult = null;
        String newTreeId = null;
        for (int i=0; i<testResultList.size(); i++) {
            testResult = (TestResult)testResultList.get(i);
            System.out.println("Testing from-tree-id="+testResult.fromTreeId+
                " from-action-ref="+testResult.fromActionRef+
                " from-outcome="+testResult.fromOutcome);
            context.setTree(new SimpleTreeImpl(context, testResult.fromTreeId));
            navHandler.handleNavigation(context, testResult.fromActionRef, testResult.fromOutcome);
            newTreeId = context.getTree().getTreeId();
            System.out.println("assertTrue("+newTreeId+".equals("+testResult.toTreeId+"))");
            assertTrue(newTreeId.equals(testResult.toTreeId));
        }
    }

    // This tests that the same <from-tree-id> element value existing in a seperate
    // navigation rule, gets combined with the other rules with the same <from-tree-id>.
    // Specifically, it will to make sure that after loading, there are the correct number of
    // cases with the common <from-tree-id>;
 
    public void testSeperateRule() {
        int cnt = 0;
        ConfigBase cbase = (ConfigBase)config.getServletContext().getAttribute(RIConstants.CONFIG_ATTR);
        List navs = cbase.getNavigationCases();
        for (int i=0; i<navs.size(); i++) {
            ConfigNavigationCase nc = (ConfigNavigationCase)navs.get(i);
            if (nc.getFromTreeId().equals("/login.jsp")) {
                cnt++;
            }
        }
        assertTrue(cnt == 4);
    }

    class TestResult extends Object {
        public String fromTreeId = null;
        public String fromActionRef = null;
        public String fromOutcome = null;
        public String toTreeId = null;
    }

} // end of class TestNavigationHandler

