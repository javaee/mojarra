/*
 * $Id: TestNavigationHandler.java,v 1.26 2006/03/29 23:04:40 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// TestNavigationHandler.java

package com.sun.faces.application;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;
import com.sun.org.apache.commons.digester.CallMethodRule;
import com.sun.org.apache.commons.digester.CallParamRule;
import com.sun.org.apache.commons.digester.Digester;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class test the <code>NavigationHandlerImpl</code> functionality.
 * It uses two xml files:
 * 1) faces-navigation.xml --> contains the navigation cases themselves.
 * 2) navigation-cases.xml --> contains the test cases including expected
 * view identifier outcomes for this test to validate against.
 * Both files exist under <code>web/test/WEB-INF</code>.
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestNavigationHandler.java,v 1.26 2006/03/29 23:04:40 rlubke Exp $
 */

public class TestNavigationHandler extends ServletFacesTestCase {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    private List testResultList = null;
    protected Digester digester = null;
    private ApplicationImpl application = null;
    private NavigationHandlerTestImpl navHandler = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestNavigationHandler() {
        super("TestNavigationHandler");
    }


    public TestNavigationHandler(String name) {
        super(name);
    }
//
// Class methods
//

//
// Methods from TestCase
//

    public void setUp() {
        super.setUp();
        loadConfigFile();
    }

//
// General Methods
//
    private void loadConfigFile() {
        loadFromInitParam("/WEB-INF/faces-navigation.xml");
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

        digester.addRule("*/test",
                         new CallMethodRule("createAndAccrueTestResult", 4));
        digester.addRule("*/test", new CallParamRule(0, "fromViewId"));
        digester.addRule("*/test", new CallParamRule(1, "fromAction"));
        digester.addRule("*/test", new CallParamRule(2, "fromOutcome"));
        digester.addRule("*/test", new CallParamRule(3, "toViewId"));

        String fileName = "/WEB-INF/navigation-cases.xml";
        InputStream input = null;
        try {
            input = config.getServletContext().getResourceAsStream(fileName);
        } catch (Throwable t) {
            System.out.println("Error Opening File:" + fileName);
            assertTrue(false);
        }
        try {
            digester.push(this);
            digester.parse(input);
        } catch (Throwable t) {
            if (null != t) {
                t.printStackTrace();
            }
            System.out.println("Unable to parse file:" + t.getMessage());
            assertTrue(false);
        }
    }


    public void createAndAccrueTestResult(String fromViewId, String fromAction,
                                          String fromOutcome, String toViewId) {
        if (testResultList == null) {
            testResultList = new ArrayList();
        }
        TestResult testResult = new TestResult();
        testResult.fromViewId = fromViewId;
        testResult.fromAction = fromAction;
        testResult.fromOutcome = fromOutcome;
        testResult.toViewId = toViewId;
        testResultList.add(testResult);
    }


    public void testNavigationHandler() {

        ApplicationFactory aFactory =
            (ApplicationFactory) FactoryFinder.getFactory(
                FactoryFinder.APPLICATION_FACTORY);
        Application application = (ApplicationImpl) aFactory.getApplication();
        loadTestResultList();
        NavigationHandlerImpl navHandler = (NavigationHandlerImpl) application.getNavigationHandler();
        FacesContext context = getFacesContext();

        String newViewId = null;
        UIViewRoot page = null;
        boolean gotException = false;

        for (int i = 0; i < testResultList.size(); i++) {
            TestResult testResult = (TestResult) testResultList.get(i);
            System.out.println("Testing from-view-id=" + testResult.fromViewId +
                               " from-action=" + testResult.fromAction +
                               " from-outcome=" + testResult.fromOutcome);
            page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
            page.setViewId(testResult.fromViewId);
            context.setViewRoot(page);
            try {
                navHandler.handleNavigation(context, testResult.fromAction,
                                            testResult.fromOutcome);
            } catch (Exception e) {
                // exception is valid only if context or fromoutcome is null.
                assertTrue(testResult.fromOutcome == null);
                gotException = true;
            }
            if (!gotException) {
                newViewId = context.getViewRoot().getViewId();
                if (testResult.fromOutcome == null) {
                    System.out.println(
                        "assertTrue(" + newViewId + ".equals(" +
                        testResult.fromViewId +
                        "))");
                    assertTrue(newViewId.equals(testResult.fromViewId));
                } else {
                    System.out.println(
                        "assertTrue(" + newViewId + ".equals(" +
                        testResult.toViewId +
                        "))");
                    assertTrue(newViewId.equals(testResult.toViewId));
                }
            }
        }
    }

    // This tests that the same <from-view-id> element value existing in a seperate
    // navigation rule, gets combined with the other rules with the same <from-view-id>.
    // Specifically, it will to make sure that after loading, there are the correct number of
    // cases with the common <from-view-id>;
 
    public void testSeperateRule() {
        int cnt = 0;
        ApplicationFactory aFactory =
            (ApplicationFactory) FactoryFinder.getFactory(
                FactoryFinder.APPLICATION_FACTORY);
        Application application = aFactory.getApplication();
        assertTrue(application instanceof ApplicationImpl);
	ApplicationAssociate associate = ApplicationAssociate.getInstance(getFacesContext().getExternalContext());
	assertNotNull(associate);
	
        Map caseListMap = associate.getNavigationCaseListMappings();
        Iterator iter = caseListMap.keySet().iterator();
        while (iter.hasNext()) {
            String fromViewId = (String) iter.next();
            if (fromViewId.equals("/login.jsp")) {
                List caseList = (List) caseListMap.get(fromViewId);
                for (int i = 0; i < caseList.size(); i++) {
                    ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(
                        i);
                    if (cnc.getFromViewId().equals("/login.jsp")) {
                        cnt++;
                    }
                }
            }
        }
        assertTrue(cnt == 6);
    }


    class TestResult extends Object {

        public String fromViewId = null;
        public String fromAction = null;
        public String fromOutcome = null;
        public String toViewId = null;
    }

} // end of class TestNavigationHandler

