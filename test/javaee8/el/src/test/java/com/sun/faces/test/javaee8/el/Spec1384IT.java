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
 * https://glassfish.java.net/public/CDDLGPL_1_1.html
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
package com.sun.faces.test.javaee8.el;

import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_1_4;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_2_1;
import static com.sun.faces.test.junit.JsfVersion.JSF_2_3_0_M03;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;

/**
 * Tests both build time and render time availability of the correct #{component}
 * value.
 *
 * Expected rendered output:
 *
 *       id1-1:component1
 *       id2-1:componentA
 *       id1-2:component1
 *       
 *       id1-3:component2
 *       id2-2:componentA
 *       id1-4:component2

 * component1 wraps componentA, so this tests that inside component1
 * but before componentA, #{component} refers to component1. The implicitly
 * created Facelets component (UIInstructions) are known not to alter #{component}.
 * Inside componentA, #{component} needs to change to componentA, and directly outside
 * componentA but still inside component1 it needs to change back to component1.
 * 
 * This therefore tests that #{component} is able to change to deeper stacked components
 * and able to move back when that stack unwinds.
 *
 * The test is repeated to test that the same changes are possible at the top level.
 *
 */
@RunWith(JsfTestRunner.class)
public class Spec1384IT {

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
    @JsfTest(value = JSF_2_3_0_M03, excludes = { WEBLOGIC_12_2_1, WEBLOGIC_12_1_4 })
    public void testCompositeComponentFacelets() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/component.xhtml");
        
        String pageAsXml = page.asXml();
        
        int id11 = pageAsXml.indexOf("id1-1:component1");
        int id21 = pageAsXml.indexOf("id2-1:componentA");
        int id12 = pageAsXml.indexOf("id1-2:component1");
        
        int id13 = pageAsXml.indexOf("id1-3:component2");
        int id22 = pageAsXml.indexOf("id2-2:componentA");
        int id14 = pageAsXml.indexOf("id1-4:component2");
        
        assertTrue(
            id11 > -1 &&
            id21 > id11 &&
            id12 > id21 &&
            id13 > id12 &&
            id22 > id13 &&
            id14 > id22
         );
    }

}
