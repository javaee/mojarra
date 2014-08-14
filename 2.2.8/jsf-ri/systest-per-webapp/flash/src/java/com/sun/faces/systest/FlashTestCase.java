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

package com.sun.faces.systest;


import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;




/**
 * <p>Make sure that an application that replaces the ApplicationFactory
 * but uses the decorator pattern to allow the existing ApplicationImpl
 * to do the bulk of the requests works.</p>
 */

public class FlashTestCase extends HtmlUnitFacesTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public FlashTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(FlashTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables



    // ------------------------------------------------- Individual Test Methods

    /**
     *
     * <p>Verify that the bean is successfully resolved</p>
     */

    public void testFlash() throws Exception {
        // Get the first page
        HtmlPage page = getPage("/faces/index.xhtml");
        String pageText = page.asXml();
        // (?s) is an "embedded flag expression" for the "DOTALL" operator.
        // It says, "let . match any character including line terminators."
        // Because page.asXml() returns a big string with lots of \r\n chars
        // in it, we need (?s).
        // the page contains a table tag with a frame attribute whose value is hsides.

        // the page contains the following span, with the following id, with no contents
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"fooValueId\">\\s*</span>.*"));
        
        // Click the reload button
        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();
        pageText = page.asXml();
        // verify that fooValue is there, indicating that it's been stored in the flash
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"fooValueId\">\\s*fooValue\\s*</span>.*"));

        // Get the first page, again
        page = getPage("/faces/index.xhtml");
        pageText = page.asXml();

        // the page contains the following span, with the following id, with no contents
        // meaning the flash has no value for foo
        //assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"fooValueId\">\\s*</span>.*"));


        
        // fill in "addMessage" in the textBox
        HtmlTextInput text = (HtmlTextInput) page.getHtmlElementById("inputText");
        text.setValueAttribute("addMessage");
        
        // go to the next page
        button = (HtmlSubmitInput) page.getHtmlElementById("next");
        page = (HtmlPage) button.click();
        pageText = page.asXml();
        // See that it has fooValue
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"flash2FooValueId\">\\s*fooValue\\s*</span>.*"));
        // See that it has barValue
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"flash2BarValueId\">\\s*barValue\\s*</span>.*"));
        // See that it has the message
        assertTrue(-1 != pageText.indexOf("test that this persists across the redirect"));
        
        // click the reload button
        button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();
        pageText = page.asXml();        
        
        // See that it doesn't have the message
        assertTrue(-1 == pageText.indexOf("test that this persists across the redirect"));
        
        // Click the back button
        button = (HtmlSubmitInput) page.getHtmlElementById("back");
        page = (HtmlPage) button.click();
        pageText = page.asXml();
        
        // Click the next button
        button = (HtmlSubmitInput) page.getHtmlElementById("next");
        page = (HtmlPage) button.click();
        pageText = page.asXml();
        
        // See that the page does not have the message
        assertTrue(-1 == pageText.indexOf("test that this persists across the redirect"));
        
        // Click the next button
        button = (HtmlSubmitInput) page.getHtmlElementById("next");
        page = (HtmlPage) button.click();
        pageText = page.asXml();
        
        // See that it has banzai
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"flash3NowValueId\">\\s*banzai\\s*</span>.*"));
        
        // Click the next button
        button = (HtmlSubmitInput) page.getHtmlElementById("next");
        page = (HtmlPage) button.click();
        pageText = page.asXml();
        
        // See that it still has banzai
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"flash4BuckarooValueId\">\\s*banzai\\s*</span>.*"));

        // click the link http://localhost:${container.port}/jsf-flash/flash5.jsf?id=1
        HtmlAnchor link = (HtmlAnchor) page.getElementById("link");
        page = link.click();

        // on flash5
        link = (HtmlAnchor) page.getElementById("link");
        page = link.click(); // clicks http://localhost:${container.port}/jsf-flash/flash6.jsf

        assertTrue(page.asText().contains("Value is 1."));

        // click the link on the next page
        link = (HtmlAnchor) page.getElementById("link"); // http://localhost:${container.port}/jsf-flash/flash7.jsf
        page = link.click();

        assertTrue(page.asText().contains("Value is 1."));

        // click the link on the same page
        link = (HtmlAnchor) page.getElementById("link"); // http://localhost:${container.port}/jsf-flash/flash7.jsf
        page = link.click();

        assertTrue(page.asText().contains("Value is 1."));

        // click the link on the same page
        link = (HtmlAnchor) page.getElementById("link"); // http://localhost:${container.port}/jsf-flash/flash7.jsf
        page = link.click();

        assertTrue(page.asText().contains("Value is 1."));

        link = (HtmlAnchor) page.getElementById("link2"); // http://localhost:${container.port}/jsf-flash/flash8.jsf
        page = link.click();

        assertTrue(page.asText().contains("Value is 1."));

        link = (HtmlAnchor) page.getElementById("link"); // http://localhost:${container.port}/jsf-flash/flash8.jsf
        page = link.click();

        // it went away because there was no keep
        assertTrue(page.asText().contains("Value is ."));

        page = getPage("/faces/flash9.xhtml"); // http://localhost:${container.port}/jsf-flash/flash9.jsf

        text = (HtmlTextInput) page.getHtmlElementById("valueA");
        text.setValueAttribute("a value");

        text = (HtmlTextInput) page.getHtmlElementById("valueB");
        text.setValueAttribute("b value");

        text = (HtmlTextInput) page.getHtmlElementById("valueC");
        text.setValueAttribute("c value");

        button = (HtmlSubmitInput) page.getHtmlElementById("keep");
        page = (HtmlPage) button.click();  // http://localhost:${container.port}/jsf-flash/flash11.jsf

        pageText = page.asText();

        assertTrue(pageText.contains("valueA: a value"));
        assertTrue(pageText.contains("valueB: b value"));
        assertTrue(pageText.contains("valueC: c value"));

        link = (HtmlAnchor) page.getElementById("flash9"); // http://localhost:${container.port}/jsf-flash/flash9.jsf
        page = link.click();

        text = (HtmlTextInput) page.getHtmlElementById("valueA");
        assertEquals(text.getValueAttribute(), "a value");

        text = (HtmlTextInput) page.getHtmlElementById("valueB");
        assertEquals(text.getValueAttribute(), "b value");

        text = (HtmlTextInput) page.getHtmlElementById("valueC");
        assertEquals(text.getValueAttribute(), "c value");

        page = getPage("/faces/flash9.xhtml"); // http://localhost:${container.port}/jsf-flash/flash9.jsf
        text = (HtmlTextInput) page.getHtmlElementById("valueA");
        assertEquals(text.getValueAttribute(), "");

        text = (HtmlTextInput) page.getHtmlElementById("valueB");
        assertEquals(text.getValueAttribute(), "");

        text = (HtmlTextInput) page.getHtmlElementById("valueC");
        assertEquals(text.getValueAttribute(), "");

        text = (HtmlTextInput) page.getHtmlElementById("valueA");
        text.setValueAttribute("A value");

        text = (HtmlTextInput) page.getHtmlElementById("valueB");
        text.setValueAttribute("B value");

        text = (HtmlTextInput) page.getHtmlElementById("valueC");
        text.setValueAttribute("C value");

        button = (HtmlSubmitInput) page.getHtmlElementById("nokeep");
        page = (HtmlPage) button.click();  // http://localhost:${container.port}/jsf-flash/flash10.jsf

        pageText = page.asText();

        assertTrue(pageText.contains("valueA: A value"));
        assertTrue(pageText.contains("valueB: B value"));
        assertTrue(pageText.contains("valueC: C value"));

        button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();  // http://localhost:${container.port}/jsf-flash/flash10.jsf

        pageText = page.asText();

        assertTrue(!pageText.contains("valueA: A value"));
        assertTrue(!pageText.contains("valueB: B value"));
        assertTrue(!pageText.contains("valueC: C value"));

        // content from Sebastian Hennebrueder
        page = getPage("/faces/flash12.xhtml");
        button = (HtmlSubmitInput) page.getHtmlElementById("start");
        page = (HtmlPage) button.click();  // http://localhost:${container.port}/jsf-flash/flash10.jsf

        pageText = page.asText();
        assertTrue(pageText.contains("4711"));

        page = (HtmlPage) page.refresh();

        pageText = page.asText();
        assertTrue(!pageText.contains("4711"));

        // Test for JAVASERVERFACES-2087
        page = getPage("/faces/flash13.xhtml");
        button = (HtmlSubmitInput) page.getHtmlElementById("flashbtn");
        page = (HtmlPage) button.click();  // http://localhost:${container.port}/jsf-flash/flash13.jsf
        
        pageText = page.asText();
        assertTrue(pageText.contains("read strobist"));

        page = getPage("/faces/flash13.xhtml");
        pageText = page.asText();
        assertTrue(!pageText.contains("read strobist"));

        page = getPage("/faces/flash13.xhtml");
        pageText = page.asText();
        assertTrue(!pageText.contains("read strobist"));

        // Only execute this assertion 
        // in non-cluster scenario
        if (getInstanceNumbers().size() < 2) {
            page = getPage("/faces/flash14.xhtml");
            pageText = page.asText();
            assertTrue(pageText.matches("(?s).*\\[received javax.faces.event.PreClearFlashEvent source:\\{\\}\\].*"));
        }
        

    }

}
