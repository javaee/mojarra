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
package com.sun.faces.test.webprofile.flow.issue4279getNavigationCaseNotIdempotent;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Assert;
import static org.junit.Assert.fail;

public class Issue4279GetNavigationCaseNotIdempotentIT {
    /**
     * Stores the web URL.
     */
    private String webUrl;
    /**
     * Stores the web client.
     */
    private WebClient webClient;

    /**
     * Setup before testing.
     * 
     * @throws Exception when a serious error occurs.
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * Cleanup after testing.
     * 
     * @throws Exception when a serious error occurs.
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Setup before testing.
     */
    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
System.err.println("setUp: webUrl = " + webUrl);
        webClient = new WebClient();
    }

    /**
     * Tear down after testing.
     */
    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testIssue4279GetNavigationCaseNotIdempotent() throws Exception {

		doTestIssue4279GetNavigationCaseNotIdempotent("call_getNavigationCase_and_returnFromFlow2");
		doTestIssue4279GetNavigationCaseNotIdempotent("returnFromFlow2");
	}

	private void doTestIssue4279GetNavigationCaseNotIdempotent(String returnFromFlow2ButtonId) throws IOException {

		HtmlPage page = webClient.getPage(webUrl);
		assertNotNull(page.getElementById("flow1"));
		HtmlInput flow1Button = (HtmlInput) page.getElementById("flow1");
		page = flow1Button.click();
		assertNotNull(page.getElementById("callFlow2"));
		HtmlInput callFlow2Button = (HtmlInput) page.getElementById("callFlow2");
		page = callFlow2Button.click();
		assertNotNull(page.getElementById("returnFromFlow2"));
		HtmlInput returnFromFlow2Button = (HtmlInput) page.getElementById(returnFromFlow2ButtonId);
		try {
			page = returnFromFlow2Button.click();
		} catch(FailingHttpStatusCodeException exception) {
			throw new AssertionError("Failed to exit flow2 and return to flow1 after clicking " + returnFromFlow2ButtonId, exception);
		}
		assertNotNull(page.getElementById("returnFromFlow1"));
		HtmlInput returnFromFlow1Button = (HtmlInput) page.getElementById("returnFromFlow1");
		page = returnFromFlow1Button.click();
		assertNotNull(page.getElementById("flow1"));
	}
}
