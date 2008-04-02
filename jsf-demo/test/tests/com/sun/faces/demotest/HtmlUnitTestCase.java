/*
 * $Id: HtmlUnitTestCase.java,v 1.2 2003/12/17 15:20:15 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.demotest;

import junit.framework.TestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.Page;

import java.net.URL;


public class HtmlUnitTestCase extends TestCase {
    // target host
    protected String host;
    
    // target port
    protected String port;
    
    // target context
    protected String context;

    //
    // Test Methods
    //
    protected void setUp() throws Exception {
        host = System.getProperty("host");
        port = System.getProperty("port");
        context = System.getProperty("context.path");
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
}
