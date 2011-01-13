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

import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AjaxSelectTestCase extends AbstractTestCase {

    public AjaxSelectTestCase(String name) {
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
        return (new TestSuite(AjaxTagWrappingTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    /*
       Test each component to see that it behaves correctly when used with an Ajax tag
     */
    public void testAjaxSelect() throws Exception {
        getPage("/faces/ajax/ajaxSelect.xhtml");

        checkTrue("out", "Pending");

        ClickableElement click = lastpage.getHtmlElementById("form:s1rad:0");

        lastpage = click.click();

        checkTrue("out", "radio-1");

        HtmlSelect select = lastpage.getHtmlElementById("form:s1menu");

        lastpage = (HtmlPage) select.setSelectedAttribute("2",true);

        checkTrue("out", "menu-2");

        select = lastpage.getHtmlElementById("form:s1list");

        lastpage = (HtmlPage) select.setSelectedAttribute("2",true);

        checkTrue("out", "list-2");

        select = lastpage.getHtmlElementById("form:smlist");

        lastpage = (HtmlPage) select.setSelectedAttribute("2",true);

        checkTrue("out", "mlist-2");

        click = lastpage.getHtmlElementById("form:smcheck:0");

        lastpage = click.click();

        checkTrue("out", "mcheck-1");

        click = lastpage.getHtmlElementById("form:bool");

        lastpage = click.click();

        checkTrue("out", "PASSED");


        // Now, reload everything and do it again.
        // This tests for bug 1339
        click = lastpage.getHtmlElementById("form:button");

        click.click();

        checkTrue("out", "Pending");

        click = lastpage.getHtmlElementById("form:s1rad:0");

        lastpage = click.click();

        checkTrue("out", "radio-1");

        select = lastpage.getHtmlElementById("form:s1menu");

        lastpage = (HtmlPage) select.setSelectedAttribute("2",true);

        checkTrue("out", "menu-2");

        select = lastpage.getHtmlElementById("form:s1list");

        lastpage = (HtmlPage) select.setSelectedAttribute("2",true);

        checkTrue("out", "list-2");

        select = lastpage.getHtmlElementById("form:smlist");

        lastpage = (HtmlPage) select.setSelectedAttribute("2",true);

        checkTrue("out", "mlist-2");

        click = lastpage.getHtmlElementById("form:smcheck:0");

        lastpage = click.click();

        checkTrue("out", "mcheck-1");

        click = lastpage.getHtmlElementById("form:bool");

        lastpage = click.click();

        checkTrue("out", "PASSED");

    }
}