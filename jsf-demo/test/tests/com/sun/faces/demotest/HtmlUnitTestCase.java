/*
 * $Id: HtmlUnitTestCase.java,v 1.8 2005/02/08 19:24:36 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.demotest;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import junit.framework.TestCase;


public class HtmlUnitTestCase extends TestCase {

    private static final Log log = LogFactory.getLog(HtmlUnitTestCase.class);

    // target host
    protected String host;

    // target port
    protected String port;

    // target context
    protected String context;

    // The WebClient instance for this test case
    protected WebClient client = null;

    // The current session identifier
    protected String sessionId = null;


    //
    // Test Methods
    //
    protected void setUp() throws Exception {
        host = System.getProperty("host");
        port = System.getProperty("port");
        context = System.getProperty("context.path");
        client = new WebClient();
        super.setUp();
    }


    // Utility method to strip jsessionid information from values.
    // Servlet specification doesn't disallow a path from being
    // encoded even if the client is using cookies.
    protected String stripJsessionInfo(String value) {
        int idx = value.indexOf(";jsessionid");
        if (idx != -1) {
            return value.substring(0, idx);
        }
        return value;
    }


    // Get the initial page of the target application
    protected Page getInitialPage() throws Exception {
        WebClient client = new WebClient();
        client.setRedirectEnabled(true);
        return client.getPage(new URL("http://" + host + ":" + port + context));
    }


    /**
     * <p>Return the page for the specified context-relative path,
     * maintaining session affinity if <code>sessionId</code> is not null.</p>
     *
     * @param path Context-relative part of the path
     */
    protected HtmlPage getPage(String path) throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("Getting URL: " + getURL(path).toString());
        }

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
        if (null != port) {
            sb.append(":" + port);
        }
        sb.append(context);
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
     * Added due to API change in HtmlUnit.
     */
    protected List getAllElementsOfGivenClass(HtmlPage page, List list,
                                              Class matchClass) {

        return getAllElementsOfGivenClass(page.getDocumentElement(),
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


}
