/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.servlet30.composite2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static org.junit.Assert.*;

public class Issue3077IT {

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

    @Test
    public void testCCAndInclude() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/ccAndInclude.xhtml");
        String pageText = page.asXml();
        
        String beforeText = pageText.substring(0, pageText.indexOf("cc is evaluated in a xhtml file included by the composite component"));
        
        assertTrue(beforeText.indexOf("#{cc['clientId']} evaluates to: external:internal") != -1);
        assertTrue(beforeText.indexOf("#{cc.clientId} evaluates to: external:internal") != -1);
        assertTrue(beforeText.indexOf("#{cc['attrs'].param} evaluates to: Test string") != -1);
        assertTrue(beforeText.indexOf("#{cc.attrs.param} evaluates to: Test string") != -1);
        
        String afterText = pageText.substring(pageText.indexOf("cc is evaluated in a xhtml file included by the composite component"));
        
        assertTrue(afterText.indexOf("#{cc['clientId']} evaluates to: external:internal") != -1);
        assertTrue(afterText.indexOf("#{cc.clientId} evaluates to: external:internal") != -1);
        assertTrue(afterText.indexOf("#{cc['attrs'].param} evaluates to: Test string") != -1);
        assertTrue(afterText.indexOf("#{cc.attrs.param} evaluates to: Test string") != -1);
    }
}
