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

package com.sun.faces.test.agnostic.application.navigation;

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
            System.out.println("Testing from-view-id=" + testResult.fromViewId +
                               " from-action=" + testResult.fromAction +
                               " from-outcome=" + testResult.fromOutcome);
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
                    && testResult.fromOutcome != null) {
                    assertTrue(listener.getPassedEvent() instanceof PreDestroyViewMapEvent);
                } else {
                    assertTrue(!listener.wasProcessEventInvoked());
                    assertTrue(listener.getPassedEvent() == null);
                }
                listener.reset();
                newViewId = fc.getViewRoot().getViewId();
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
        app.unsubscribeFromEvent(PreDestroyViewMapEvent.class,
                                         UIViewRoot.class,
                                         listener);
        return "SUCCESS";
    }

    public String getSimilarViewIds() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        NavigationHandler navHandler = app.getNavigationHandler();
        UIViewRoot root = app.getViewHandler().createView(fc, "/dir1/dir2/dir3/test.jsp");
        root.setLocale(Locale.US);
        fc.setViewRoot(root);
        try {
            navHandler.handleNavigation(fc, null, "home");
        } catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
        String newViewId = fc.getViewRoot().getViewId();
        assertTrue("newViewId is: " + newViewId, "/dir1/dir2/dir3/home.jsp".equals(newViewId));
        return "SUCCESS";
    }

    public String getSeparateRule() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        int cnt = 0;
        assertTrue(app instanceof ApplicationImpl);
        ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) app.getNavigationHandler();
        Map caseListMap = handler.getNavigationCases();
        Iterator iter = caseListMap.keySet().iterator();
        while (iter.hasNext()) {
            String fromViewId = (String) iter.next();
            if (fromViewId.equals("/login.jsp")) {
                Set<NavigationCase> caseSet = (Set<NavigationCase>) caseListMap.get(fromViewId);
                for (NavigationCase navCase : caseSet) {
                    if (navCase.getFromViewId().equals("/login.jsp")) {
                        cnt++;
                    }
                }
            }
        }
        assertTrue(cnt == 6);
        return "SUCCESS";
    }

    public String getWrappedNavigationHandler() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ConfigurableNavigationHandler impl = new NavigationHandlerImpl();
        NavigationHandler parent = new WrapperNavigationHandler(impl);
        parent.handleNavigation(fc, "", "");

        int cnt = 0;
        Map caseListMap = impl.getNavigationCases();
        Iterator iter = caseListMap.keySet().iterator();
        while (iter.hasNext()) {
            String fromViewId = (String) iter.next();
            if (fromViewId.equals("/login.jsp")) {
                Set<NavigationCase> caseSet = (Set<NavigationCase>) caseListMap.get(fromViewId);
                for (NavigationCase navCase : caseSet) {
                    if (navCase.getFromViewId().equals("/login.jsp")) {
                        cnt++;
                    }
                }
            }
        }
        assertTrue(cnt == 6);
        return "SUCCESS";
    }

    public String getRedirectParameters() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        UIViewRoot root = Util.getViewHandler(fc).createView(fc, null);
        root.setViewId("/page1.xhtml");
        fc.setViewRoot(root);
        ConfigurableNavigationHandler cnh = (ConfigurableNavigationHandler)app.getNavigationHandler();
        NavigationCase c1 = cnh.getNavigationCase(fc, null, "redirectOutcome1");
        Map<String,List<String>> parameters = c1.getParameters();
        assertNotNull(parameters);
        assertEquals(2, parameters.size());
        List<String> fooParams = parameters.get("foo");
        assertNotNull(fooParams);
        assertEquals(2, fooParams.size());
        assertEquals("bar", fooParams.get(0));
        assertEquals("bar2", fooParams.get(1));
        List<String> foo2Params = parameters.get("foo2");
        assertEquals(1, foo2Params.size());
        assertEquals("bar3", foo2Params.get(0));
        assertTrue(c1.isIncludeViewParams());

        NavigationCase c2 = cnh.getNavigationCase(fc, null, "redirectOutcome2");
        parameters = c2.getParameters();
        assertNull(parameters);
        assertFalse(c2.isIncludeViewParams());

        // ensure implicit navigation outcomes that include query strings
        // are properly parsed.

        NavigationCase c3 = cnh.getNavigationCase(fc, null, 
            "test?foo=rab&amp;foo=rab2&foo2=rab3&amp;faces-redirect=true&includeViewParams=true&");
        assertNotNull(c3);
        parameters = c3.getParameters();
        assertNotNull(parameters);
        assertTrue(c3.isRedirect());
        assertTrue(c3.isIncludeViewParams());
        assertEquals(2, parameters.size());
        fooParams = parameters.get("foo");
        assertNotNull(fooParams);
        assertEquals(2, fooParams.size());
        assertEquals("rab", fooParams.get(0));
        assertEquals("rab2", fooParams.get(1));
        foo2Params = parameters.get("foo2");
        assertEquals(1, foo2Params.size());
        assertEquals("rab3", foo2Params.get(0));


        // ensure implicit navigation outcomes that include query strings
        // separated with &amp; are properly parsed.

        NavigationCase c4 = cnh.getNavigationCase(fc, null,
            "test?foo=rab&amp;foo=rab2&foo2=rab3&amp;faces-redirect=true&amp;includeViewParams=true&");
        assertNotNull(c4);
        parameters = c4.getParameters();
        assertNotNull(parameters);
        assertTrue(c4.isRedirect());
        assertTrue(c4.isIncludeViewParams());
        assertEquals(2, parameters.size());
        fooParams = parameters.get("foo");
        assertNotNull(fooParams);
        assertEquals(2, fooParams.size());
        assertEquals("rab", fooParams.get(0));
        assertEquals("rab2", fooParams.get(1));
        foo2Params = parameters.get("foo2");
        assertEquals(1, foo2Params.size());
        assertEquals("rab3", foo2Params.get(0));

        // ensure invalid query string correctly handled
        NavigationCase c5 = cnh.getNavigationCase(fc, null, "test?");

        assertNotNull(c5);
        assertNull(c5.getParameters());
        assertFalse(c5.isRedirect());
        assertFalse(c5.isIncludeViewParams());

        // ensure redirect parameter el evaluation is performed more than once
        NavigationCase ncase = cnh.getNavigationCase(fc, null, "redirectOutcome3");
        String url = fc.getExternalContext().encodeRedirectURL(
            "/path.xhtml", evaluateExpressions(fc, ncase.getParameters()));
        System.out.println("URL: " + url);
        assertTrue(url.contains("param=1"));
        url = fc.getExternalContext().encodeRedirectURL("/path.xhtml",
                evaluateExpressions(fc,ncase.getParameters()));
        assertTrue(url.contains("param=2"));
        return "SUCCESS";
    }

    private void loadTestResultList() throws Exception {

        DocumentBuilderFactory f = DbfFactory.getFactory();
        f.setNamespaceAware(false);
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();

        Document d = builder.parse(((ServletContext)FacesContext.getCurrentInstance().getExternalContext().
            getContext()).getResourceAsStream("/WEB-INF/navigation-cases.xml"));
        NodeList navigationRules = d.getDocumentElement()
              .getElementsByTagName("test");
        for (int i = 0; i < navigationRules.getLength(); i++) {
            Node test = navigationRules.item(i);
            NamedNodeMap attributes = test.getAttributes();
            Node fromViewId = attributes.getNamedItem("fromViewId");
            Node fromAction = attributes.getNamedItem("fromAction");
            Node fromOutput = attributes.getNamedItem("fromOutcome");
            Node toViewId = attributes.getNamedItem("toViewId");
            createAndAccrueTestResult(((fromViewId != null) ? fromViewId.getTextContent().trim() : null),
                                      ((fromAction != null) ? fromAction.getTextContent().trim() : null),
                                      ((fromOutput != null) ? fromOutput.getTextContent().trim() : null),
                                      ((toViewId != null) ? toViewId.getTextContent().trim() : null));
        }
    }

    private void createAndAccrueTestResult(String fromViewId, String fromAction,
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

    private static final class WrapperNavigationHandler extends NavigationHandler {

        private NavigationHandler delegate;

        public WrapperNavigationHandler(NavigationHandler delegate) {
            this.delegate = delegate;
        }

        public void handleNavigation(FacesContext context, String fromAction, String outcome) {
            delegate.handleNavigation(context, fromAction, outcome);
        }
    }

    private Map<String, List<String>> evaluateExpressions(FacesContext context, Map<String, List<String>> map) {

        if (map != null && !map.isEmpty()) {
            Map<String, List<String>> ret = new HashMap<String, List<String>>(map.size());
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                ret.put(entry.getKey(), evaluateExpressions(context, entry.getValue()));
            }

            return ret;
        }

        return map;

    }

    private List<String> evaluateExpressions(FacesContext context, List<String> values) {
         if (!values.isEmpty()) {
             List<String> ret = new ArrayList<String>(values.size());
             Application app = context.getApplication();
             for (String val : values) {
                 if (val != null) {
                     String value = val.trim();
                     if (isExpression(value)) {
                         value = app.evaluateExpressionGet(context,
                                                           value,
                                                           String.class);
                     }
                     ret.add(value);
                 }
             }

             return ret;
         }
         return values;
     }

    private static boolean isExpression(String expression) {

        if (null == expression) {
            return false;
        }

        //check to see if attribute has an expression
        int start = expression.indexOf("#{");
        return start != -1 && expression.indexOf('}', start+2) != -1;
    }

    private String title = "Test Navigation Handler";
    public String getTitle() {
        return title;
    }

    private String status="";

    public String getStatus() {
        return status;
    }

}

