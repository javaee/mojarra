/*
 * $Id: AbstractTestCase.java,v 1.2 2003/09/05 18:57:04 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.htmlunit;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.URL;
import java.util.Iterator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;



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
        state = client.getWebConnection().getStateForUrl(domainURL);

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

        // The page itself corresponds to the <html> element
        Iterator topIterator = page.getChildElements().iterator();
        while (topIterator.hasNext()) {
            HtmlElement topElement = (HtmlElement) topIterator.next();
            if (topElement instanceof HtmlBody) {
                return (topElement.asText());
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
        HtmlPage page = (HtmlPage) client.getPage(getURL(path));
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


}
