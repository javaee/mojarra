/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Test cases for Facelets functionality
 */
public class FaceletsTestCase extends HtmlUnitFacesTestCase {


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

//        HtmlPage page = getPage("/faces/facelets/sourcefromdtdconfig.xhtml") ;
//
//        // verify the output is initially null
//        List<HtmlSpan> output = new ArrayList<HtmlSpan>(1);
//        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
//        assertTrue(!output.isEmpty());
//        HtmlSpan span = output.get(0);
//        assertTrue("Hello!".equals(span.asText()));
//        assertTrue("color:red".equals(span.getStyleAttribute()));

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
            assertEquals("form1:input: Validation Error: Length is less than allowable minimum of '5'", element.asText());
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
            assertEquals("form2:input2: Validation Error: Length is less than allowable minimum of '5'", element.asText());
            if (count > 1) {
                fail("Expected a single validation failure");
            }
        }
    }


    


    /**
     * Added for issue 1202.  Ensure duplicate phase listeners aren't registered
     * when using f:phaseListener and partial state saving is enabled (which is
     * the default for systest).
     */
    public void testPhaseListenerRegistration() throws Exception {
        // moved to new harness, see issue #2848
    }

    public void testWhen() throws Exception {
        HtmlPage page = getPage("/faces/facelets/when.xhtml");
        String text = page.asText();
        assertTrue(text.contains("size = 1"));
        assertTrue(text.contains("isEmpty = false"));
        assertTrue(text.contains("there is some!!!"));
        assertTrue(text.contains("there is some (really)!!!"));

    }


    /**
     * Added for issue 1552.
     */
    public void testModeratelyComplexTemplating() throws Exception {
        HtmlPage page = getPage("/faces/facelets/templateDecoration2.xhtml");
        String text = page.asText();
        assertTrue(text.contains("Inserted from client1 Default"));
    }


    /**
     * Added for issue 1313
     */
    public void testIssue1313() throws Exception {

        HtmlPage page = getPage("/faces/facelets/issue1313.xhtml");
        List<HtmlDivision> divs = new ArrayList<HtmlDivision>();

        getAllElementsOfGivenClass(page, divs, HtmlDivision.class);
        validateToggleState1(divs);
        HtmlSubmitInput input = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:nonajax");
        assertNotNull(input);
        page = input.click();
        divs.clear();
        getAllElementsOfGivenClass(page, divs, HtmlDivision.class);
        validateToggleState2(divs);
        input = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:ajax");
        assertNotNull(input);
        page = input.click();
        divs.clear();
        getAllElementsOfGivenClass(page, divs, HtmlDivision.class);
        validateToggleState1(divs);
        input = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:nonajax");
        assertNotNull(input);
        page = input.click();
        divs.clear();
        getAllElementsOfGivenClass(page, divs, HtmlDivision.class);
        validateToggleState2(divs);
        input = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:ajax");
        assertNotNull(input);
        page = input.click();
        divs.clear();
        getAllElementsOfGivenClass(page, divs, HtmlDivision.class);
        validateToggleState1(divs);

    }
    
    public void testIssue1576() throws Exception {
        HtmlPage page = getPage("/faces/facelets/Issue1576UsingPage.xhtml");
        
        String text = page.asText();
        char copyright = 0xa9;
        char middot = 0xb7;
        char nbsp = 0xa0;

        assertTrue(-1 != text.indexOf(copyright));
        assertTrue(-1 != text.indexOf(middot));
        // nbsp is converted to space on the server
        assertTrue(-1 == text.indexOf(nbsp));
        
        int [] rc = new int[1];

        // We have to use Socket to do this because HtmlUnit swallows the doctype.
        String xml = this.issueHttpRequest("GET", rc, "/faces/facelets/Issue1576UsingPage.xhtml");
        // assert there is exactly one DOCTYPE
        int i = xml.indexOf("DOCTYPE");
        assertTrue(-1 != i);
        assertTrue(-1 == xml.indexOf("DOCTYPE", i + "DOCTYPE".length()));
                
    }
    
    
    private String issueHttpRequest(String methodName, int [] rc, String path) throws Exception {

        URL url = getURL(path);
        Socket s = new Socket(url.getHost(), url.getPort());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        String requestLine = methodName + " /" + contextPath + path + " HTTP/1.1\r\n";
        writer.write(requestLine);
        writer.write("Host: " + url.getHost() + ":" + url.getPort() + "\r\n");
        writer.write("User-Agent: systest-client\r\n");
        writer.write("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n");
        writer.write("Connection: close\r\n");
        writer.write("\r\n");
        writer.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String cur = null;
        StringBuilder builder = new StringBuilder();
        rc[0] = -1;
        while (null != (cur = reader.readLine())) {
            if (-1 == rc[0]) {
                String [] tokens = cur.split("\\s");
                rc[0] = Integer.valueOf(tokens[1]);
            }
            builder.append(cur).append("\n");
        }
        writer.close();

        
        return builder.toString();
    }

    /*
     * Added for issue 1527
     */
    public void testForEach() throws Exception {

        HtmlPage page = getPage("/faces/facelets/forEach.xhtml");
        boolean uniqueIds = true;
        List<HtmlTextInput> input = new ArrayList<HtmlTextInput>();
        getAllElementsOfGivenClass(page, input, HtmlTextInput.class);
        String[] names = new String[input.size()];
        String[] temp = new String[input.size()];
        int i=0;
        int j=0;
        for (HtmlTextInput inputText : input) {
            names[i++] = inputText.getNameAttribute();
        }
        for (i=0; i < names.length; i++) {
            if (isUnique(names[i], temp)) {
                temp[j++] = names[i];
            } else {
                uniqueIds = false;
                break;
            }
        }
        assertTrue(uniqueIds);
    }

    public void testDefineInsertELExpression() throws Exception {
        HtmlPage page = getPage("/faces/facelets/Client3.xhtml");
        assertTrue(page.asText().contains("Inserted from client3"));
    }


    // --------------------------------------------------------- Private Methods

    private boolean isUnique(String s, String[] array) {
        for (int i=0; i < array.length; i++) {
            if (array[i] != null && s.equals(array[i])) {
                return false;
            }
        }
        return true;
    }

    private void validateToggleState1(List<HtmlDivision> divs) {
        assertTrue(divs.size() == 2);
        HtmlDivision div1 = divs.get(0);
        assertEquals("frag1", "frag1", div1.getId());
        assertEquals("frag1", "frag1", div1.asText().trim());
        HtmlDivision div2 = divs.get(1);
        assertEquals("otherwise", "otherwise", div2.getId());
        assertEquals("C:OTHERWISE TOGGLE STATE FALSE C:OTHERWISE",
                     "C:OTHERWISE TOGGLE STATE FALSE C:OTHERWISE",
                     div2.asText().trim());
    }


    private void validateToggleState2(List<HtmlDivision> divs) {
        assertTrue(divs.size() == 3);
        HtmlDivision div1 = divs.get(0);
        assertEquals("frag2", "frag2", div1.getId());
        assertEquals("frag2", "frag2", div1.asText().trim());
        HtmlDivision div2 = divs.get(1);
        assertEquals("if", "if", div2.getId());
        assertEquals("C:IF TOGGLE STATE TRUE C:IF",
                     "C:IF TOGGLE STATE TRUE C:IF",
                     div2.asText().trim());
        HtmlDivision div3 = divs.get(2);
        assertEquals("when", "when", div3.getId());
        assertEquals("C:WHEN TOGGLE STATE TRUE C:WHEN",
                     "C:WHEN TOGGLE STATE TRUE C:WHEN",
                     div3.asText().trim());
    }


}
