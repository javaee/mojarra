/*
 * $Id: TestExternalContextImpl.java,v 1.11 2004/02/06 18:56:40 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestExternalContextImpl.java

package com.sun.faces.context;

import com.sun.faces.ServletFacesTestCase;
import org.apache.cactus.WebRequest;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * <B>TestExternalContextImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestExternalContextImpl.java,v 1.11 2004/02/06 18:56:40 rlubke Exp $
 */

public class TestExternalContextImpl extends ServletFacesTestCase {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    // These constants identify positions in the boolean supported array;
    // Each one is named after the existing method in the Map interface;
    // For example, "ApplicationMap" implementation of Map interface
    // supports "put" method, but "RequestParameterMap" implementation may not;
    // So, for "ApplicationMap" you could say:
    //    supported[PUT]=true;
    // Intitially, all array elements are set to false;

    public static final int CLEAR = 0;
    public static final int CONTAINS_KEY = 1;
    public static final int CONTAINS_VALUE = 2;
    public static final int ENTRY_SET = 3;
    public static final int GET = 4;
    public static final int HASH_CODE = 5;
    public static final int IS_EMPTY = 6;
    public static final int KEY_SET = 7;
    public static final int PUT = 8;
    public static final int PUT_ALL = 9;
    public static final int REMOVE = 10;
    public static final int SIZE = 11;
    public static final int VALUES = 12;

    public boolean[] supported = new boolean[13];

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers
//

    public TestExternalContextImpl() {
        super("TestExternalContext");
    }


    public TestExternalContextImpl(String name) {
        super(name);
    }
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//
    public void initializeSupported() {
        for (int i = 0; i < supported.length; i++) {
            supported[i] = false;
        }
    }

//PENDING(rogerk) the unit test for ExternalContext should cast the Object instances
// to the expected type.  It should test put and get.  It should test the
// UnsupportedOperationException is thrown when expected, etc.

// Tests constructor's ability to create contained objects...
    public void testConstructor() {
        ExternalContextImpl ecImpl = new ExternalContextImpl(
            getConfig().getServletContext(),
            getRequest(),
            getResponse());

        System.out.println("Testing getSession not null...");
        assertTrue(null != ecImpl.getSession(false));
        System.out.println("Testing getContext not null...");
        assertTrue(null != ecImpl.getContext());
        System.out.println("Testing getRequest not null...");
        assertTrue(null != ecImpl.getRequest());
        System.out.println("Testing getResponse not null...");
        assertTrue(null != ecImpl.getResponse());
        System.out.println("Testing getApplicationMap not null...");
        assertTrue(null != ecImpl.getApplicationMap());
        System.out.println("Testing getSessionMap not null...");
        assertTrue(null != ecImpl.getSessionMap());
        System.out.println("Testing getRequestMap not null...");
        assertTrue(null != ecImpl.getRequestMap());
    }


    public void testServletContext() {
        getConfig().getServletContext().setAttribute("foo", "bar");
        ServletContext sc = (ServletContext) getFacesContext()
            .getExternalContext()
            .getContext();
        assertTrue(null != sc.getAttribute("foo"));
        assertTrue(
            null !=
            getFacesContext().getExternalContext().getApplicationMap().get(
                "foo"));
    }


    public void testGetSession() {
        Object session = getFacesContext().getExternalContext().getSession(
            false);
        assertTrue(null != session);
        assertTrue(session instanceof HttpSession);
    }


    public void testGetRequest() {
        Object request = getFacesContext().getExternalContext().getRequest();
        assertTrue(null != request);
        assertTrue(request instanceof ServletRequest);
    }


    public void testGetResponse() {
        Object response = getFacesContext().getExternalContext().getResponse();
        assertTrue(null != response);
        assertTrue(response instanceof ServletResponse);
    }


