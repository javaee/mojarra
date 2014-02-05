/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.systest.http;

import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestSuite;



 public class HttpMethodTestCase extends HtmlUnitFacesTestCase {


    public HttpMethodTestCase(String name) {
        super(name);
    }

    public static Test suite() {
        return (new TestSuite(HttpMethodTestCase.class));
    }
    
    static final String interweaving = "/faces/interweaving01.jsp";
    static final String interweavingRegEx = "(?s).*Begin\\s*test\\s*jsp include without verbatim\\s*interweaving\\s*works\\s*well!!\\s*End\\s*test\\s*jsp include without verbatim.*";
    
    static final String repeat = "/faces/facelets/uirepeat.xhtml";
    static final String repeatRegEx = "(?s).*ListFlavor is chocolate.*";

    public void testPositive() throws Exception {
        int [] rc = new int[1];
        // Ensure the GET request works as expected
        assertTrue(issueHttpRequest("GET", rc, interweaving).matches(interweavingRegEx));
        assertEquals(HttpURLConnection.HTTP_OK, rc[0]);

        // Ensure the POST request works as expected
        assertTrue(issueHttpRequest("POST", rc, interweaving).matches(interweavingRegEx));
        assertEquals(HttpURLConnection.HTTP_OK, rc[0]);

        // Ensure the PUT request works as expected
        assertTrue(issueHttpRequest("PUT", rc, repeat).matches(repeatRegEx));
        assertEquals(HttpURLConnection.HTTP_OK, rc[0]);

        // Ensure the DELETE request works as expected
        assertTrue(issueHttpRequest("DELETE", rc, repeat).matches(repeatRegEx));
        assertEquals(HttpURLConnection.HTTP_OK, rc[0]);

        // Ensure the HEAD request works as expected
        String result = issueHttpRequest("HEAD", rc, repeat);
        String [] tokens = result.split("[\\r\\n][\\r\\n]");        
        assertTrue(1 == tokens.length);
        assertEquals(HttpURLConnection.HTTP_OK, rc[0]);

        // Ensure the OPTIONS  request works as expected
        result = issueHttpRequest("OPTIONS", rc, repeat);
        tokens = result.split("[\\r\\n][\\r\\n]");        
        assertTrue(1 == tokens.length || "0".equals(tokens[1]));
        assertEquals(HttpURLConnection.HTTP_OK, rc[0]);

        // Ensure the GETBOGUSALLOWED request *does* work, because
        // we configured it in web.xml
        assertTrue(issueHttpRequest("GETBOGUSALLOWED", rc, repeat).matches(repeatRegEx));
        assertEquals(HttpURLConnection.HTTP_OK, rc[0]);

    }

     public void testNegative() throws Exception {
        int [] rc = new int[1];

        // Ensure the GET22 request does not work
        assertFalse("Bogus HTTP method was accepted by server.  Fail.",
                issueHttpRequest("GET22", rc, interweaving).matches(interweavingRegEx));
        assertFalse("Bogus HTTP method returned HTTP_OK status.  Fail.", HttpURLConnection.HTTP_OK == rc[0]);
     }
    
    private String issueHttpRequest(String methodName, int [] rc, String path) throws Exception {

        URL url = getURL(path);
        Socket s = new Socket(url.getHost(), url.getPort());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        String requestLine = methodName + " /" + contextPath + path + " HTTP/1.1\r\n";
        writer.write(requestLine);
        writer.write("Host: " + url.getHost() + ":" + url.getPort() + "\r\n");
        writer.write("User-Agent: systest-client\r\n");
        writer.write("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n");
        writer.write("Connection: close\r\n");
        writer.write("\r\n");
        writer.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String cur = null;
        StringBuilder builder = new StringBuilder();
        rc[0] = -1;
        while (null != (cur = reader.readLine())) {
            if (-1 == rc[0]) {
                String [] tokens = cur.split("\\s");
                rc[0] = Integer.valueOf(tokens[1]);
            }
            builder.append(cur).append("\n");
        }
        writer.close();

        
        return builder.toString();
    }


}
