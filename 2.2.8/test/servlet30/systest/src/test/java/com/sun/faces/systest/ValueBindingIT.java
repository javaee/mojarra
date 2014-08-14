/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.systest;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValueBindingIT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }
    
    /*
     * ValueBinding Test #1 (Simple Bean Getter)
     */
    @Test
    public void testValueBinding1() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/valueBinding01.jsp");
        assertTrue(Pattern.matches("(?s).*/valueBinding01.jsp PASSED.*", page.asXml()));
    }

    /*
     * ValueBinding Test #2 (Simple String Property Getter)
     */
    @Test
    public void testValueBinding2() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/valueBinding02.jsp");
        assertTrue(Pattern.matches("(?s).*/valueBinding02.jsp PASSED.*", page.asXml()));
    }
    
    /*
     * ValueBinding Test #3 (Simple Integer Property Getter) 
     */
    @Test
    public void testValueBinding3() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/valueBinding03.jsp");
        assertTrue(Pattern.matches("(?s).*/valueBinding03.jsp PASSED.*", page.asXml()));
    }
    
    /*
     * ValueBinding Test #4 (Simple Boolean Property Getter)
     */
    @Test
    public void testValueBinding4() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/valueBinding04.jsp");
        assertTrue(Pattern.matches("(?s).*/valueBinding04.jsp PASSED.*", page.asXml()));
    }
    
    /*
     * ValueBinding Test #5 (Simple Integer Expression Getter)
     */
    @Test
    public void testValueBinding5() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/valueBinding05.jsp");
        assertTrue(Pattern.matches("(?s).*/valueBinding05.jsp PASSED.*", page.asXml()));
    }
    
    /*
     * ValueBinding Test #6 (Simple Boolean Expression Getter)
     */
    @Test
    public void testValueBinding6() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/valueBinding06.jsp");
        assertTrue(Pattern.matches("(?s).*/valueBinding06.jsp PASSED.*", page.asXml()));
    }
    
    /*
     * ValueBinding Test #7 (Mixed Literal and Expression Getter)
     */
    @Test
    public void testValueBinding7() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/valueBinding07.jsp");
        assertTrue(Pattern.matches("(?s).*/valueBinding07.jsp PASSED.*", page.asXml()));
    }
}
