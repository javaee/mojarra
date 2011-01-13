/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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

import javax.faces.application.NavigationCase;
import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;
import com.sun.faces.config.DbfFactory;

import javax.faces.FactoryFinder;
import javax.faces.event.SystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

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
 */

public class TestJSF2NavigationHandler extends ServletFacesTestCase {

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

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestJSF2NavigationHandler() {
        super("TestJSF2NavigationHandler");
    }


    public TestJSF2NavigationHandler(String name) {
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
        loadFromInitParam("/WEB-INF/faces-navigation-2.xml");
    }


    private void loadTestResultList() throws Exception {

        DocumentBuilderFactory f = DbfFactory.getFactory();
        f.setNamespaceAware(false);
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();

        Document d = builder.parse(config.getServletContext().getResourceAsStream("/WEB-INF/navigation-cases-2.xml"));
        NodeList navigationRules = d.getDocumentElement()
              .getElementsByTagName("test");
        for (int i = 0; i < navigationRules.getLength(); i++) {
            Node test = navigationRules.item(i);
            NamedNodeMap attributes = test.getAttributes();
            Node fromViewId = attributes.getNamedItem("fromViewId");
            Node fromAction = attributes.getNamedItem("fromAction");
            Node fromOutput = attributes.getNamedItem("fromOutcome");
            Node condition = attributes.getNamedItem("if");
            Node toViewId = attributes.getNamedItem("toViewId");
            createAndAccrueTestResult(((fromViewId != null) ? fromViewId.getTextContent().trim() : null),
                                      ((fromAction != null) ? fromAction.getTextContent().trim() : null),
                                      ((fromOutput != null) ? fromOutput.getTextContent().trim() : null),
                                      ((condition != null) ? condition.getTextContent().trim() : null),
                                      ((toViewId != null) ? toViewId.getTextContent().trim() : null));
        }

    }




    public void createAndAccrueTestResult(String fromViewId, String fromAction,
                                          String fromOutcome, String condition, String toViewId) {
        if (testResultList == null) {
            testResultList = new ArrayList();
        }
        TestResult testResult = new TestResult();
        testResult.fromViewId = fromViewId;
        testResult.fromAction = fromAction;
        testResult.fromOutcome = fromOutcome;
        testResult.condition = condition;
        testResult.toViewId = toViewId;
        testResultList.add(testResult);
    }


    public void testNavigationHandler() {

        Application application = getFacesContext().getApplication();
        ViewMapDestroyedListener listener = new ViewMapDestroyedListener();
        application.subscribeToEvent(PreDestroyViewMapEvent.class,
                                     UIViewRoot.class,
                                     listener);
        try {
            loadTestResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        NavigationHandlerImpl navHandler = (NavigationHandlerImpl) application.getNavigationHandler();
        FacesContext context = getFacesContext();

        String newViewId;
        UIViewRoot page;
        boolean gotException = false;

        for (int i = 0; i < testResultList.size(); i++) {
            TestResult testResult = (TestResult) testResultList.get(i);
            Boolean conditionResult = null;
            if (testResult.condition != null) {
                conditionResult = (Boolean) application.getExpressionFactory()
                    .createValueExpression(context.getELContext(), testResult.condition, Boolean.class).getValue(context.getELContext());
            }
            System.out.println("Testing from-view-id=" + testResult.fromViewId +
                               " from-action=" + testResult.fromAction +
                               " from-outcome=" + testResult.fromOutcome +
                               " if=" + testResult.condition);
            page = Util.getViewHandler(context).createView(context, null);
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
                // test assumption: if the from and to change, it's because the outcome was not-null or a condition was evaluated
                if (!testResult.fromViewId.equals(testResult.toViewId)
                    && (testResult.fromOutcome != null || testResult.condition != null)
                    && (testResult.condition == null || conditionResult != null)) {
                    assertTrue(listener.getPassedEvent() instanceof PreDestroyViewMapEvent);
                } else {
                    assertTrue(!listener.wasProcessEventInvoked());
                    assertTrue(listener.getPassedEvent() == null);
                }
                listener.reset();
                newViewId = context.getViewRoot().getViewId();
                if (testResult.fromOutcome == null && testResult.condition == null) {
                    listener.reset();
                    System.out.println(
                        "assertTrue(" + newViewId + ".equals(" +
                        testResult.fromViewId +
                        "))");
                    assertTrue(newViewId.equals(testResult.fromViewId));
                }
                // test assumption: if condition is false, we advance to some other view
                else if (testResult.condition != null && conditionResult == false) {
                    listener.reset();
                    System.out.println(
                        "assertTrue(!" + newViewId + ".equals(" +
                        testResult.toViewId +
                        "))");
                    assertTrue(!newViewId.equals(testResult.toViewId));
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
        application.unsubscribeFromEvent(PreDestroyViewMapEvent.class,
                                         UIViewRoot.class,
                                         listener);
    }

    class TestResult extends Object {

        public String fromViewId = null;
        public String fromAction = null;
        public String fromOutcome = null;
        public String condition = null;
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

