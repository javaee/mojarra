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

package com.sun.faces.demotest;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import junit.framework.TestCase;


public class HtmlUnitTestCase extends TestCase {

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


    /** Added due to API change in HtmlUnit. */
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
        if (null == root) {
            return list;
        }
        if (null == list) {
            list = new ArrayList();
        }
        for (HtmlElement element : root.getAllHtmlChildElements()) {
            getAllElementsOfGivenClass(element, list, matchClass);
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

}
