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

package com.sun.faces.htmlunit;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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

    // The cookie manager
    protected CookieManager cmanager = null;

    // The last requested page
    protected HtmlPage lastpage = null;


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {

        BrowserVersion browserVersion;

        contextPath = System.getProperty("context.path");
        host = System.getProperty("host");
        port = Integer.parseInt(System.getProperty("port"));
        String browser = System.getProperty("browser");

        if ("FF3".equals(browser)) {
            browserVersion = BrowserVersion.FIREFOX_3;
        } else if ("FF2".equals(browser)) {
            browserVersion = BrowserVersion.FIREFOX_2;
        } else if ("IE6".equals(browser)) {
            browserVersion = BrowserVersion.INTERNET_EXPLORER_6;
        } else {
            browserVersion = BrowserVersion.INTERNET_EXPLORER_7;
        }
        
        String proxyHost = System.getProperty("proxyHost");
        String proxyPort = System.getProperty("proxyPort");

        client = new WebClient(browserVersion);
        cmanager = client.getCookieManager();
        // Add an ajax controller to synchronize all ajax calls
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        domainURL = getURL("/");
        WebRequestSettings settings = new WebRequestSettings(domainURL);
        if (null != proxyHost && null != proxyPort) {
            settings.setProxyHost(proxyHost);
            int proxyPortInt = Integer.parseInt(proxyPort);
            settings.setProxyPort(proxyPortInt);
            ProxyConfig config = client.getProxyConfig();
            config.setProxyHost(proxyHost);
            config.setProxyPort(proxyPortInt);
            
        }
        WebResponse response = client.getWebConnection().getResponse(settings);

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
        cmanager = null;

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
        lastpage  = (HtmlPage) client.getPage(getURL(path));
        if (sessionId == null) {
            parseSession(lastpage);
        }
        return lastpage;

    }


    /**
     * The same as {@link #getPage(String)} except this uses the specified
     * WebClient.
     *
     * @param path   context-relative path
     * @param client WebClient
     * @return an HtmlPage instance
     * @throws Exception if an error occurs
     */
    protected HtmlPage getPage(String path, WebClient client) throws Exception {
        lastpage = (HtmlPage) client.getPage(getURL(path));
        if (sessionId == null) {
            parseSession(lastpage);
        }
        return lastpage;
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
        cmanager.clearCookies();
        return true;
    }

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
     *
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
        if (null == root) {
            return list;
        }
        if (null == list) {
            list = new ArrayList();
        }
        Iterable<HtmlElement> iterable = root.getAllHtmlChildElements();
        Iterator<HtmlElement> iter = iterable.iterator();
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

    protected String getText(String element) {
        return ((HtmlElement)lastpage.getHtmlElementById(element)).asText();
    }

    /*
      Check that the text of the element is equal to the supplied string
     */
    protected boolean check(String element, String expected) {
        return expected.equals(getText(element));
    }

    protected void checkTrue(String element, String expected) {
        assertTrue(element+":- Expected '"+expected+"', but received '"+getText(element)+"'", check(element,expected));
    }

}
