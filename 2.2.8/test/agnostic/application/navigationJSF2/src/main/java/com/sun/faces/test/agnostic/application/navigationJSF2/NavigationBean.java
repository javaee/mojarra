/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.agnostic.application.navigationJSF2;

import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.application.NavigationHandlerImpl;
import com.sun.faces.config.DbfFactory;
import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.application.Application;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.servlet.ServletContext;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.Assert.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;


@ManagedBean
@SessionScoped

public class NavigationBean {

    private List testResultList = null;

    public NavigationBean() {
    }

    public String getNavigationHandler() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ViewMapDestroyedListener listener = new ViewMapDestroyedListener();
        app.subscribeToEvent(PreDestroyViewMapEvent.class,
                                     UIViewRoot.class,
                                     listener);
        try {
            loadTestResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        NavigationHandlerImpl navHandler = (NavigationHandlerImpl) app.getNavigationHandler();
        String newViewId;
        UIViewRoot page;
        boolean gotException = false;

        for (int i = 0; i < testResultList.size(); i++) {
            TestResult testResult = (TestResult) testResultList.get(i);
            Boolean conditionResult = null;
            if (testResult.condition != null) {
                conditionResult = (Boolean) app.getExpressionFactory().
                    createValueExpression(fc.getELContext(), testResult.condition, Boolean.class).
                        getValue(fc.getELContext());
            }
            System.out.println("Testing from-view-id=" + testResult.fromViewId +
                               " from-action=" + testResult.fromAction +
                               " from-outcome=" + testResult.fromOutcome +
                               " if=" + testResult.condition);
            page = Util.getViewHandler(fc).createView(fc, null);
            page.setViewId(testResult.fromViewId);
            page.setLocale(Locale.US);
            page.getViewMap(); // cause the map to be created
            fc.setViewRoot(page);
            listener.reset();
            try {
                navHandler.handleNavigation(fc, testResult.fromAction,
                                            testResult.fromOutcome);
            } catch (Exception e) {
                // exception is valid only if context or fromoutcome is null.
                assertTrue(testResult.fromOutcome == null);
                gotException = true;
            }
            if (!gotException) {
                if (!testResult.fromViewId.equals(testResult.toViewId)
                    && (testResult.fromOutcome != null || testResult.condition != null)
                    && (testResult.condition == null || conditionResult != null)) {
                    assertTrue(listener.getPassedEvent() instanceof PreDestroyViewMapEvent);
                } else {
                    assertTrue(!listener.wasProcessEventInvoked());
                    assertTrue(listener.getPassedEvent() == null);
                }
                listener.reset();
                newViewId = fc.getViewRoot().getViewId();
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
        app.unsubscribeFromEvent(PreDestroyViewMapEvent.class,
                                         UIViewRoot.class,
                                         listener);
        return "SUCCESS";
    }

    private void loadTestResultList() throws Exception {
        DocumentBuilderFactory f = DbfFactory.getFactory();
        f.setNamespaceAware(false);
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();

        Document d = builder.parse(((ServletContext)FacesContext.getCurrentInstance().getExternalContext().
            getContext()).getResourceAsStream("/WEB-INF/navigation-cases-2.xml"));
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

    private void createAndAccrueTestResult(String fromViewId, String fromAction,
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

    private String title = "Test JSF2 Navigation Handler";
    public String getTitle() {
        return title;
    }

    private String status="";

    public String getStatus() {
        return status;
    }

}

