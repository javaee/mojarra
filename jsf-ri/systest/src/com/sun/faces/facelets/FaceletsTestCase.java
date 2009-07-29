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

package com.sun.faces.facelets;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Test cases for Facelets functionality
 */
public class FaceletsTestCase extends AbstractTestCase {


    // --------------------------------------------------------------- Test Init


    public FaceletsTestCase() {
        this("FaceletsTestCase");
    }


    public FaceletsTestCase(String name) {
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
        return (new TestSuite(FaceletsTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    /*
     * Added for issue 917.
     */
    public void testSetPropertyActionListener1() throws Exception {

        HtmlPage page = getPage("/faces/facelets/setpropertyactionlistener1.xhtml") ;

        // verify the output is initially null
        List<HtmlSpan> output = new ArrayList<HtmlSpan>(1);
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(!output.isEmpty());
        HtmlSpan span = output.get(0);
        assertTrue("Expected: 'Current Name: ', Received: '"+span.asText()+"'","Current Name:".equals(span.asText()));

        // click the commandLink with a nested setPropertyActionListener
        // to cause the name attribute in the session to be populated.
        HtmlElement clink = page.getHtmlElementById("form:s1");
        assertNotNull(clink);
        page = (HtmlPage) ((HtmlSubmitInput) clink).click();
        output.clear();
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(!output.isEmpty());
        span = output.get(0);
        assertTrue("Current Name: Mojarra".equals(span.asText()));

        // click the commandButton with a nested setPropertyActionListener
        // to cause the name attribute in the session to be cleared
        HtmlElement cbutton = page.getHtmlElementById("form:s2");
        assertNotNull(clink);
        page = (HtmlPage) ((HtmlAnchor) cbutton).click();
        output.clear();
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(!output.isEmpty());
        span = output.get(0);
        assertTrue("Current Name:".equals(span.asText()));
        
    }


    /*
     * Added for issue 909.
     */
    public void testTagSourceFromDtdDocument() throws Exception {

        HtmlPage page = getPage("/faces/facelets/sourcefromdtdconfig.xhtml") ;

        // verify the output is initially null
        List<HtmlSpan> output = new ArrayList<HtmlSpan>(1);
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(!output.isEmpty());
        HtmlSpan span = output.get(0);
        assertTrue("Hello!".equals(span.asText()));
        assertTrue("color:red".equals(span.getStyleAttribute()));

    }


    /*
     * Verify #{component} and #{cc} expressions evaluate
     * at build time.
     * @throws Exception
     */
    public void testComponentELAtBuildTime() throws Exception {

        HtmlPage page = getPage("/faces/facelets/componentELAtBuildTime.xhtml") ;

        // verify the output is initially null
        List<HtmlSpan> output = new ArrayList<HtmlSpan>(4);
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(output.size() == 3);
        for (HtmlSpan span : output) {
            assertTrue("PASSED".equals(span.asText()));
        }

    }


    /**
     * Ensure attributes that evaluate EL expressions only render the attribute
     * name/value pair when the value is non-null.
     *
     * https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=940
     */ 
    public void testConditionalAttributeRendering() throws Exception {

        HtmlPage page = getPage("/faces/facelets/conditionalCCAttributeRendering.xhtml") ;

        // verify the output is initially null
        List<HtmlSpan> output = new ArrayList<HtmlSpan>(1);
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(output.size() == 1);
        HtmlSpan span = output.get(0);
        assertTrue(span.getStyleAttribute().length() == 0);
        HtmlSubmitInput add = (HtmlSubmitInput) getInputContainingGivenId(page, "form:add");
        page = add.click();

        // ensure the span is now styled since there is a non-null value available.
        output.clear();
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(output.size() == 1);
        span = output.get(0);
        assertEquals("color:red", span.getStyleAttribute());

        // ensure the style is not rendered once again after the attribute value
        // is set to "".
        HtmlSubmitInput remove = (HtmlSubmitInput) getInputContainingGivenId(page, "form:remove");
        page = remove.click();
        output.clear();
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(output.size() == 1);
        span = output.get(0);
        assertTrue(span.getStyleAttribute().length() == 0);

    }

    public void testTemplateComp() throws Exception {
        lastpage = getPage("/faces/facelets/templateComp.xhtml");

        assertTrue("Template Test".equals(lastpage.getTitleText()));

        String templateText = lastpage.getElementById("templateText").getTextContent();
        assertTrue("Template text".equals(templateText));

        String toplevelContent = lastpage.getElementById("toplevelContent").getTextContent();
        assertTrue("Inserted Content".equals(toplevelContent));
    }

    public void testTemplateDecorate() throws Exception {
        lastpage = getPage("/faces/facelets/templateDecorate.xhtml");

        assertTrue("Decorate Test".equals(lastpage.getTitleText()));

        String templateText = lastpage.getElementById("comp").getTextContent();
        assertTrue("Composition Text".equals(templateText));

        String toplevelContent = lastpage.getElementById("insert").getTextContent();
        assertTrue("Inserted Text".equals(toplevelContent));
    }


    public void testValidatorWrappingNestingDisableHandling() throws Exception {

        HtmlPage page = getPage("/faces/facelets/validatorDisabled.xhtml");
        HtmlTextInput input = (HtmlTextInput) getInputContainingGivenId(page, "form1:input");
        assertNotNull(input);
        input.setValueAttribute("aaaa");
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, "form1:sub");
        page = submit.click();

        HtmlUnorderedList list = (HtmlUnorderedList) page.getElementById("form1:messages1");
        int count = 0;
        for (HtmlElement element : list.getAllHtmlChildElements()) {
            count++;
            assertEquals("form1:input: Validation Error: Value is less than allowable minimum of '5'", element.asText());
            if (count > 1) {
                fail("Expected a single validation failure");
            }
        }

        page = getPage("/faces/facelets/validatorDisabled.xhtml");
        HtmlTextInput input1 = (HtmlTextInput) getInputContainingGivenId(page, "form2:input1");
        HtmlTextInput input2 = (HtmlTextInput) getInputContainingGivenId(page, "form2:input2");
        input1.setValueAttribute("aaaa");
        input2.setValueAttribute("aaaa");
        submit = (HtmlSubmitInput) getInputContainingGivenId(page, "form2:sub");
        page = submit.click();

        HtmlElement list1 = page.getElementById("form2:messages2");
        assertTrue(list1 instanceof HtmlDivision); // if it's not, it means messages where displayed
        HtmlUnorderedList list2 = (HtmlUnorderedList) page.getElementById("form2:messages3");
        assertFalse(list1.getAllHtmlChildElements().iterator().hasNext());
        count = 0;
        for (HtmlElement element : list2.getAllHtmlChildElements()) {
            count++;
            assertEquals("form2:input2: Validation Error: Value is less than allowable minimum of '5'", element.asText());
            if (count > 1) {
                fail("Expected a single validation failure");
            }
        }
    }


    public void testUIRepeatVarStatusBroadcast() throws Exception {

        HtmlPage page = getPage("/faces/facelets/uirepeat2.xhtml");
        List<HtmlAnchor> anchors = new ArrayList<HtmlAnchor>(4);
        getAllElementsOfGivenClass(page, anchors, HtmlAnchor.class);
        assertEquals("Expected to find only 4 HtmlAnchors", 4, anchors.size());
        String[] expectedValues = {
              "Index: 0",
              "Index: 1",
              "Index: 2",
              "Index: 3",
        };

        for (int i = 0, len = expectedValues.length; i < len; i++) {
            HtmlAnchor anchor = anchors.get(i);
            page = anchor.click();
            assertTrue(page.asText().contains(expectedValues[i]));
        }

    }


    public void testUIRepeatStateNotLostOnNonUIRepeatMessage() throws Exception {

        HtmlPage page = getPage("/faces/facelets/uirepeat3.xhtml");
        List<HtmlTextInput> inputs = new ArrayList<HtmlTextInput>(5);
        getAllElementsOfGivenClass(page, inputs, HtmlTextInput.class);
        assertEquals("Expected 5 input fields", 5, inputs.size());
        inputs.get(0).setValueAttribute("A"); // this causes a validation failure
        inputs.get(1).setValueAttribute("1");
        inputs.get(2).setValueAttribute("2");
        inputs.get(3).setValueAttribute("3");
        inputs.get(4).setValueAttribute("4");
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, "submit");
        page = submit.click();
        assertTrue(page.asText().contains("'A' is not a number."));
        // now verify the inputs nested within the UIRepeat were not cleared
        inputs.clear();
        getAllElementsOfGivenClass(page, inputs, HtmlTextInput.class);
        assertEquals("A", inputs.get(0).getValueAttribute());
        assertEquals("1", inputs.get(1).getValueAttribute());
        assertEquals("2", inputs.get(2).getValueAttribute());
        assertEquals("3", inputs.get(3).getValueAttribute());
        assertEquals("4", inputs.get(4).getValueAttribute());
        
    }


    /**
     * Added for issue 1202.  Ensure duplicate phase listeners aren't registered
     * when using f:phaseListener and partial state saving is enabled (which is
     * the default for systest).
     */
    public void testPhaseListenerRegistration() throws Exception {

        HtmlPage page = getPage("/faces/facelets/viewPhaseListeners.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, "form:button");
        assertNotNull(submit);

        for (int i = 0; i < 5; i++) {
            page = (HtmlPage) submit.click();
            assertTrue(!page.asText().contains("ERROR"));
            submit = (HtmlSubmitInput) getInputContainingGivenId(page, "form:button");
        }

    }


    /**
     * Added for issue 1218.
     */
    public void testForEachVarStatusNoException() throws Exception {

        HtmlPage page = getPage("/faces/facelets/forEach.xhtml");
        assertTrue(page.asText().contains("1 2 3"));
        
    }

}
