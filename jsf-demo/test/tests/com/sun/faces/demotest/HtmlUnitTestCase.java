/*
 * $Id: HtmlUnitTestCase.java,v 1.5 2004/01/29 15:51:22 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.demotest;

import junit.framework.TestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.*;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
     * Depth first search from root to find all children that are
     * instances of HtmlInput.  Add them to the list.
     *
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
