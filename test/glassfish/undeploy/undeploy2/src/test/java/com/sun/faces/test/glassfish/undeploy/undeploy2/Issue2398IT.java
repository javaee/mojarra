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
package com.sun.faces.test.glassfish.undeploy.undeploy2;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.test.junit.JsfServerExclude;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import com.sun.faces.test.junit.JsfVersion;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

@RunWith(JsfTestRunner.class)
public class Issue2398IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
        webClient.setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(60000);
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    /**
     * Test for issue #2398.
     *
     * 1. Test if the undeploy #1 application is active. 2. Test if the undeploy
     * #2 application is active. 3. Get the number of active InitFacesContexts.
     * 4. Undeploy 'undeploy #1' 5. Verify the number of active
     * InitFacesContexts stayed the same.
     *
     * @throws Exception when a serious error occurs.
     */
    @JsfTest(value = JsfVersion.JSF_2_2_0, 
            excludes = {JsfServerExclude.GLASSFISH_4_0, JsfServerExclude.GLASSFISH_4_1})
    @Test
    public void testIssue2398() throws Exception {
        HtmlPage page = webClient.getPage(webUrl.substring(0, webUrl.length() - 2) + "2/faces/index.xhtml");

        if (!(page.getWebResponse().getResponseHeaderValue("Server").equals("GlassFish Server Open Source Edition  4.0"))) {
            page = webClient.getPage(webUrl + "faces/index.xhtml");

            page = webClient.getPage(webUrl.substring(0, webUrl.length() - 2) + "1/faces/index.xhtml");
            assertTrue(page.asText().indexOf("Undeploy #1 is active!") != -1);

            page = webClient.getPage(webUrl + "faces/index.xhtml");
            assertTrue(page.asText().indexOf("Undeploy #2 is active!") != -1);

            page = webClient.getPage(webUrl + "faces/count.xhtml");
            Integer count = new Integer(page.asText().trim());

            WebRequest webRequest = new WebRequest(
                    new URL("http://localhost:4848/management/domain/applications/application/test-glassfish-undeploy-undeploy1"),
                    HttpMethod.DELETE);
            webRequest.setAdditionalHeader("X-Requested-By", "127.0.0.1");
            webClient.getPage(webRequest);

            try {
                webClient.setPrintContentOnFailingStatusCode(false);
                webClient.getPage(webUrl.substring(0, webUrl.length() - 2) + "1/faces/index.xhtml");
                fail("Undeploy #1 is active!");
            } catch (FailingHttpStatusCodeException exception) {
                assertEquals(404, exception.getStatusCode());
                webClient.setPrintContentOnFailingStatusCode(true);
            }

            page = webClient.getPage(webUrl + "faces/count.xhtml");
            Integer newCount = new Integer(page.asText().trim());
            assertTrue(count.intValue() >= newCount.intValue());
        }
    }
}
