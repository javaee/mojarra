/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlHorizontalRule;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AjaxInsertDeleteTestCase extends HtmlUnitFacesTestCase {

    public AjaxInsertDeleteTestCase(String name) {
        super(name);
    }

    /*
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /*
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(AjaxInsertDeleteTestCase.class));
    }


    /*
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    public void testInsertDelete() throws Exception {

        HtmlPage page = getPage("/faces/ajax/ajaxInsertDelete.xhtml");

        assertNull(getBeforeHeading(page));
        assertNull(getAfterHeading(page));

        HtmlSubmitInput beforeButton = getBeforeButton(page);
        assertNotNull(beforeButton);
        page = beforeButton.click();

        HtmlHeading2 beforeHeading = getBeforeHeading(page);
        assertNotNull(beforeHeading);
        assertTrue(beforeHeading.getNextSibling() instanceof HtmlHorizontalRule);
        assertNotNull(page.getElementById("trbefore"));

        HtmlSubmitInput afterButton = getAfterButton(page);
        assertNotNull(afterButton);
        page = afterButton.click();

        HtmlHeading2 afterHeading = getAfterHeading(page);
        assertNotNull(afterHeading);
        assertTrue(afterHeading.getPreviousSibling() instanceof HtmlHorizontalRule);
        assertNotNull(page.getElementById("trafter"));

        HtmlSubmitInput removeBefore = getRemoveBeforeButton(page);
        assertNotNull(removeBefore);
        page = removeBefore.click();

        assertNull(getBeforeHeading(page));
        assertNotNull(getAfterHeading(page));

        assertNull(page.getElementById("trbefore"));

        HtmlSubmitInput removeAfter = getRemoveAfterButton(page);
        assertNotNull(removeAfter);
        page = removeAfter.click();

        assertNull(page.getElementById("trafter"));

        assertNull(getBeforeHeading(page));
        assertNull(getAfterHeading(page));

    }


    // --------------------------------------------------------  Private Methods


    private HtmlSubmitInput getBeforeButton(HtmlPage page) {

        return (HtmlSubmitInput) getInputContainingGivenId(page, "form1:before");

    }


    private HtmlSubmitInput getAfterButton(HtmlPage page) {

        return (HtmlSubmitInput) getInputContainingGivenId(page, "form1:after");

    }


    private HtmlSubmitInput getRemoveBeforeButton(HtmlPage page) {

        return (HtmlSubmitInput) getInputContainingGivenId(page, "form1:removeBefore");

    }


    private HtmlSubmitInput getRemoveAfterButton(HtmlPage page) {

        return (HtmlSubmitInput) getInputContainingGivenId(page, "form1:removeAfter");

    }


    private HtmlHeading2 getBeforeHeading(HtmlPage page) {

        return (HtmlHeading2) page.getElementById("h2before");

    }


    private HtmlHeading2 getAfterHeading(HtmlPage page) {

        return (HtmlHeading2) page.getElementById("h2after");

    }
}
