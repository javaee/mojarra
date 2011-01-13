/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;

public class AjaxTagEventWrappingTestCase extends AbstractTestCase {

    public AjaxTagEventWrappingTestCase(String name) {
        super(name);
    }

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /*
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(AjaxTagEventWrappingTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testAjaxTagEventWrapping() throws Exception {
        getPage("/faces/ajax/ajaxTagEventWrapping.xhtml");
        System.out.println("Start ajax tag event wrapping test");

        // Check initial values
        checkTrue("out1","0");
        checkTrue("say","init");
        checkTrue("paramOut","");
        checkTrue("out2","1");

        // Press Count
        HtmlSubmitInput button = (HtmlSubmitInput) lastpage.getHtmlElementById("button1");
        lastpage = (HtmlPage) button.click();

        checkTrue("out1","2");
        checkTrue("out2","1");

        // Press Say
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button2");
        lastpage = (HtmlPage) button.click();

        checkTrue("say","1");
        checkTrue("out1","2");
        checkTrue("out2","1");

        // Press Count and Say
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button3");
        lastpage = (HtmlPage) button.click();

        checkTrue("say","2");
        checkTrue("out1","3");
        checkTrue("out2","1");

        // Press Param
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button4");
        lastpage = (HtmlPage) button.click();

        checkTrue("say","init");
        checkTrue("out1","4");
        checkTrue("out2","5");
        checkTrue("paramOut","testval");

        // Reset Page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reset");
        lastpage = (HtmlPage) button.click();
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reload");
        lastpage = (HtmlPage) button.click();

        // Check initial values
        checkTrue("out1","0");
        checkTrue("say","init");
        checkTrue("paramOut","");
        checkTrue("out2","1");

        // Press Count and Param
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button5");
        lastpage = (HtmlPage) button.click();

        checkTrue("out1","2");
        checkTrue("say","init");
        checkTrue("paramOut","testval");
        checkTrue("out2","1");

        // Reset Page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reset");
        lastpage = (HtmlPage) button.click();
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reload");
        lastpage = (HtmlPage) button.click();

        // Check initial values
        checkTrue("out1","0");
        checkTrue("say","init");
        checkTrue("paramOut","");
        checkTrue("out2","1");

        // Press Count and Say and Param
        /*  Test is faulty - commenting out - race to see if say is actually set

        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button6");
        lastpage = (HtmlPage) button.click();

        checkTrue("out1","2");
        checkTrue("say","1");
        checkTrue("paramOut","testval");
        checkTrue("out2","1");
        */
        // leaving out button 7

        // Reset Page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reset");
        lastpage = (HtmlPage) button.click();
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reload");
        lastpage = (HtmlPage) button.click();

        // Check initial values
        checkTrue("out1","0");
        checkTrue("say","init");
        checkTrue("paramOut","");
        checkTrue("out2","1");


        // Check ajax checkbox
        HtmlCheckBoxInput checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox1"));
        lastpage = (HtmlPage)checked.click();

        System.out.println(getText("checkedvalue1"));
        checkTrue("checkedvalue1","true");
        checkTrue("out2","1");

        // Check ajax + userwrap checkbox
        checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox2"));
        lastpage = (HtmlPage)checked.click();

        checkTrue("checkedvalue2","true");
        checkTrue("say","1");
        checkTrue("out2","1");

        // Check user onchange checkbox
        checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox3"));
        lastpage = (HtmlPage)checked.click();

        checkTrue("checkedvalue3","false");
        checkTrue("say","2");
        checkTrue("out2","1");

    }

}
