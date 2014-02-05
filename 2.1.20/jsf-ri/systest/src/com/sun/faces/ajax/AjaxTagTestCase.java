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

package com.sun.faces.ajax;

import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;

public class AjaxTagTestCase extends HtmlUnitFacesTestCase {

    public AjaxTagTestCase(String name) {
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
        return (new TestSuite(AjaxTagTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testAjaxTagCount() throws Exception {
        getPage("/faces/ajax/ajaxTagCount.xhtml");
        System.out.println("Start ajax count test");
        // First we'll check the first page was output correctly
        checkTrue("countForm:out1","0");
        checkTrue("out2","1");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("countForm:button1");
        lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds
        checkTrue("countForm:out1","2");

        // Check that the request did NOT update the rest of the page.
        checkTrue("out2","1");
    }

    public void testAjaxTagMulti() throws Exception {
        getPage("/faces/ajax/ajaxTagMulti.xhtml");
        System.out.println("Start ajax tag multi test");
        // First we'll check the first page was output correctly
        checkTrue("countForm:out1","0");
        checkTrue("countForm:out2","1");
        checkTrue("countForm:out3","2");
        checkTrue("outside","3");

        // Press Count 1
        HtmlSubmitInput button = (HtmlSubmitInput) lastpage.getHtmlElementById("countForm:button1");
        lastpage = (HtmlPage) button.click();

        checkTrue("countForm:out1","4");

        // Press Count 2
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("countForm:button2");
        lastpage = (HtmlPage) button.click();

        checkTrue("countForm:out1","5");
        checkTrue("countForm:out2","6");

        // Press Count all 3
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("countForm:button3");
        lastpage = (HtmlPage) button.click();

        checkTrue("countForm:out1","7");
        checkTrue("countForm:out2","8");
        checkTrue("countForm:out3","9");

        // Check that the request did NOT update the rest of the page.
        checkTrue("outside","3");

        // Press Count form
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("countForm:button4");
        lastpage = (HtmlPage) button.click();

        checkTrue("countForm:out1","10");
        checkTrue("countForm:out2","11");
        checkTrue("countForm:out3","12");

        // Check that the request did NOT update the rest of the page.
        checkTrue("outside","3");

        // Press Refresh form
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("countForm:reset");
        lastpage = (HtmlPage) button.click();

        checkTrue("countForm:out1","0");
        checkTrue("countForm:out2","1");
        checkTrue("countForm:out3","2");


        // Check that the request did NOT update the rest of the page.
        checkTrue("outside","3");
    }

    public void testAjaxTagDefaultsButton() throws Exception {
        System.out.println("Starting Ajax Tag Defaults Button Test");
        getPage("/faces/ajax/ajaxTagDefaultsButton.xhtml");


        String out1 = "form1:out1";
        String out2 = "form1:out2";
        String out3 = "out3";
        String reload = "form1:reload";
        String reset1 = "form1:reset1";
        String reset2 = "form1:reset2";
        String reset3 = "form1:reset3";
        String reset4 = "form1:reset4";

        // First, we'll test to make sure the initial values come out right
        checkTrue(out1,"0");
        checkTrue(out2,"1");
        checkTrue(out3,"2");

        // Reload the page
        HtmlSubmitInput button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"3");
        checkTrue(out2,"4");
        checkTrue(out3,"5");

        // Now, make the Ajax call to first reset button
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reset1);
        lastpage = (HtmlPage) button.click();

        // Check that the ajax request succeeds
        checkTrue(out1,"0");
        checkTrue(out2,"4");
        checkTrue(out3,"5");

        // Reload the page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"1");
        checkTrue(out2,"2");
        checkTrue(out3,"3");

        // Now, make the Ajax call to second reset button
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reset2);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"0");
        checkTrue(out2,"2");
        checkTrue(out3,"3");

        // Reload the page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"1");
        checkTrue(out2,"2");
        checkTrue(out3,"3");

