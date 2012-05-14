/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU General
 * Public License Version 2 only ("GPL") or the Common Development and
 * Distribution License("CDDL") (collectively, the "License"). You may not use
 * this file except in compliance with the License. You can obtain a copy of the
 * License at https://glassfish.dev.java.net/public/CDDLGPL_1_1.html or
 * packager/legal/LICENSE.txt. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception: Oracle designates this particular file as subject to
 * the "Classpath" exception as provided by Oracle in the GPL Version 2 section
 * of the License file that accompanied this code.
 *
 * Modifications: If applicable, add the following below the License Header,
 * with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s): If you wish your version of this file to be governed by only
 * the CDDL or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution under the
 * [CDDL or GPL Version 2] license." If you don't indicate a single choice of
 * license, a recipient has the option to distribute your version of this file
 * under either the CDDL, the GPL Version 2 or to extend the choice of license
 * to its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright holder.
 */
package com.sun.faces.test.agnostic.dynamic;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Issue2373IT {

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
    public void testMoveComponent() throws Exception {

        /*
         * Make sure the Moveable HELLO is before the first panelBox11
         */
        HtmlPage page = webClient.getPage(webUrl + "faces/moveComponent.xhtml");
        assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:panelBox11"));

        /**
         * After clicking make sure it is inside the first panelBox11
         */
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form1:commandButton11");
        page = button.click();
        assertTrue(page.asXml().indexOf("Moveable HELLO text") > page.asXml().indexOf("form1:panelBox11"));
        assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:commandButton11"));

        /**
         * After clicking the same button make sure it is still there.
         */
        button = (HtmlSubmitInput) page.getElementById("form1:commandButton11");
        page = button.click();
        assertTrue(page.asXml().indexOf("Moveable HELLO text") > page.asXml().indexOf("form1:panelBox11"));
        assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:commandButton11"));
    }

    @Test
    public void testMoveComponent2() throws Exception {

        /*
         * Make sure the Moveable HELLO is before the first panelBox11
         */
        HtmlPage page = webClient.getPage(webUrl + "faces/moveComponent.xhtml");
        assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:subview1:panelBox12"));

        /**
         * After clicking make sure it is inside the first panelBox11
         */
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form1:subview1:commandButton12");
        page = button.click();
        assertTrue(page.asXml().indexOf("Moveable HELLO text") > page.asXml().indexOf("form1:subview1:panelBox12"));
        assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:subview1:commandButton12"));

        /**
         * And now move it to the first panel box.
         */
        button = (HtmlSubmitInput) page.getElementById("form1:commandButton11");
        page = button.click();
        assertTrue(page.asXml().indexOf("Moveable HELLO text") > page.asXml().indexOf("form1:panelBox11"));
        assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:commandButton11"));
    }

    @Test
    public void testMoveComponent3() throws Exception {
        /*
         * Make sure the Moveable HELLO is before the first panelBox11
         */
        HtmlPage page = webClient.getPage(webUrl + "faces/moveComponent.xhtml");
        assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:subview1:panelBox12"));

        for (int i = 0; i < 10; i++) {
            /**
             * After clicking make sure it is inside the first panelBox11
             */
            HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form1:subview1:commandButton12");
            page = button.click();
            assertTrue(page.asXml().indexOf("Moveable HELLO text") > page.asXml().indexOf("form1:subview1:panelBox12"));
            assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:subview1:commandButton12"));

            /**
             * And now move it to the first panel box.
             */
            button = (HtmlSubmitInput) page.getElementById("form1:commandButton11");
            page = button.click();
            assertTrue(page.asXml().indexOf("Moveable HELLO text") > page.asXml().indexOf("form1:panelBox11"));
            assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:commandButton11"));

            /**
             * And now move it to the third panel box.
             */
            button = (HtmlSubmitInput) page.getElementById("form1:subview2:commandButton13");
            page = button.click();
            assertTrue(page.asXml().indexOf("Moveable HELLO text") > page.asXml().indexOf("form1:subview2:panelBox11"));
            assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:subview2:commandButton13"));

            /**
             * And now move it to the fourth panel box.
             */
            button = (HtmlSubmitInput) page.getElementById("form1:subview2:subview2b:commandButton14");
            page = button.click();
            assertTrue(page.asXml().indexOf("Moveable HELLO text") > page.asXml().indexOf("form1:subview2:subview2b:panelBox4"));
            assertTrue(page.asXml().indexOf("Moveable HELLO text") < page.asXml().indexOf("form1:subview2:subview2b:commandButton14"));
        }
    }
    
    @Test
    public void testToggle1() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/toggle.xhtml");
        for(int i=0; i<10; i++) {
            String text = page.asXml();
            assertTrue(text.indexOf("Manually added child2") < text.indexOf("Manually added child1"));
            HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form1:button");
            page = button.click();
            text = page.asXml();
            assertTrue(text.indexOf("Manually added child1") < text.indexOf("Manually added child2"));
            button = (HtmlSubmitInput) page.getElementById("form1:button");
            page = button.click();
        }
    }
}
