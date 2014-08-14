/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.agnostic;

import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.Ignore;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * <p>Make sure that an application that replaces the ApplicationFactory
 * but uses the decorator pattern to allow the existing ApplicationImpl
 * to do the bulk of the requests works.</p>
 */

public class IssueReplaceVariableResolverAndAddELResolverProgrammaticallyIT {


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
        webClient = new WebClient();
    }

    /**
     * Tear down after testing.
     */
    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    /**
     *
     * <p>Verify that the bean is successfully resolved</p>
     */
    @Test
    public void testReplaceVariableResolverAndAddELResolverProgrammatically() throws Exception {
	HtmlPage page = webClient.getPage(webUrl + "faces/test.jsp");
	assertTrue(-1 != page.asText().indexOf("Invoking the variable resolver chain: success."));
	assertTrue(-1 != page.asText().indexOf("Invoking the variable resolver directly: success."));
	assertTrue(-1 != page.asText().indexOf("Invoking the EL resolver directly: true."));
	assertTrue(-1 != page.asText().indexOf("result: isReadOnly invoked directly."));
	assertTrue(-1 != page.asText().indexOf("Invoking the EL resolver via chain: true."));
	assertTrue(-1 != page.asText().indexOf("result: isReadOnly invoked thru chain."));
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("reload", true);
        page = (HtmlPage) button.click();
        String text = page.asXml();
        text = text.replaceAll(":[0-9]*\\)", "\\)");
        text = text.replaceAll("com.sun.faces.", "");
        text = text.replaceAll("toString() invocation", "");

        String [] orderedListOfStringsToFindInPage = {
            "FacesELResolverForFaces",
            "el.ImplicitObjectELResolver.getValue",
            "el.VariableResolverChainWrapper.getValue",
            "NewVariableResolver.resolveVariable",
            "NewELResolver.getValue",
            "el.ManagedBeanELResolver.resolveBean",
            "el.FacesResourceBundleELResolver.getValue",
            "el.ScopedAttributeELResolver.getValue",
            "FacesELResolverForJsp",
            "NewVariableResolver.resolveVariable",
            "NewELResolver.getValue"
        };
        boolean [] foundFlags = new boolean[orderedListOfStringsToFindInPage.length];
        int i,j;
        for (i = 0; i < foundFlags.length; i++) {
            foundFlags[i] = false;
        }
        String [] textSplitOnSpace = text.split(" ");
        j = 0;
        for (i = 0; i < textSplitOnSpace.length &&
                    j < orderedListOfStringsToFindInPage.length; i++) {
            if (textSplitOnSpace[i].contains(orderedListOfStringsToFindInPage[j])) {
                foundFlags[j++] = true;
            }
        }
        for (i = 0; i < foundFlags.length; i++) {
            if (!foundFlags[i]) {
                fail("Unable to find " + orderedListOfStringsToFindInPage[i] +
                     " at expected order in ELResolver chain.  Text: " + text);
            }
        }
    }

}
