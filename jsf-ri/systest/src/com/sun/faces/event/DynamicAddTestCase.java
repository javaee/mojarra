/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.event;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.*;
import java.util.regex.Pattern;


/**
 * Unit tests for Composite Components.
 */
public class DynamicAddTestCase extends AbstractTestCase {


    public DynamicAddTestCase() {
        this("VerifyBuildBeforeRestoreTestCase");
    }

    public DynamicAddTestCase(String name) {
        super(name);
    }


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
        return (new TestSuite(DynamicAddTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }
    

    // -------------------------------------------------------------- Test Cases

    public void testVerifyDynamicAdd() throws Exception {

        HtmlPage page = getPage("/faces/dynamicComponents.xhtml");
        String text = page.asXml();
        assertTrue(Pattern.matches("(?s).*\\s*<ul>\\s*\\s*<p>\\s*Dynamic Component dynamic1\\s*<ul>\\s*\\s*<p>\\s*Dynamic Component dynamic2\\s*<ul>\\s*\\s*<p>\\s*Dynamic Component dynamic3\\s*<ul>\\s*\\s*<p>\\s*Dynamic Component dynamic4\\s*<ul>\\s*\\s*<p>\\s*Dynamic Component dynamic5\\s*</p>\\s*\\s*</ul>\\s*\\s*</p>\\s*\\s*</ul>\\s*\\s*</p>\\s*\\s*</ul>\\s*\\s*</p>\\s*\\s*</ul>\\s*\\s*</p>\\s*\\s*</ul>\\s*.*",
                text));

    }

    public void testDynamicAddHandlesViewIdChanges() throws Exception {

        HtmlPage page = getPage("/faces/dynamicComponents00.xhtml");
        String text;
        HtmlSubmitInput button = (HtmlSubmitInput)
                this.getInputContainingGivenId(page, "next");
        page = button.click();
        button = (HtmlSubmitInput)
                this.getInputContainingGivenId(page, "thisAgain");
        page = button.click();
        button = (HtmlSubmitInput)
                this.getInputContainingGivenId(page, "thisAgain");
        page = button.click();
        text = page.asXml();
        assertTrue(text.contains("Dynamic Component dynamic1"));
        button = (HtmlSubmitInput)
                this.getInputContainingGivenId(page, "next");
        page = button.click();
        text = page.asXml();
        assertTrue(page.asXml().contains("no dynamic component"));



    }

    public void testEventsPublishedAfterAddBeforeRender() throws Exception {
        HtmlPage page = getPage("/faces/publishEvents.xhtml");
        String text = page.asText();
        assertTrue(text.contains("componentWithListener : Event: javax.faces.event.PostAddToViewEvent"));
        assertTrue(text.contains("componentWithListener : Event: javax.faces.event.PreRenderViewEvent"));
        assertTrue(!text.contains("componentWithNoListener"));
    }

    
}
