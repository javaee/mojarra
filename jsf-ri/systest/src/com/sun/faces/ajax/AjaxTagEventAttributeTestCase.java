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

public class AjaxTagEventAttributeTestCase extends AbstractTestCase {

    public AjaxTagEventAttributeTestCase(String name) {
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
        return (new TestSuite(AjaxTagEventAttributeTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testAjaxTagEventAttribute() throws Exception {
        getPage("/faces/ajax/ajaxTagEventAttribute.xhtml");
        System.out.println("Start ajax tag event attribute test");

        // Check initial values
        checkTrue("out1","0");
        checkTrue("out2","1");
        checkTrue("out3","");
        checkTrue("checkedvalue1","false");
        checkTrue("checkedvalue2","false");

        // Press Count
        HtmlSubmitInput button = (HtmlSubmitInput) lastpage.getHtmlElementById("button");
        lastpage = (HtmlPage) button.click();

        checkTrue("out1","0");
        checkTrue("out2","0");

        HtmlInput input = (HtmlInput) lastpage.getHtmlElementById("in1");
        lastpage = (HtmlPage) input.setValueAttribute("test");

        checkTrue("out3","test");


        // Check ajax checkbox
        HtmlCheckBoxInput checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox1"));
        lastpage = (HtmlPage)checked.click();

        checkTrue("checkedvalue1","true");

        // Check ajax checkbox
        checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox2"));
        lastpage = (HtmlPage)checked.click();

        checkTrue("checkedvalue2","true");



        // Check that all ajax requests didn't result in a reload
        checkTrue("out4","2");

    }

}