    public void beginGetRequestParameterNames(WebRequest theRequest) {
        theRequest.addParameter("One", "one");
        theRequest.addParameter("Two", "two");
        theRequest.addParameter("Three", "three");
    }


    public void testGetRequestParameterNames() {
        Iterator iter = getFacesContext().getExternalContext()
            .getRequestParameterNames();
        boolean oneFound = false, twoFound = false, threeFound = false;
        while (iter.hasNext()) {
            String paramName = (String) iter.next();
            if (paramName.equals("One")) {
                oneFound = true;
            } else if (paramName.equals("Two")) {
                twoFound = true;
            } else if (paramName.equals("Three")) {
                threeFound = true;
            }
        }
        assertTrue(oneFound && twoFound && threeFound);
    }


    public void beginGetLocale(WebRequest theRequest) {
        theRequest.addHeader("Accept-Language", "de");
    }


    public void testGetLocale() {
        Locale locale = getFacesContext().getExternalContext()
            .getRequestLocale();
        assertTrue(locale.getLanguage().equals("de"));
    }


    public void beginGetRequestPathInfo(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/foo", "/bar", null);
    }


    public void testGetRequestPathInfo() {
        String pathInfo = getFacesContext().getExternalContext()
            .getRequestPathInfo();
        assertTrue(pathInfo.equals("/bar"));
    }

    // PENDING(craigmcc) - Comment out this test because on my platform
    // the getRequestCookies() call returns null
    /*
    public void beginGetRequestCookies(WebRequest theRequest) {
        theRequest.addCookie("One", "one");
        theRequest.addCookie("Two", "two");
        theRequest.addCookie("Three", "three");
    }

    public void testGetRequestCookies() {
        Cookie[] cookies = getFacesContext().getExternalContext().getRequestCookies();
        boolean oneNFound=false,twoNFound=false,threeNFound=false;
        boolean oneVFound=false,twoVFound=false,threeVFound=false;
        for (int i=0; i<cookies.length; i++) {
            if (cookies[i].getName().equals("One")) {
                oneNFound = true;
            } else if (cookies[i].getName().equals("Two")) {
                twoNFound = true;
            } else if (cookies[i].getName().equals("Three")) {
                threeNFound = true;
            }
            if (cookies[i].getValue().equals("one")) {
                oneVFound = true;
            } else if (cookies[i].getValue().equals("two")) {
                twoVFound = true;
            } else if (cookies[i].getValue().equals("three")) {
                threeVFound = true;
            }
        }
        assertTrue(oneNFound && twoNFound && threeNFound &&
            oneVFound && twoVFound && threeVFound);
    }
    */

    public void beginGetRequestContextPath(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/foo", "/bar", null);
    }


    public void testGetRequestContextPath() {
        String contextPath = getFacesContext().getExternalContext()
            .getRequestContextPath();
        assertTrue(contextPath.equals("/test"));
    }


    public void testGetInitParameter() {
        String expectedValue = config.getServletContext().getInitParameter(
            "testInitParam");
        String value = getFacesContext().getExternalContext().getInitParameter(
            "testInitParam");
        assertTrue(expectedValue.equals(value));
    }


    public void testGetResourcePaths() {
        Set paths = getFacesContext().getExternalContext().getResourcePaths(
            "/");
        Object[] pathArray = paths.toArray();
        assertTrue(pathArray.length > 0);
        boolean foundIt = false;
        // assert that WEB-INF is found (at least)
        for (int i = 0; i < pathArray.length; i++) {
            if (pathArray[i].equals("/WEB-INF/")) {
                foundIt = true;
                break;
            }
        }
        assertTrue(foundIt);
    }


    public void testGetResourceAsStream() {
        InputStream is = getFacesContext().getExternalContext()
            .getResourceAsStream("/TestExternalContext");
        assertTrue(null != is);
    }

    //PENDNG(rogerk) not sure how to handle these...

    public void testEncodeActionURL() {
    }


    public void testEncodeResourceURL() {
    }