        // Now, make the Ajax call to third reset button
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reset3);
        lastpage = (HtmlPage) button.click();

        // Check the page did *not* update
        checkTrue(out1,"1");
        checkTrue(out2,"2");
        checkTrue(out3,"3");

        // Now, Reload the page, to check that reset3 actually executed
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();
        checkTrue(out1,"0");
        checkTrue(out2,"1");
        checkTrue(out3,"2");

        // Reload the page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"3");
        checkTrue(out2,"4");
        checkTrue(out3,"5");

        // Now, make the Ajax call to fourth reset button
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reset4);
        lastpage = (HtmlPage) button.click();

        // Check the page did *not* update
        checkTrue(out1,"3");
        checkTrue(out2,"4");
        checkTrue(out3,"5");

        // Now, Reload the page, to check that reset4 actually executed
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();
        checkTrue(out1,"0");
        checkTrue(out2,"1");
        checkTrue(out3,"2");
    }

    public void testAjaxTagDefaultsButtonNoPrepend() throws Exception {
        System.out.println("Starting Tag Defaults Button No Prepend Test");
        getPage("/faces/ajax/ajaxTagDefaultsButtonNoPrepend.xhtml");


        String out1 = "out1";
        String out2 = "out2";
        String out3 = "out3";
        String reload = "reload";
        String reset1 = "reset1";
        String reset2 = "reset2";
        String reset3 = "reset3";
        String reset4 = "reset4";


        // First, we'll test to make sure the initial values come out right
        checkTrue(out1,"0");
        checkTrue(out2,"1");
        checkTrue(out3,"2");

        // Reload the page
        HtmlSubmitInput button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"3");
        checkTrue(out2,"4");
        checkTrue(out3,"5");

        // Now, make the Ajax call to first reset button
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reset1);
        lastpage = (HtmlPage) button.click();

        // Check that the ajax request succeeds
        checkTrue(out1,"0");
        checkTrue(out2,"4");
        checkTrue(out3,"5");

        // Reload the page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"1");
        checkTrue(out2,"2");
        checkTrue(out3,"3");

        // Now, make the Ajax call to second reset button
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reset2);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"0");
        checkTrue(out2,"2");
        checkTrue(out3,"3");

        // Reload the page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"1");
        checkTrue(out2,"2");
        checkTrue(out3,"3");

        // Now, make the Ajax call to third reset button
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reset3);
        lastpage = (HtmlPage) button.click();

        // Check the page did *not* update
        checkTrue(out1,"1");
        checkTrue(out2,"2");
        checkTrue(out3,"3");

        // Now, Reload the page, to check that reset3 actually executed
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();
        checkTrue(out1,"0");
        checkTrue(out2,"1");
        checkTrue(out3,"2");

        // Reload the page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();

        // Check that the page updated correctly
        checkTrue(out1,"3");
        checkTrue(out2,"4");
        checkTrue(out3,"5");

        // Now, make the Ajax call to fourth reset button
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reset4);
        lastpage = (HtmlPage) button.click();

        // Check the page did *not* update
        checkTrue(out1,"3");
        checkTrue(out2,"4");
        checkTrue(out3,"5");

        // Now, Reload the page, to check that reset4 actually executed
        button = (HtmlSubmitInput) lastpage.getHtmlElementById(reload);
        lastpage = (HtmlPage) button.click();
        checkTrue(out1,"0");
        checkTrue(out2,"1");
        checkTrue(out3,"2");
    }

    public void testAjaxTagDefaultsEdit() throws Exception {
        System.out.println("Starting Tag Defaults Edit Test");
        getPage("/faces/ajax/ajaxTagDefaultsEdit.xhtml");


        String out1 = "form1:out1";
        String out2 = "form1:out2";
        String out3 = "out3";
        String echo1Out = "form1:echo1Out";
        String echo2Out = "form1:echo2Out";
        String echo3Out = "form1:echo3Out";
        String echo4Out = "form1:echo4Out";
        String edit1 = "form1:edit1";
        String edit2 = "form1:edit2";
        String edit3 = "form1:edit3";
        String edit4 = "form1:edit4";
        String refresh = "form1:refresh";

        // First, we'll test to make sure the initial values come out right
        checkTrue(out1,"echo");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");
        checkTrue(echo1Out,"");
        checkTrue(echo2Out,"");
        checkTrue(echo3Out,"");
        checkTrue(echo4Out,"");

        // Next, enter data into first field
        HtmlTextInput echo1 = ((HtmlTextInput)lastpage.getHtmlElementById(edit1));
        echo1.focus();
        echo1.type("test1");
        echo1.blur();

        // Refresh the panel to check the listener fired
        HtmlSubmitInput button = lastpage.getHtmlElementById(refresh);
        button.click();
        checkTrue(echo1Out,"test1");
        checkTrue(out1,"test1");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");

        // Next, enter data into second field
        HtmlTextInput echo2 = ((HtmlTextInput)lastpage.getHtmlElementById(edit2));
        echo2.focus();
        echo2.type("test2");
        echo2.blur();

        // Refresh the panel to check the listener fired
        button = lastpage.getHtmlElementById(refresh);
        button.click();
        checkTrue(echo2Out,"test2");
        checkTrue(out1,"test2");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");

        // Next, enter data into third field
        HtmlTextInput echo3 = ((HtmlTextInput)lastpage.getHtmlElementById(edit3));
        echo3.focus();
        echo3.type("test3");
        echo3.blur();

        // Refresh the panel to check the listener fired
        button = lastpage.getHtmlElementById(refresh);
        button.click();
        checkTrue(echo3Out,"test3");
        checkTrue(out1,"test3");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");

        // Next, enter data into the fourth field
        HtmlTextInput echo4 = ((HtmlTextInput)lastpage.getHtmlElementById(edit4));
        echo4.focus();
        echo4.type("test4");
        echo4.blur();

        // Refresh the panel to check the listener fired
        button = lastpage.getHtmlElementById(refresh);
        button.click();
        checkTrue(echo4Out,"test4");
        checkTrue(out1,"test4");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");


    }

    public void testAjaxTagDefaultsEditNoPrepend() throws Exception {
        System.out.println("Starting Tag Defaults Edit No Prepend Test");
        getPage("/faces/ajax/ajaxTagDefaultsEditNoPrepend.xhtml");

        String out1 = "out1";
        String out2 = "out2";
        String out3 = "out3";
        String echo1Out = "echo1Out";
        String echo2Out = "echo2Out";
        String echo3Out = "echo3Out";
        String echo4Out = "echo4Out";
        String edit1 = "edit1";
        String edit2 = "edit2";
        String edit3 = "edit3";
        String edit4 = "edit4";
        String refresh = "refresh";

        // First, we'll test to make sure the initial values come out right
        checkTrue(out1,"echo");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");
        checkTrue(echo1Out,"");
        checkTrue(echo2Out,"");
        checkTrue(echo3Out,"");
        checkTrue(echo4Out,"");

        // Next, enter data into first field
        HtmlTextInput echo1 = ((HtmlTextInput)lastpage.getHtmlElementById(edit1));
        echo1.focus();
        echo1.type("test1");
        echo1.blur();

        // Refresh the panel to check the listener fired
        HtmlSubmitInput button = lastpage.getHtmlElementById(refresh);
        button.click();
        checkTrue(echo1Out,"test1");
        checkTrue(out1,"test1");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");

        // Next, enter data into second field
        HtmlTextInput echo2 = ((HtmlTextInput)lastpage.getHtmlElementById(edit2));
        echo2.focus();
        echo2.type("test2");
        echo2.blur();

        // Refresh the panel to check the listener fired
        button = lastpage.getHtmlElementById(refresh);
        button.click();
        checkTrue(echo2Out,"test2");
        checkTrue(out1,"test2");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");

        // Next, enter data into third field
        HtmlTextInput echo3 = ((HtmlTextInput)lastpage.getHtmlElementById(edit3));
        echo3.focus();
        echo3.type("test3");
        echo3.blur();

        // Refresh the panel to check the listener fired
        button = lastpage.getHtmlElementById(refresh);
        button.click();
        checkTrue(echo3Out,"test3");
        checkTrue(out1,"test3");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");

        // Next, enter data into the fourth field
        HtmlTextInput echo4 = ((HtmlTextInput)lastpage.getHtmlElementById(edit4));
        echo4.focus();
        echo4.type("test4");
        echo4.blur();

        // Refresh the panel to check the listener fired
        button = lastpage.getHtmlElementById(refresh);
        button.click();
        checkTrue(echo4Out,"test4");
        checkTrue(out1,"test4");
        checkTrue(out2,"echo");
        checkTrue(out3,"echo");

    }

    public void testAjaxTagEvent() throws Exception {
        getPage("/faces/ajax/ajaxTagEvent.xhtml");
        System.out.println("Start ajax tag event test");

        // First we'll check the first page was output correctly
        checkTrue("countForm:out1","0");
        checkTrue("out2","1");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("countForm:button1");
        lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds
        checkTrue("countForm:out1","2");

        // Check that the request did NOT update the rest of the page.
        checkTrue("out2","1");

        HtmlSubmitInput error = (HtmlSubmitInput) lastpage.getHtmlElementById("countForm:error");
        lastpage = (HtmlPage) error.click();

        // Check that events were written to the page.
        String statusArea = "Name: countForm:button1 Event: begin ";
        statusArea = statusArea + "Name: countForm:button1 Event: complete " ;
        statusArea = statusArea + "Name: countForm:button1 Event: success " ;
        statusArea = statusArea + "Name: countForm:error Event: begin " ;
        statusArea = statusArea + "Name: countForm:error Event: complete " ;
        statusArea = statusArea + "Name: countForm:error Event: success " ;
        //System.out.println(statusArea);
        //System.out.println(getText("statusArea");
        checkTrue("statusArea",statusArea);
    }

    public void testAjaxTagDisabled() throws Exception {
        getPage("/faces/ajax/ajaxTagDisabled.xhtml");
        System.out.println("Start ajax tag Disabled test");

        // First we'll check the first page was output correctly
        checkTrue("countForm:out1","0");
        checkTrue("out2","1");

        // Submit the ajax request
        HtmlButtonInput button1 = (HtmlButtonInput) lastpage.getHtmlElementById("countForm:button1");
        lastpage = (HtmlPage) button1.click();

        // Check that the button does nothing
        checkTrue("countForm:out1","0");

        // Check that the request did NOT update the rest of the page.
        checkTrue("out2","1");

        // Submit the ajax request
        HtmlButtonInput button2 = (HtmlButtonInput) lastpage.getHtmlElementById("countForm:button2");
        lastpage = (HtmlPage) button2.click();

        // Check that the request succeeds
        checkTrue("countForm:out1","2");

        // Check that the request did NOT update the rest of the page.
        checkTrue("out2","1");

    }

    public void testAjaxTagKeywords() throws Exception {
        getPage("/faces/ajax/ajaxTagKeywords.xhtml");
        System.out.println("Start ajax tag Keyword test");

        checkTrue("out1","0");
        checkTrue("out2","1");
        checkTrue("button3","2");
        checkTrue("out3","3");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("button1");
        lastpage = (HtmlPage) button1.click();


        checkTrue("out1","4");
        checkTrue("out2","5");
        checkTrue("button3","6");
        checkTrue("out3","7");

        // Submit the ajax request
        HtmlSubmitInput button2 = (HtmlSubmitInput) lastpage.getHtmlElementById("button2");
        lastpage = (HtmlPage) button2.click();

        checkTrue("out1","8");
        checkTrue("out2","9");
        checkTrue("button3","10");
        checkTrue("out3","7");

        // Submit the ajax request
        HtmlSubmitInput button3 = (HtmlSubmitInput) lastpage.getHtmlElementById("button3");
        lastpage = (HtmlPage) button3.click();

        checkTrue("out1","8");
        checkTrue("out2","9");
        checkTrue("button3","11");
        checkTrue("out3","7");

        // Submit the ajax request
        HtmlSubmitInput button4 = (HtmlSubmitInput) lastpage.getHtmlElementById("button4");
        lastpage = (HtmlPage) button4.click();

        checkTrue("out1","8");
        checkTrue("out2","9");
        checkTrue("button3","11");
        checkTrue("out3","7");

        // Submit the ajax request
        HtmlSubmitInput button5 = (HtmlSubmitInput) lastpage.getHtmlElementById("button5");
        lastpage = (HtmlPage) button5.click();

        checkTrue("out1","8");
        checkTrue("out2","12");
        checkTrue("button3","11");
        checkTrue("out3","7");
        
    }

}
