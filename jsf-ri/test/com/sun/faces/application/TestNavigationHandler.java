/*
 * $Id: TestNavigationHandler.java,v 1.29 2008/01/31 18:36:00 rlubke Exp $
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

// TestNavigationHandler.java

package com.sun.faces.application;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;
import com.sun.org.apache.commons.digester.CallMethodRule;
import com.sun.org.apache.commons.digester.CallParamRule;
import com.sun.org.apache.commons.digester.Digester;

import javax.faces.FactoryFinder;
import javax.faces.event.SystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ViewMapDestroyedEvent;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import java.io.InputStream;
import java.util.*;

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
 * @version $Id: TestNavigationHandler.java,v 1.29 2008/01/31 18:36:00 rlubke Exp $
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

        Application application = getFacesContext().getApplication();
        ViewMapDestroyedListener listener = new ViewMapDestroyedListener();
        application.subscribeToEvent(ViewMapDestroyedEvent.class,
                                     UIViewRoot.class,
                                     listener);
        loadTestResultList();
        NavigationHandlerImpl navHandler = (NavigationHandlerImpl) application.getNavigationHandler();
        FacesContext context = getFacesContext();

        String newViewId;
        UIViewRoot page;
        boolean gotException = false;

        for (int i = 0; i < testResultList.size(); i++) {
            TestResult testResult = (TestResult) testResultList.get(i);
            System.out.println("Testing from-view-id=" + testResult.fromViewId +
                               " from-action=" + testResult.fromAction +
                               " from-outcome=" + testResult.fromOutcome);
            page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
            page.setViewId(testResult.fromViewId);
            page.setLocale(Locale.US);
            page.getViewMap(); // cause the map to be created
            context.setViewRoot(page);
            listener.reset();
            try {
                navHandler.handleNavigation(context, testResult.fromAction,
                                            testResult.fromOutcome);
            } catch (Exception e) {
                // exception is valid only if context or fromoutcome is null.
                assertTrue(testResult.fromOutcome == null);
                gotException = true;
            }
            if (!gotException) {
                if (!testResult.fromViewId.equals(testResult.toViewId)
                    && testResult.fromOutcome != null) {
                    assertTrue(listener.getPassedEvent() instanceof ViewMapDestroyedEvent);
                } else {
                    assertTrue(!listener.wasProcessEventInvoked());
                    assertTrue(listener.getPassedEvent() == null);
                }
                listener.reset();
                newViewId = context.getViewRoot().getViewId();
                if (testResult.fromOutcome == null) {
                    listener.reset();
                    System.out.println(
                        "assertTrue(" + newViewId + ".equals(" +
                        testResult.fromViewId +
                        "))");
                    assertTrue(newViewId.equals(testResult.fromViewId));
                } else {
                    listener.reset();
                    System.out.println(
                        "assertTrue(" + newViewId + ".equals(" +
                        testResult.toViewId +
                        "))");
                    assertTrue(newViewId.equals(testResult.toViewId));
                }
            }
        }
        application.unsubscribeFromEvent(ViewMapDestroyedEvent.class,
                                         UIViewRoot.class,
                                         listener);
    }

     public void testSimilarFromViewId() {
        ApplicationFactory aFactory =
            (ApplicationFactory) FactoryFinder.getFactory(
                FactoryFinder.APPLICATION_FACTORY);
        Application application = aFactory.getApplication();
        NavigationHandler navHandler = application.getNavigationHandler();

        UIViewRoot root = application.getViewHandler().createView(getFacesContext(), "/dir1/dir2/dir3/test.jsp");
        root.setLocale(Locale.US);
        getFacesContext().setViewRoot(root);

        try {
            navHandler.handleNavigation(getFacesContext(), null, "home");
        } catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
        String newViewId = getFacesContext().getViewRoot().getViewId();
        assertTrue("newViewId is: " + newViewId, "/dir1/dir2/dir3/home.jsp".equals(newViewId));
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

    private static final class ViewMapDestroyedListener
          implements SystemEventListener {

        private SystemEvent event;
        private boolean processEventInvoked;

        public void processEvent(SystemEvent event)
        throws AbortProcessingException {
            this.processEventInvoked = true;
            this.event = event;
        }

        public boolean isListenerForSource(Object source) {
            return (source instanceof UIViewRoot);
        }

        public SystemEvent getPassedEvent() {
            return event;
        }

        public boolean wasProcessEventInvoked() {
            return processEventInvoked;
        }

        public void reset() {
            processEventInvoked = false;
            event = null;
        }
    }

} // end of class TestNavigationHandler

