/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.test.servlet40.exactmapping;

import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_1_4;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_2_1;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_3_1;
import static com.sun.faces.test.junit.JsfVersion.JSF_2_3_0_M10;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;

@RunWith(JsfTestRunner.class)
public class Spec1260IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.close();
    }
    
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1, WEBLOGIC_12_3_1})
    public void testExactMappedViewLoads() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "foo");
        String content = page.asXml();
        
        // Basic test that if the FacesServlet is mapped to /foo, the right view "foo.xhtml" is loaded.
        assertTrue(content.contains("This is page foo"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1, WEBLOGIC_12_3_1})
    public void testPostBackToExactMappedView() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "foo");
        
        page = page.getHtmlElementById("form:commandButton").click();
        
        String content = page.asXml();
        
        assertTrue(content.contains("foo method invoked"));
        
        // If page /foo postbacks to itself, the new URL should be /foo again
        assertTrue(page.getUrl().getPath().endsWith("/foo"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1, WEBLOGIC_12_3_1})
    public void testLinkToNonExactMappedView() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "foo");
        
        assertTrue(page.asXml().contains("This is page foo"));
        
        page = page.getHtmlElementById("form:button").click();
        
        String content = page.asXml();
        
        assertTrue(content.contains("This is page bar"));
        
        // view "bar" is not exact mapped, so should be loaded via the suffix
        // or prefix the FacesServlet is mapped to when coming from /foo
        
        String path = page.getUrl().getPath();
        
        assertTrue(path.endsWith("/bar.jsf") || path.endsWith("/faces/bar"));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1, WEBLOGIC_12_3_1})
    public void testPostBackOnLinkedNonExactMappedView() throws Exception {
        
        // Navigate from /foo to /bar.jsf
        HtmlPage page = webClient.getPage(webUrl + "foo");
        page = page.getHtmlElementById("form:button").click();
        
        // After navigating to a non-exact mapped view, a postback should stil work 
        page = page.getHtmlElementById("form:commandButton").click();
        assertTrue(page.asXml().contains("foo method invoked"));
        
        // Check we're indeed on bar.jsf or faces/bar
        String path = page.getUrl().getPath();
        assertTrue(path.endsWith("/bar.jsf") || path.endsWith("/faces/bar"));
    }
    
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1, WEBLOGIC_12_3_1})
    public void testResourceReferenceFromExactMappedView() throws Exception {
        
        HtmlPage page = webClient.getPage(webUrl + "foo");
        
        String content = page.asXml();
        
        // Runtime must have found out the mappings of the FacesServlet and used one of the prefix or suffix
        // mappings to render the reference to "jsf.js", which is not exactly mapped.
        assertTrue(content.contains("javax.faces.resource/jsf.js.jsf") || content.contains("javax.faces.resource/faces/jsf.js") );
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M10, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1, WEBLOGIC_12_3_1})
    public void testAjaxFromExactMappedView() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "foo");
        
        page = page.getHtmlElementById("form:commandButtonAjax").click();
        webClient.waitForBackgroundJavaScript(6000);
        
        String content = page.asXml();
        
        // AJAX from an exact-mapped view should work
        assertTrue(content.contains("partial request = true"));
        
        // Part of page not updated via AJAX so should not show
        assertTrue(!content.contains("should not see this"));
    }

    
   
}
