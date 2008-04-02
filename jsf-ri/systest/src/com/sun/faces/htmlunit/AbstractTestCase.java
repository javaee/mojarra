/*
 * $Id: AbstractTestCase.java,v 1.15 2006/03/29 23:03:55 rlubke Exp $
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

package com.sun.faces.htmlunit;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;

import java.net.URL;
import java.util.Iterator;

import java.util.List;
import java.util.ArrayList;


/**
 * <p>Abstract base class for test cases utilizing HtmlUnit.</p>
 */

public abstract class AbstractTestCase extends TestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public AbstractTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // System property values used to configure the HTTP connection
    protected String contextPath = null;
    protected String host = null;
    protected int port = 0;

    // The current session identifier
    protected String sessionId = null;


    // The WebClient instance for this test case
    protected WebClient client = null;

    // The URL for our test application
    protected URL domainURL = null;

    // The HttpState for our domain URL
    protected HttpState state = null;


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {

        contextPath = System.getProperty("context.path");
        host = System.getProperty("host");
        port = Integer.parseInt(System.getProperty("port"));

        client = new WebClient();
        domainURL = getURL("/");
        WebRequestSettings settings = new WebRequestSettings(domainURL);
        WebResponse response = client.getWebConnection().getResponse(settings);
        
        state = client.getWebConnection().getState();

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(AbstractTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        client = null;
        domainURL = null;
        state = null;

    }


    // ------------------------------------------------- Individual Test Methods


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Extract and return the result of calling <code>asText()</code>
     * on the <code>&lt;body&gt;</code> element of this page.</p>
     *
     * @param page The <code>HtmlPage</code> to process
     */
    protected String getBodyText(HtmlPage page) {

        Object body =
            page.getDocumentElement().getHtmlElementsByTagName("body").get(0);

        if (body != null) {
            if (body instanceof HtmlBody) {
                return (((HtmlBody) body).asText());
            }
        }

        fail("This page does not have a <body> element");
        return (null); // To satisfy the compiler

    }


    /**
     * <p>Return the page for the specified context-relative path,
     * maintaining session affinity if <code>sessionId</code> is not null.</p>
     *
     * @param path Context-relative part of the path
     */
    protected HtmlPage getPage(String path) throws Exception {

        /* Cookies seem to be maintained automatically now
        if (sessionId != null) {
            //            System.err.println("Joining   session " + sessionId);
            client.addRequestHeader("Cookie", "JSESSIONID=" + sessionId);
        }
        */
	Object obj = client.getPage(getURL(path));
        HtmlPage page = (HtmlPage) obj;
        if (sessionId == null) {
            parseSession(page);
        }
        return (page);

    }


    /**
     * <p>Return a <code>URL</code> for the specified context-relative
     * path.</p>
     *
     * @param path Context relative path
     */
    protected URL getURL(String path) throws Exception {

        StringBuffer sb = new StringBuffer("http://");
        sb.append(host);
        if (port != 80) {
            sb.append(":");
            sb.append("" + port);
        }
        sb.append(contextPath);
        sb.append(path);
        return (new URL(sb.toString()));

    }


    /**
     * <p>Parse and save any session identifier from the specified page.</p>
     *
     * @param page The current page
     */
    protected void parseSession(HtmlPage page) {

        String value =
            page.getWebResponse().getResponseHeaderValue("Set-Cookie");
        if (value == null) {
            return;
        }
        int equals = value.indexOf("JSESSIONID=");
        if (equals < 0) {
            return;
        }
        value = value.substring(equals + "JSESSIONID=".length());
        int semi = value.indexOf(";");
        if (semi >= 0) {
            value = value.substring(0, semi);
        }
        sessionId = value;
        //        System.err.println("Beginning session " + sessionId);

    }


    protected boolean clearAllCookies() {
        if (null == state) {
            state = client.getWebConnection().getState();
            if (null == state) {
                return false;
            }
        }

        Cookie[] cookies = state.getCookies();
        if (null == cookies) {
            return false;
        }
        java.util.Date exp = null;
        long
            curTime = System.currentTimeMillis(),
            latestTime = curTime;
        // find the freshest cookie
        for (int i = 0, len = cookies.length; i < len; i++) {
            if (null != (exp = cookies[i].getExpiryDate())) {
                curTime = exp.getTime();
                if (latestTime < curTime) {
                    curTime = latestTime;
                }
            }
        }
        boolean result =
            state.purgeExpiredCookies(new java.util.Date(latestTime));
        return result;
    }


    /**
     * <p>Set up the session identifier cookie if it is not already there.</p>
     *
     * @param sessionId The new session identifier
     */
    /* Cookies seem to be maintained automatically now
    protected void setSessionId(String sessionId) {

        Cookie cookie = null;

        // Update the current cookie, if there is one
        Cookie cookies[] = state.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            cookie = cookies[i];
            if ("JSESSIONID".equals(cookie.getName()) &&
                host.equals(cookie.getDomain()) &&
                contextPath.equals(cookie.getPath())) {
                cookie.setValue(sessionId);
                return;
            }
        }

        // Create a new session identifier cookie
        cookie = new Cookie();
        cookie.setDomain(host);
        cookie.setDomainAttributeSpecified(true);
        cookie.setName("JSESSIONID");
        cookie.setPath(contextPath);
        cookie.setPathAttributeSpecified(true);
        cookie.setSecure(false);
        cookie.setValue(sessionId);
        cookie.setVersion(1);  // Assumes Tomcat knows how to deal with them
        state.addCookie(cookie);

    }
    */

    // Return the form with the specified "id" from the specified page
    // (HtmlPage.getFormByName() looks at "name" instead)
    protected HtmlForm getFormById(HtmlPage page, String id) {

        Iterator forms = page.getForms().iterator();
        while (forms.hasNext()) {
            HtmlForm form = (HtmlForm) forms.next();
            if (id.equals(form.getAttributeValue("id"))) {
                return (form);
            }
        }
        return (null);

    }


    /**
     * <p>Added to compensate for changes in the HtmlUnit 1.4 API.</p>
     * @see #getAllElementsOfGivenClass(com.gargoylesoftware.htmlunit.html.HtmlElement, java.util.List, Class)
     */
    protected List getAllElementsOfGivenClass(HtmlPage root, List list,
                                              Class matchClass) {

        return getAllElementsOfGivenClass(root.getDocumentElement(),
                                          list,
                                          matchClass);

    }

    /**
     * Depth first search from root to find all children that are
     * instances of HtmlInput.  Add them to the list.
     */
    protected List getAllElementsOfGivenClass(HtmlElement root, List list,
                                              Class matchClass) {
        Iterator iter = null;
        if (null == root) {
            return list;
        }
        if (null == list) {
            list = new ArrayList();
        }
        iter = root.getAllHtmlChildElements();
        while (iter.hasNext()) {
            getAllElementsOfGivenClass((HtmlElement) iter.next(), list,
                                       matchClass);
        }
        if (matchClass.isInstance(root)) {
            if (!list.contains(root)) {
                list.add(root);
            }
        }
        return list;
    }

    protected HtmlInput getInputContainingGivenId(HtmlPage root,
						  String id) {
	List list;
	int i;
	HtmlInput result = null;
	
	list = getAllElementsOfGivenClass(root, null, HtmlInput.class);
	for (i = 0; i < list.size(); i++) {
	    result = (HtmlInput) list.get(i);
	    if (-1 != result.getIdAttribute().indexOf(id)) {
		break;
	    }
	    result = null;
	}
	return result;
	
    }

    protected HtmlInput getNthInputContainingGivenId(HtmlPage root,
						     String id, 
						     int whichInput) {
	List list;
	int i, hitCount = 0;
	HtmlInput result = null;
	
	list = getAllElementsOfGivenClass(root, null, HtmlInput.class);
	for (i = 0; i < list.size(); i++) {
	    result = (HtmlInput) list.get(i);
	    if (-1 != result.getIdAttribute().indexOf(id) &&
		hitCount++ == whichInput) {
		break;
	    }
	    result = null;
	}
	return result;
	
    }

    protected HtmlInput getNthFromLastInputContainingGivenId(HtmlPage root,
							     String id, 
							     int whichInput) {
	List list;
	int i, hitCount = 0;
	HtmlInput result = null;
	
	list = getAllElementsOfGivenClass(root, null, HtmlInput.class); 
	for (i = list.size() - 1; i >= 0; i--) {
	    result = (HtmlInput) list.get(i);
	    if (-1 != result.getIdAttribute().indexOf(id) &&
		hitCount++ == whichInput) {
		break;
	    }
	    result = null;
	}
	return result;
	
    }


}
