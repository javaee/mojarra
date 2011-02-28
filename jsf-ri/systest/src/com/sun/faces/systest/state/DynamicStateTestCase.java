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

package com.sun.faces.systest.state;

import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import junit.framework.Test;
import junit.framework.TestSuite;

public class DynamicStateTestCase extends HtmlUnitFacesTestCase {

    public DynamicStateTestCase(String name) {
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
        return (new TestSuite(DynamicStateTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testDynamicDeletionPrefix() throws Exception {
        doTestDynamicDeletion("/faces/state/dynamicDeletion.xhtml");
    }
    
    public void testDynamicDeletionExtension() throws Exception {
        doTestDynamicDeletion("/state/dynamicDeletion.faces");
    }
    
    public void testDynamicAdditionPrefix() throws Exception {
        doTestDynamicAddition("/faces/state/dynamicAddition.xhtml");
    }
    
    public void testDynamicAdditionExtension() throws Exception {
        doTestDynamicAddition("/state/dynamicAddition.faces");
    }

    /*
     * Added for issue 1183.
     */
    public void testNestedComponentAddition() throws Exception {

        HtmlPage page = getPage("/faces/state/dynamicAddition2.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:render");
        page = submit.click();
        HtmlTextInput input = (HtmlTextInput)
              getInputContainingGivenId(page, "form:textInput");
        assertNotNull(input);
        assertEquals("default value", input.getValueAttribute());
        input.setValueAttribute("new value");
        submit = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:render");
        page = submit.click();
        input = (HtmlTextInput)
              getInputContainingGivenId(page, "form:textInput");
        assertNotNull(input);
        assertEquals("new value", input.getValueAttribute());

        // ensure events are fired properly when adding tree deltas
        // to the view
        submit = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:submit");
        page = submit.click();
        input = (HtmlTextInput)
              getInputContainingGivenId(page, "form:textInput");
        assertNotNull(input);

        // once more for good measure
        submit = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:submit");
        page = submit.click();
        input = (HtmlTextInput)
              getInputContainingGivenId(page, "form:textInput");
        assertNotNull(input);

    }


    /**
     * Added for issue 1185.
     */
    public void testDeleteAddSameAction() throws Exception {

        HtmlPage page = getPage("/faces/state/dynamicAdditionDeletion.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:render");
        page = submit.click();

        // first click removes children from the panel (should be empty)
        // and adds a new button
        assertTrue(page.asText().contains("dynamically added button"));

        for (int i = 0; i < 5; i++) {
            // repeated clicks will remove the single child and add a new button
            // back.
            submit = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:render");
            page = submit.click();

            assertTrue(page.asText().contains("dynamically added button"));
        }

    }


    /**
     * Added for issue 1553.
     */
    public void testDynamicAdditionTransietSubTree() throws Exception {

        // any exception thrown here will fail the test
        client.setThrowExceptionOnFailingStatusCode(true);
        getPage("/faces/state/dynamicAdditionTransientSubTree.xhtml");

    }


    // --------------------------------------------------------- Private Methods


    private void doTestDynamicDeletion(String viewId) throws Exception {
        HtmlPage page = getPage(viewId);
        HtmlTextInput textField = (HtmlTextInput)
                getInputContainingGivenId(page, "textField");
        textField.setValueAttribute("some text");
        HtmlSubmitInput button = (HtmlSubmitInput)
                getInputContainingGivenId(page, "reload");
        try {
            page = (HtmlPage) button.click();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(-1 == page.asText().indexOf("cbutton should not be found"));
    }


    private void doTestDynamicAddition(String viewId) throws Exception {
        HtmlPage page = getPage(viewId);
        HtmlTextInput textField = (HtmlTextInput)
                getInputContainingGivenId(page, "textField");
        textField.setValueAttribute("some text");
        HtmlSubmitInput button = (HtmlSubmitInput)
                getInputContainingGivenId(page, "reload");
        try {
            page = (HtmlPage) button.click();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(-1 == page.asText().indexOf("cbutton should be found"));
    }

} // end of class PathTestCase
