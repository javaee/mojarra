/*
 * $Id: TestExternalContextImpl.java,v 1.2 2003/03/27 07:34:33 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestExternalContextImpl.java

package com.sun.faces.context;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageImpl;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.tree.SimpleTreeImpl;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;

import javax.faces.event.FacesEvent;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.context.ResponseWriter;
import javax.faces.webapp.ServletResponseWriter;
import java.io.PrintWriter;
import javax.faces.context.ResponseStream;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.RIConstants;
import javax.faces.render.RenderKit;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.sun.faces.ServletFacesTestCase;

/**
 *
 *  <B>TestExternalContextImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestExternalContextImpl.java,v 1.2 2003/03/27 07:34:33 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestExternalContextImpl extends ServletFacesTestCase
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

    public TestExternalContextImpl() {super("TestExternalContext");}
    public TestExternalContextImpl(String name) {super(name);}
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
        for (int i=0; i<supported.length; i++) {
            supported[i]=false;
        }
    }

//PENDING(rogerk) the unit test for ExternalContext should cast the Object instances 
// to the expected type.  It should test put and get.  It should test the
// UnsupportedOperationException is thrown when expected, etc.

// Tests constructor's ability to create contained objects...
    public void testConstructor() {
        ExternalContextImpl ecImpl = new ExternalContextImpl(getConfig().getServletContext(), 
            getRequest(), getResponse());

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

    public void testGetSession() {
        Object session = getFacesContext().getExternalContext().getSession(false);
        assertTrue (null != session);
        assertTrue(session instanceof HttpSession);
    }

    public void testGetRequest() {
        Object request = getFacesContext().getExternalContext().getRequest();
        assertTrue (null != request);
        assertTrue(request instanceof ServletRequest);
    }

    public void testGetResponse() {
        Object response = getFacesContext().getExternalContext().getResponse();
        assertTrue (null != response);
        assertTrue(response instanceof ServletResponse);
    }

    public void beginGetRequestParameterNames(WebRequest theRequest) {
        theRequest.addParameter("One", "one");
        theRequest.addParameter("Two", "two");
        theRequest.addParameter("Three", "three");
    }

    public void testGetRequestParameterNames() {
        Iterator iter = getFacesContext().getExternalContext().getRequestParameterNames();
        boolean oneFound=false,twoFound=false,threeFound=false;
        while (iter.hasNext()) {
            String paramName = (String)iter.next();
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
        Locale locale = getFacesContext().getExternalContext().getRequestLocale();
        assertTrue(locale.getLanguage().equals("de"));
    }

    public void beginGetRequestPathInfo(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/foo", "/bar", null);
    }

    public void testGetRequestPathInfo() {
        String pathInfo = getFacesContext().getExternalContext().getRequestPathInfo();
        assertTrue(pathInfo.equals("/bar"));
    } 

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

    public void beginGetRequestContextPath(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/foo", "/bar", null);
    }

    public void testGetRequestContextPath() {
        String contextPath = getFacesContext().getExternalContext().getRequestContextPath();
        assertTrue(contextPath.equals("/test"));
    }

    public void testGetInitParameter() {
        String expectedValue = (String)config.getServletContext().getInitParameter("saveStateInClient");
        String value = getFacesContext().getExternalContext().getInitParameter("saveStateInClient");
        assertTrue(expectedValue.equals(value));
    }

    public void testGetResourcePaths() {
        Set paths = getFacesContext().getExternalContext().getResourcePaths("/");
        Object[] pathArray = paths.toArray();
        assertTrue(pathArray.length > 0);
        boolean foundIt = false;
        // assert that WEB-INF is found (at least)
        for (int i=0; i<pathArray.length; i++) {
            if (pathArray[i].equals("/WEB-INF/")) {
                foundIt = true;
                break;
            }
        }
        assertTrue(foundIt);
    }

    public void testGetResourceAsStream() {
        InputStream is = getFacesContext().getExternalContext().getResourceAsStream("/TestExternalContext");
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
        System.out.println("Testing ApplicationMap methods...");
        Map applicationMap = getFacesContext().getExternalContext().getApplicationMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET]=true;
        supported[PUT]=true;
        supported[REMOVE]=true;
        testUnsupportedExceptions(applicationMap, supported);

        System.out.println("    Testing Put/Get/Remove Methods...");
        applicationMap.put("foo", "bar");
        String value = (String)applicationMap.get("foo");
        assertTrue(value.equals("bar"));
        String removed = (String)applicationMap.remove("foo");
        assertTrue(null == (String)applicationMap.get("foo"));
        assertTrue(removed.equals("bar"));
    }

    public void testSessionMap() {
        System.out.println("Testing SessionMap methods...");
        Map sessionMap = getFacesContext().getExternalContext().getSessionMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        initializeSupported();
        supported[GET]=true;
        supported[PUT]=true;
        supported[REMOVE]=true;
        testUnsupportedExceptions(sessionMap, supported);

        System.out.println("    Testing Put/Get/Remove Methods...");
        sessionMap.put("foo", "bar");
        String value = (String)sessionMap.get("foo");
        assertTrue(value.equals("bar"));
        String removed = (String)sessionMap.remove("foo");
        assertTrue(null == (String)sessionMap.get("foo"));
        assertTrue(removed.equals("bar"));
    }

    public void testRequestMap() {
        System.out.println("Testing RequestMap methods...");
        Map requestMap = getFacesContext().getExternalContext().getRequestMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET]=true;
        supported[PUT]=true;
        supported[REMOVE]=true;
        testUnsupportedExceptions(requestMap, supported);

        System.out.println("    Testing Put/Get/Remove Methods...");
        requestMap.put("foo", "bar");
        String value = (String)requestMap.get("foo");
        assertTrue(value.equals("bar"));
        String removed = (String)requestMap.remove("foo");
        assertTrue(null == (String)requestMap.get("foo"));
        assertTrue(removed.equals("bar"));
    }

    public void beginRequestParameterMap(WebRequest theRequest) {
        theRequest.addParameter("foo", "bar");
    }

    public void testRequestParameterMap() {
        System.out.println("Testing RequestParameterMap methods...");
        Map requestParameterMap = getFacesContext().getExternalContext().getRequestParameterMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET]=true;
        testUnsupportedExceptions(requestParameterMap, supported);

        System.out.println("    Testing Get Method...");
        assertTrue(requestParameterMap.get("foo") instanceof String);
        String value = (String)requestParameterMap.get("foo");
        assertTrue(value.equals("bar"));
    }

    public void beginRequestParameterValuesMap(WebRequest theRequest) {
        theRequest.addParameter("foo", "one");
        theRequest.addParameter("foo", "two");
        theRequest.addParameter("foo", "three");
    }

    public void testRequestParameterValuesMap() {
        System.out.println("Testing RequestParameterValuesMap methods...");
        Map requestParameterValuesMap = getFacesContext().getExternalContext().getRequestParameterValuesMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET]=true;
        testUnsupportedExceptions(requestParameterValuesMap, supported);

        System.out.println("    Testing Get Method...");
        assertTrue(requestParameterValuesMap.get("foo") instanceof String[]);
        String[] returnValues = (String[])requestParameterValuesMap.get("foo");
        String[] values = {"one", "two", "three",};
        assertTrue(Arrays.equals(values, returnValues));
    }

    public void beginRequestHeaderMap(WebRequest theRequest) {
        theRequest.addHeader("foo", "bar");
    }

    public void testRequestHeaderMap() {
        System.out.println("Testing RequestHeaderMap methods...");
        Map requestHeaderMap = getFacesContext().getExternalContext().getRequestHeaderMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET]=true;
        testUnsupportedExceptions(requestHeaderMap, supported);

        System.out.println("    Testing Get Method...");
        assertTrue(requestHeaderMap.get("foo") instanceof String);
        String value = (String)requestHeaderMap.get("foo");
        assertTrue(value.equals("bar"));
    }

    public void beginRequestHeaderValuesMap(WebRequest theRequest) {
        theRequest.addHeader("foo", "one");
        theRequest.addHeader("foo", "two");
        theRequest.addHeader("foo", "three");
    }

    public void testRequestHeaderValuesMap() {
        System.out.println("Testing RequestHeaderValuesMap methods...");
        Map requestHeaderValuesMap = getFacesContext().getExternalContext().getRequestHeaderValuesMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET]=true;
        testUnsupportedExceptions(requestHeaderValuesMap, supported);

        System.out.println("    Testing Get Method...");
        assertTrue(requestHeaderValuesMap.get("One") instanceof Enumeration);
        Enumeration enum = (Enumeration)requestHeaderValuesMap.get("foo");
        String returnValues = null;
        while (enum.hasMoreElements()) {
            returnValues = (String)enum.nextElement();
        }
        
        String value = "one,two,three";
        assertTrue(returnValues.equals(value));
    }

    public void beginRequestCookieMap(WebRequest theRequest) {
        theRequest.addCookie("foo", "bar");
    }

    public void testRequestCookieMap() {
        System.out.println("Testing RequestCookieMap methods...");
        Map requestCookieMap = getFacesContext().getExternalContext().getRequestCookieMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET]=true;
        testUnsupportedExceptions(requestCookieMap, supported);

        System.out.println("    Testing Get Method...");
        assertTrue(requestCookieMap.get("foo") instanceof Cookie);
        Cookie value = (Cookie)requestCookieMap.get("foo");
        assertTrue(value.getValue().equals("bar"));
    }

    public void testInitParameterMap() {
        System.out.println("Testing InitParameterMap methods...");
        String expectedValue = (String)config.getServletContext().getInitParameter("saveStateInClient");
        Map initParameterMap = getFacesContext().getExternalContext().getInitParameterMap();
        System.out.println("    Testing UnsupportedOperationException(s)...");
        initializeSupported();
        supported[GET]=true;
        testUnsupportedExceptions(initParameterMap, supported);

        System.out.println("    Testing Get Method...");
        assertTrue(initParameterMap.get("saveStateInClient") instanceof String);
        assertTrue(((String)initParameterMap.get("saveStateInClient")).equals(expectedValue));
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

} // end of class TestExternalContextImpl