    public void testEncodeNamespaceURL() {
    }


    public void testEncodeURL() {
    }


    public void testDispatchMessage() {
    }


    public void testApplicatonMap() {
        System.out.println("Testing ApplicationMap...");
        Map applicationMap = getFacesContext().getExternalContext()
            .getApplicationMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET] = true;
        supported[PUT] = true;
        supported[REMOVE] = true;
        supported[CONTAINS_KEY] = true;
        supported[CONTAINS_VALUE] = true;
        supported[ENTRY_SET] = true;
        supported[VALUES] = true;
        supported[HASH_CODE] = true;
        supported[IS_EMPTY] = true;
        supported[KEY_SET] = true;
        supported[SIZE] = true;

        testUnsupportedExceptions(applicationMap, supported);

        System.out.println(
            "    Testing supported methods of ApplicationMap...");
        applicationMap.put("foo", "bar");
        String value = (String) applicationMap.get("foo");
        assertTrue(value.equals("bar"));
        String removed = (String) applicationMap.remove("foo");
        assertTrue(null == (String) applicationMap.get("foo"));
        assertTrue(removed.equals("bar"));
        applicationMap.put("foo", "bar");
        assertTrue(applicationMap.containsKey("foo"));
        assertTrue(applicationMap.containsValue("bar"));
        assertTrue(!applicationMap.entrySet().isEmpty());
        assertTrue(!applicationMap.values().isEmpty());
        assertTrue(!applicationMap.keySet().isEmpty());
        assertTrue(applicationMap.size() >= 1);
        assertTrue(
            applicationMap.hashCode() ==
            getFacesContext().getExternalContext().getApplicationMap()
            .hashCode());
        assertTrue(applicationMap.equals(
            getFacesContext().getExternalContext().getApplicationMap()));
        assertTrue(!applicationMap.equals(null));
        assertTrue(!applicationMap.equals(new HashMap()));
        applicationMap.remove("foo");
    }


    public void testSessionMap() {
        System.out.println("Testing SessionMap...");
        Map sessionMap = getFacesContext().getExternalContext().getSessionMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET] = true;
        supported[PUT] = true;
        supported[REMOVE] = true;
        supported[CONTAINS_KEY] = true;
        supported[CONTAINS_VALUE] = true;
        supported[ENTRY_SET] = true;
        supported[VALUES] = true;
        supported[HASH_CODE] = true;
        supported[IS_EMPTY] = true;
        supported[KEY_SET] = true;
        supported[SIZE] = true;

        testUnsupportedExceptions(sessionMap, supported);

        System.out.println("    Testing supported methods of SessionMap...");
        sessionMap.put("foo", "bar");
        String value = (String) sessionMap.get("foo");
        assertTrue(value.equals("bar"));
        String removed = (String) sessionMap.remove("foo");
        assertTrue(null == (String) sessionMap.get("foo"));
        assertTrue(removed.equals("bar"));
        sessionMap.put("foo", "bar");
        assertTrue(sessionMap.containsKey("foo"));
        assertTrue(sessionMap.containsValue("bar"));
        assertTrue(!sessionMap.entrySet().isEmpty());
        assertTrue(!sessionMap.values().isEmpty());
        assertTrue(!sessionMap.keySet().isEmpty());
        assertTrue(sessionMap.size() >= 1);
        assertTrue(sessionMap.hashCode() ==
                   getFacesContext().getExternalContext().getSessionMap()
                   .hashCode());
        assertTrue(sessionMap.equals(
            getFacesContext().getExternalContext().getSessionMap()));
        assertTrue(!sessionMap.equals(null));
        assertTrue(!sessionMap.equals(new HashMap()));
        sessionMap.remove("foo");
    }


    public void testRequestMap() {
        System.out.println("Testing RequestMap...");
        Map requestMap = getFacesContext().getExternalContext().getRequestMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET] = true;
        supported[PUT] = true;
        supported[REMOVE] = true;
        supported[CONTAINS_KEY] = true;
        supported[CONTAINS_VALUE] = true;
        supported[ENTRY_SET] = true;
        supported[VALUES] = true;
        supported[HASH_CODE] = true;
        supported[IS_EMPTY] = true;
        supported[KEY_SET] = true;
        supported[SIZE] = true;
        testUnsupportedExceptions(requestMap, supported);

        System.out.println("    Testing supported methods of RequestMap...");
        requestMap.put("foo", "bar");
        String value = (String) requestMap.get("foo");
        assertTrue(value.equals("bar"));
        String removed = (String) requestMap.remove("foo");
        assertTrue(null == (String) requestMap.get("foo"));
        assertTrue(removed.equals("bar"));
        requestMap.put("foo", "bar");
        assertTrue(requestMap.containsKey("foo"));
        assertTrue(requestMap.containsValue("bar"));
        assertTrue(!requestMap.entrySet().isEmpty());
        assertTrue(!requestMap.values().isEmpty());
        assertTrue(!requestMap.keySet().isEmpty());
        assertTrue(requestMap.size() >= 1);
        assertTrue(requestMap.hashCode() ==
                   getFacesContext().getExternalContext().getRequestMap()
                   .hashCode());
        assertTrue(requestMap.equals(
            getFacesContext().getExternalContext().getRequestMap()));
        assertTrue(!requestMap.equals(null));
        assertTrue(!requestMap.equals(new HashMap()));
        requestMap.remove("foo");
    }


    public void beginRequestParameterMap(WebRequest theRequest) {
        theRequest.addParameter("foo", "bar");
    }


    public void testRequestParameterMap() {
        System.out.println("Testing RequestParameterMap...");
        Map requestParameterMap = getFacesContext().getExternalContext()
            .getRequestParameterMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET] = true;
        supported[CONTAINS_KEY] = true;
        supported[CONTAINS_VALUE] = true;
        supported[ENTRY_SET] = true;
        supported[VALUES] = true;
        supported[HASH_CODE] = true;
        supported[IS_EMPTY] = true;
        supported[KEY_SET] = true;
        supported[SIZE] = true;
        testUnsupportedExceptions(requestParameterMap, supported);

        System.out.println(
            "    Testing supported methods of RequestParameterMap...");
        assertTrue(requestParameterMap.get("foo") instanceof String);
        String value = (String) requestParameterMap.get("foo");
        assertTrue(value.equals("bar"));
        assertTrue(requestParameterMap.containsKey("foo"));
        assertTrue(requestParameterMap.containsValue("bar"));
        assertTrue(!requestParameterMap.entrySet().isEmpty());
        assertTrue(!requestParameterMap.values().isEmpty());
        assertTrue(!requestParameterMap.keySet().isEmpty());
        assertTrue(requestParameterMap.size() >= 1);
        assertTrue(
            requestParameterMap.hashCode() ==
            getFacesContext().getExternalContext().getRequestParameterMap()
            .hashCode());
        assertTrue(requestParameterMap.equals(
            getFacesContext().getExternalContext().getRequestParameterMap()));
        assertTrue(!requestParameterMap.equals(null));
        assertTrue(!requestParameterMap.equals(new HashMap()));
    }


    public void beginRequestParameterValuesMap(WebRequest theRequest) {
        theRequest.addParameter("foo", "one");
        theRequest.addParameter("foo", "two");
        theRequest.addParameter("foo", "three");
    }


    public void testRequestParameterValuesMap() {
        System.out.println("Testing RequestParameterValuesMap...");
        Map requestParameterValuesMap = getFacesContext().getExternalContext()
            .getRequestParameterValuesMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET] = true;
        supported[CONTAINS_KEY] = true;
        supported[CONTAINS_VALUE] = true;
        supported[ENTRY_SET] = true;
        supported[VALUES] = true;
        supported[HASH_CODE] = true;
        supported[IS_EMPTY] = true;
        supported[KEY_SET] = true;
        supported[SIZE] = true;
        testUnsupportedExceptions(requestParameterValuesMap, supported);

        System.out.println(
            "    Testing supported methods of RequesParameterValuesMap...");
        assertTrue(requestParameterValuesMap.get("foo") instanceof String[]);
        String[] returnValues = (String[]) requestParameterValuesMap.get("foo");
        String[] values = {"one", "two", "three", };
        assertTrue(Arrays.equals(values, returnValues));
        assertTrue(requestParameterValuesMap.containsKey("foo"));
        assertTrue(
            requestParameterValuesMap.containsValue(
                request.getParameterValues("foo")));
        assertTrue(!requestParameterValuesMap.entrySet().isEmpty());
        assertTrue(!requestParameterValuesMap.values().isEmpty());
        assertTrue(!requestParameterValuesMap.keySet().isEmpty());
        assertTrue(requestParameterValuesMap.size() >= 1);
        assertTrue(
            requestParameterValuesMap.hashCode() ==
            getFacesContext().getExternalContext()
            .getRequestParameterValuesMap()
            .hashCode());
        assertTrue(
            requestParameterValuesMap.equals(
                getFacesContext().getExternalContext()
                .getRequestParameterValuesMap()));
        assertTrue(!requestParameterValuesMap.equals(null));
        assertTrue(!requestParameterValuesMap.equals(new HashMap()));
    }


    public void beginRequestHeaderMap(WebRequest theRequest) {
        theRequest.addHeader("foo", "bar");
    }


    public void testRequestHeaderMap() {
        System.out.println("Testing RequestHeaderMap...");
        Map requestHeaderMap = getFacesContext().getExternalContext()
            .getRequestHeaderMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET] = true;
        supported[CONTAINS_KEY] = true;
        supported[CONTAINS_VALUE] = true;
        supported[ENTRY_SET] = true;
        supported[VALUES] = true;
        supported[HASH_CODE] = true;
        supported[IS_EMPTY] = true;
        supported[KEY_SET] = true;
        supported[SIZE] = true;
        testUnsupportedExceptions(requestHeaderMap, supported);

        System.out.println(
            "    Testing supported methods of RequesHeaderMap...");
        assertTrue(requestHeaderMap.get("foo") instanceof String);
        String value = (String) requestHeaderMap.get("foo");
        assertTrue(value.equals("bar"));
        assertTrue(requestHeaderMap.containsKey("foo"));
        assertTrue(requestHeaderMap.containsValue("bar"));
        assertTrue(!requestHeaderMap.entrySet().isEmpty());
        assertTrue(!requestHeaderMap.values().isEmpty());
        assertTrue(!requestHeaderMap.keySet().isEmpty());
        assertTrue(requestHeaderMap.size() >= 1);
        assertTrue(
            requestHeaderMap.hashCode() ==
            getFacesContext().getExternalContext().getRequestHeaderMap()
            .hashCode());
        assertTrue(requestHeaderMap.equals(
            getFacesContext().getExternalContext().getRequestHeaderMap()));
        assertTrue(!requestHeaderMap.equals(null));
        assertTrue(!requestHeaderMap.equals(new HashMap()));
    }


    public void beginRequestHeaderValuesMap(WebRequest theRequest) {
        theRequest.addHeader("foo", "one");
        theRequest.addHeader("foo", "two");
        theRequest.addHeader("foo", "three");
    }


    public void testRequestHeaderValuesMap() {
        System.out.println("Testing RequestHeaderValuesMap...");
        Map requestHeaderValuesMap = getFacesContext().getExternalContext()
            .getRequestHeaderValuesMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET] = true;
        supported[CONTAINS_KEY] = true;
        supported[CONTAINS_VALUE] = true;
        supported[ENTRY_SET] = true;
        supported[VALUES] = true;
        supported[HASH_CODE] = true;
        supported[IS_EMPTY] = true;
        supported[KEY_SET] = true;
        supported[SIZE] = true;
        testUnsupportedExceptions(requestHeaderValuesMap, supported);

        System.out.println(
            "    Testing supported methods of RequesHeaderValuesMap...");
        assertTrue(requestHeaderValuesMap.get("One") instanceof Enumeration);
        Enumeration enum = (Enumeration) requestHeaderValuesMap.get("foo");
        String returnValues = null;
        while (enum.hasMoreElements()) {
            returnValues = (String) enum.nextElement();
        }

        String value = "one,two,three";
        assertTrue(returnValues.equals(value));
        assertTrue(requestHeaderValuesMap.containsKey("foo"));
        assertTrue(
            requestHeaderValuesMap.containsValue(request.getHeaders("foo")));
        assertTrue(!requestHeaderValuesMap.containsValue(null));
        assertTrue(!requestHeaderValuesMap.containsValue(new Integer(1)));
        assertTrue(!requestHeaderValuesMap.containsValue(
            new TestEnumeration(new String[]{"one", "two", "three"})));
        assertTrue(requestHeaderValuesMap.containsValue(
            new TestEnumeration(new String[]{"one,two,three"})));
        assertTrue(!requestHeaderValuesMap.containsValue(
            new TestEnumeration(new String[]{"one,three,two"})));
        assertTrue(!requestHeaderValuesMap.containsValue(
            new TestEnumeration(new String[]{"one,two,three", "four"})));
        assertTrue(!requestHeaderValuesMap.entrySet().isEmpty());
        assertTrue(!requestHeaderValuesMap.values().isEmpty());
        assertTrue(!requestHeaderValuesMap.keySet().isEmpty());
        assertTrue(requestHeaderValuesMap.size() >= 1);
        assertTrue(
            requestHeaderValuesMap.hashCode() ==
            getFacesContext().getExternalContext().getRequestHeaderValuesMap()
            .hashCode());
        assertTrue(
            requestHeaderValuesMap.equals(
                getFacesContext().getExternalContext()
                .getRequestHeaderValuesMap()));
        assertTrue(!requestHeaderValuesMap.equals(null));
        assertTrue(!requestHeaderValuesMap.equals(new HashMap()));
    }

    // PENDING(craigmcc) - Comment out this test because on my platform
    // the getRequestCookies() call returns null
    /*
    public void beginRequestCookieMap(WebRequest theRequest) {
        theRequest.addCookie("foo", "bar");
    }

    public void testRequestCookieMap() {
        System.out.println("Testing RequestCookieMap...");
        Map requestCookieMap = getFacesContext().getExternalContext().getRequestCookieMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET] = true;
        supported[CONTAINS_KEY] = true;
        supported[CONTAINS_VALUE] = true;
        supported[ENTRY_SET] = true;
        supported[VALUES] = true;
        supported[HASH_CODE] = true;
        supported[IS_EMPTY] = true;
        supported[KEY_SET] = true;
        supported[SIZE] = true;
        testUnsupportedExceptions(requestCookieMap, supported);

        System.out.println("    Testing supported methods of RequestCookieMap...");
        assertTrue(requestCookieMap.get("foo") instanceof Cookie);
        Cookie value = (Cookie)requestCookieMap.get("foo");
        assertTrue(value.getValue().equals("bar"));
        assertTrue(requestCookieMap.containsKey("foo"));
        assertTrue(requestCookieMap.containsValue("bar"));
        assertTrue(!requestCookieMap.entrySet().isEmpty());
        assertTrue(!requestCookieMap.values().isEmpty());
        assertTrue(!requestCookieMap.keySet().isEmpty());
        assertTrue(requestCookieMap.size() >= 1);
        assertTrue(requestCookieMap.hashCode() ==
            getFacesContext().getExternalContext().getRequestCookieMap().hashCode());
        assertTrue(requestCookieMap.equals(
        getFacesContext().getExternalContext().getRequestCookieMap()));
        assertTrue(!requestCookieMap.equals(null));
        assertTrue(!requestCookieMap.equals(new HashMap()));
    }
    */

    public void testInitParameterMap() {
        System.out.println("Testing InitParameterMap...");
        String expectedValue = config.getServletContext().getInitParameter(
            "testInitParam");
        Map initParameterMap = getFacesContext().getExternalContext()
            .getInitParameterMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET] = true;
        supported[CONTAINS_KEY] = true;
        supported[CONTAINS_VALUE] = true;
        supported[ENTRY_SET] = true;
        supported[VALUES] = true;
        supported[HASH_CODE] = true;
        supported[IS_EMPTY] = true;
        supported[KEY_SET] = true;
        supported[SIZE] = true;
        testUnsupportedExceptions(initParameterMap, supported);

        System.out.println(
            "    Testing supported methods of InitParameterMap...");
        assertTrue(initParameterMap.get("testInitParam") instanceof String);
        assertTrue(
            (initParameterMap.get("testInitParam")).equals(expectedValue));
        assertTrue(initParameterMap.containsKey("testInitParam"));
        assertTrue(initParameterMap.containsValue(expectedValue));
        assertTrue(!initParameterMap.entrySet().isEmpty());
        assertTrue(!initParameterMap.values().isEmpty());
        assertTrue(!initParameterMap.keySet().isEmpty());
        assertTrue(initParameterMap.size() >= 1);
        assertTrue(
            initParameterMap.hashCode() ==
            getFacesContext().getExternalContext().getInitParameterMap()
            .hashCode());
        assertTrue(initParameterMap.equals(
            getFacesContext().getExternalContext().getInitParameterMap()));
        assertTrue(!initParameterMap.equals(null));
        assertTrue(!initParameterMap.equals(new HashMap()));
    }


    private void testUnsupportedExceptions(Map map, boolean[] supported) {

        boolean exceptionThrown = false;

        if (!supported[CLEAR]) {
            exceptionThrown = false;
            try {
                map.clear();
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[CONTAINS_KEY]) {
            exceptionThrown = false;
            try {
                map.containsKey("foo");
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[CONTAINS_VALUE]) {
            exceptionThrown = false;
            try {
                map.containsValue("foo");
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[ENTRY_SET]) {
            exceptionThrown = false;
            try {
                map.entrySet();
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[GET]) {
            exceptionThrown = false;
            try {
                map.get("foo");
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[HASH_CODE]) {
            exceptionThrown = false;
            try {
                map.hashCode();
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[IS_EMPTY]) {
            exceptionThrown = false;
            try {
                map.isEmpty();
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[KEY_SET]) {
            exceptionThrown = false;
            try {
                map.keySet();
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[PUT]) {
            exceptionThrown = false;
            try {
                map.put("foo", "bar");
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[PUT_ALL]) {
            exceptionThrown = false;
            try {
                map.putAll(new HashMap());
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[REMOVE]) {
            exceptionThrown = false;
            try {
                map.remove("foo");
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        if (!supported[SIZE]) {
            exceptionThrown = false;
            try {
                map.size();
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);

        }

        if (!supported[VALUES]) {
            exceptionThrown = false;
            try {
                map.values();
            } catch (UnsupportedOperationException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }
    }


    static class TestEnumeration implements Enumeration {

        List myList = new ArrayList();
        Iterator i = null;


        TestEnumeration(String[] values) {
            for (int i = 0; i < values.length; i++) {
                myList.add(values[i]);
            }
            i = myList.iterator();
        }


        public boolean hasMoreElements() {
            return i.hasNext();
        }


        public Object nextElement() {
            return i.next();
        }
    }


} // end of class TestExternalContextImpl
