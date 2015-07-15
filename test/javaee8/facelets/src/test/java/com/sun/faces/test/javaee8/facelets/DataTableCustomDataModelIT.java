/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.javaee8.facelets;

import static com.sun.faces.test.junit.JsfVersion.JSF_2_3_0_M03;
import static java.util.regex.Pattern.matches;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
// import org.junit.Ignore;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.test.junit.JsfTest;
import org.junit.Ignore;

public class DataTableCustomDataModelIT {
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
    @JsfTest(value = JSF_2_3_0_M03)
    @Ignore
    public void testExactClassMatch() throws Exception {
    	
    	// In this test a backing bean will return an object of type Child11.
    	// There's a DataModel registered for exactly this class, which should
    	// be picked up. 
    	//
    	// The (small) challenge is that there are also DataModels
    	// registered for super classes of Child11 (e.g. Child1), which can also
    	// handle a Child11, but these should NOT be picked up and the exact match
    	// should be preferred.
    	
    	HtmlPage page = webClient.getPage(webUrl + "datatableCustomDataModel11.xhtml");
        assertTrue(matches("(?s).*START.*11-member 1.*11-member 2.*END.*", page.asXml()));
    }
    
    @Test
    @JsfTest(value = JSF_2_3_0_M03)
    @Ignore
    public void testClosestSuperClassMatch() throws Exception {
    	
    	// In this test a backing bean will return an object of type Child111.
    	// There's NO DataModel registered for exactly this class. However, there
    	// is a DataModel registered for several super classes, which can all
    	// handle a Child111.
    	
    	// The challenge here is that the DataModel for the closest super class
    	// should be chosen, which in this test is the DataModel that handles
    	// a Child11.
    	
    	HtmlPage page = webClient.getPage(webUrl + "datatableCustomDataModel111.xhtml");
        assertTrue(matches("(?s).*START.*111-member 1.*111-member 2.*END.*", page.asXml()));
    }
}
