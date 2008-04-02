/*
 * $Id: TestNavigationHandler.java,v 1.7 2003/08/22 16:50:33 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestNavigationHandler.java

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.config.ConfigListener;
import com.sun.faces.config.ConfigNavigationCase;

import com.sun.faces.util.DebugUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.FactoryFinder;
import javax.faces.component.UIPage;
import javax.faces.component.base.UIPageBase;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

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
 *        view identifier outcomes for this test to validate against. 
 * Both files exist under <code>web/test/WEB-INF</code>.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestNavigationHandler.java,v 1.7 2003/08/22 16:50:33 eburns Exp $
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
    private ApplicationImpl application = null;
    private NavigationHandlerTestImpl navHandler = null;

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
    private void loadConfigFile() {
	loadFromInitParam("WEB-INF/faces-navigation.xml");
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
        digester.addRule("*/test", new CallParamRule(0, "fromViewId"));
        digester.addRule("*/test", new CallParamRule(1, "fromActionRef"));
        digester.addRule("*/test", new CallParamRule(2, "fromOutcome"));
        digester.addRule("*/test", new CallParamRule(3, "toViewId"));

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

    public void createAndAccrueTestResult(String fromViewId, String fromActionRef,
        String fromOutcome, String toViewId) {
        if (testResultList == null) {
            testResultList = new ArrayList();
        }
        TestResult testResult = new TestResult();
        testResult.fromViewId = fromViewId;
        testResult.fromActionRef = fromActionRef;
        testResult.fromOutcome = fromOutcome;
        testResult.toViewId = toViewId;
        testResultList.add(testResult);
    }

    public void testNavigationHandler() {
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = (ApplicationImpl) aFactory.getApplication();
        loadTestResultList();
        NavigationHandlerImpl navHandler = (NavigationHandlerImpl)application.getNavigationHandler();
	((com.sun.faces.TestNavigationHandler)navHandler).getCaseListMap().size();
        FacesContext context = getFacesContext();

        String newViewId = null;
        UIPage page = null;
        for (int i=0; i<testResultList.size(); i++) {
            TestResult testResult = (TestResult)testResultList.get(i);
            System.out.println("Testing from-view-id="+testResult.fromViewId+
                " from-action-ref="+testResult.fromActionRef+
                " from-outcome="+testResult.fromOutcome);
            page = new UIPageBase();
            page.setViewId(testResult.fromViewId);
            context.setViewRoot(page);
            navHandler.handleNavigation(
	        context, testResult.fromActionRef, testResult.fromOutcome);
            newViewId = context.getViewRoot().getViewId();
            System.out.println("assertTrue("+newViewId+".equals("+testResult.toViewId+"))");
            assertTrue(newViewId.equals(testResult.toViewId));
        }
    }

    // This tests that the same <from-view-id> element value existing in a seperate
    // navigation rule, gets combined with the other rules with the same <from-view-id>.
    // Specifically, it will to make sure that after loading, there are the correct number of
    // cases with the common <from-view-id>;
 
    public void testSeperateRule() {
        int cnt = 0;
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = (ApplicationImpl) aFactory.getApplication();
        NavigationHandlerImpl navHandler = (NavigationHandlerImpl)application.
            getNavigationHandler();
	Map caseListMap = ((com.sun.faces.TestNavigationHandler)navHandler).getCaseListMap();
	Iterator iter = caseListMap.keySet().iterator();
	while (iter.hasNext()) {
	    String fromViewId = (String)iter.next();
	    if (fromViewId.equals("/login.jsp")) {
                List caseList = (List)caseListMap.get(fromViewId);
		for (int i=0; i<caseList.size();i++) {
		    ConfigNavigationCase cnc = (ConfigNavigationCase)caseList.get(i);
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
        public String fromActionRef = null;
        public String fromOutcome = null;
        public String toViewId = null;
    }

} // end of class TestNavigationHandler

