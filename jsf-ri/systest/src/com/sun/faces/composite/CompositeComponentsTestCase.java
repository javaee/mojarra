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

package com.sun.faces.composite;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for Composite Components.
 */
public class CompositeComponentsTestCase extends AbstractTestCase {


    public CompositeComponentsTestCase() {
        this("CompositeComponentsTestCase");
    }

    public CompositeComponentsTestCase(String name) {
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
        return (new TestSuite(CompositeComponentsTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }
    

    // -------------------------------------------------------------- Test Cases

    /**
     * <p>
     *  Maps ActionListener to commandButton within composite/actionSource1.xhtml using
     *   only the name attribute.
     * </p>
     */
    public void testActionSource1() throws Exception {

        HtmlPage page = getPage("/faces/composite/actionsource.xhtml");
        validateActionMessagePresent(page,
                                     "form:actionsource1:command");

    }


    /**
     * <p>
     *   Maps ActionListener to commandButton within composite/actionSource2.xhtml using
     *   name and target attributes.
     * </p>
     */
    public void testActionSource2() throws Exception {

        HtmlPage page = getPage("/faces/composite/actionsource.xhtml");
        validateActionMessagePresent(page,
                                     "form:actionsource2:ac2");

    }


    /**
     * <p>
     *  Maps ActionListener to a commandButton within a composite/actionSource1.xhtml
     *   which is nested within composite/actionSource3.xhtml. Using the same ID
     *   in the nesting.
     * </p>
     */
    public void testActionSource3() throws Exception {

        HtmlPage page = getPage("/faces/composite/actionsource.xhtml");
        validateActionMessagePresent(page,
                                     "form:actionsource3:command:command");
        
    }


    /**
     * <p>
     *  Ensure actionListeners are properly re-targeted when the
     *  target of the actionListener is nested within another naming
     *  container.  Note that the value of the 'for' attribute doesn't
     *  mimic the NamingContainer hierarchy, that's handled by the
     *  'targets' attribute within the composite:implementation section
     *  of actionSource4.xhtml.
     * </p>
     */
    public void testActionSource4() throws Exception {

        HtmlPage page = getPage("/faces/composite/actionsource.xhtml");
        validateActionMessagePresent(page,
                                     "form:actionsource4:naming:command");
        
    }


    /**
     * <p>
     *  Maps Validator to inputText within composite/validator1.xhtml using
     *   only the name attribute.
     * </p>
     */
    public void testValidator1() throws Exception {

        HtmlPage page = getPage("/faces/composite/attachedvalidator.xhtml");
        validateValidatorMessagePresent(page,
                                        "form:s1",
                                        "form:validator1:input");

    }


    /**
     * <p>
     *   Maps Validator to inputText within composite/validator2.xhtml using
     *   name and target attributes.
     * </p>
     */
    public void testValidator2() throws Exception {

        HtmlPage page = getPage("/faces/composite/attachedvalidator.xhtml");
        validateValidatorMessagePresent(page,
                                        "form2:s2",
                                        "form2:validator2:it2");

    }


    /**
     * <p>
     *  Maps Validator to a inputText within a composite/validator1.xhtml
     *  which is nested within composite/validator3.xhtml. Using the same ID
     *  in the nesting.
     * </p>
     */
    public void testValidator3() throws Exception {

        HtmlPage page = getPage("/faces/composite/attachedvalidator.xhtml");
        validateValidatorMessagePresent(page,
                                        "form3:s3",
                                        "form3:validator3:input:input");

    }


    /**
     * <p>
     *  Ensure validators are properly re-targeted when the
     *  target of the validator is nested within another naming
     *  container.  Note that the value of the 'for' attribute doesn't
     *  mimic the NamingContainer hierarchy, that's handled by the
     *  'targets' attribute within the composite:implementation section
     *  of validator4.xhtml.
     * </p>
     */
    public void testValidator4() throws Exception {

        HtmlPage page = getPage("/faces/composite/attachedvalidator.xhtml");
        validateValidatorMessagePresent(page,
                                        "form4:s4",
                                        "form4:validator4:naming:input");

    }

    // --------------------------------------------------------- Private Methods


    private void validateActionMessagePresent(HtmlPage page, String commandId)
    throws Exception {

        page = pushButton(page, commandId);
        validateMessage(page, "Action Invoked", commandId);

    }


    private void validateValidatorMessagePresent(HtmlPage page, String commandId, String inputId)
    throws Exception {

        page = pushButton(page, commandId);
        validateMessage(page, "Validator Invoked", inputId);

    }


    private HtmlPage pushButton(HtmlPage page, String commandId)
    throws Exception {

        HtmlSubmitInput input = (HtmlSubmitInput)
              getInputContainingGivenId(page, commandId);
        assertNotNull(input);
        return (HtmlPage) input.click();

    }


    private void validateMessage(HtmlPage page,
                                 String messagePrefix,
                                 String messageSuffix) {

        List<HtmlUnorderedList> list = new ArrayList<HtmlUnorderedList>();
        getAllElementsOfGivenClass(page, list, HtmlUnorderedList.class);
        HtmlUnorderedList ulist = list.get(0);
        assertEquals("messages", ulist.getId());
        int count = 0;
        String message = (messagePrefix + " : " + messageSuffix);
        for (HtmlElement e : ulist.getAllHtmlChildElements()) {
            if (count > 1) {
                fail("Expected only one message to be displayed");
            }
            count++;
            assertTrue(e instanceof HtmlListItem);
            assertEquals(message, message, e.asText());
        }

        if (list.size() == 2) {
            ulist = list.get(1);
            for (HtmlElement e : ulist.getAllHtmlChildElements()) {
                fail("Messages have been redisplayed");
            }
        }


    }
}
