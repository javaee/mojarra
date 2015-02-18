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

package com.sun.faces.composite;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for Composite Components.
 */
public class CompositeComponentsTestCase extends HtmlUnitFacesTestCase {


    @SuppressWarnings({"UnusedDeclaration"})
    public CompositeComponentsTestCase() {
        this("CompositeComponentsTestCase");
    }

    public CompositeComponentsTestCase(String name) {
        super(name);
        addExclusion(Container.TOMCAT6, "testForNoNPE");
        addExclusion(Container.TOMCAT7, "testForNoNPE");
        addExclusion(Container.WLS_10_3_4_NO_CLUSTER, "testForNoNPE");
        addExclusion(Container.TOMCAT6, "testMetadataCache");
        addExclusion(Container.TOMCAT7, "testMetadataCache");
        addExclusion(Container.WLS_10_3_4_NO_CLUSTER, "testMetadataCache");
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

    public void testIsCompositeComponent() throws Exception {
        HtmlPage page = getPage("/faces/composite/isCompositeComponentUsing.xhtml");
        assertTrue(page.asText().contains("isCompositeComponent: true"));
    }

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
     *   Maps Validator to inputText within composite/validator2.xhtml using
     *   name and target attributes.
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

    /**
     * <p>
     *  Maps Converter to inputText within composite/validator1.xhtml using
     *   only the name attribute.
     * </p>
     *
     * <p>
     *   Maps Converter to inputText within composite/validator2.xhtml using
     *   name and target attributes.
     * </p>
     *
     * <p>
     *   Maps Converter to inputText within composite/validator2.xhtml using
     *   name and target attributes.
     * </p>
     *
     * <p>
     *  Ensure validators are properly re-targeted when the
     *  target of the validator is nested within another naming
     *  container.  Note that the value of the 'for' attribute doesn't
     *  mimic the NamingContainer hierarchy, that's handled by the
     *  'targets' attribute within the composite:implementation section
     *  of validator4.xhtml.
     * </p>
     */
    public void testConverters() throws Exception {

        String[] messageSuffixes = new String[] {
              "form:converter1:input",
              "form2:converter2:it2",
              "form3:converter3:input:input",
              "form4:converter4:naming:input"
        };

        HtmlPage page = getPage("/faces/composite/attachedconverter.xhtml");
        validateConverterMessages(page, messageSuffixes);
        page = pushButton(page, "cf:clear");
        validateConverterMessages(page, messageSuffixes);

    }


    public void testActions() throws Exception {

        final String[] commandIds = {
              "form:c0:command",
              "form:c1:nesting:aw1:command",
              "form:c2:nesting:aw2:command",
              "form:c3:nesting:aw3:nesting:aw1:command",
              "form:c4:nesting:aw4:nesting:aw1:command"
        };

        HtmlPage page = getPage("/faces/composite/action.xhtml");
        for (String commandId : commandIds) {
            HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, commandId);
            assertNotNull(submit);
            page = submit.click();
            String message = "Action invoked: " + commandId;
            assertTrue(page.asText().contains(message));
        }
        
    }


    public void testCustomActions() throws Exception {

        final String[] commandIds = {
              "form:c0:command",
              "form:c1:nesting:aw1:command",
              "form:c2:nesting:aw2:command",
              "form:c3:nesting:aw3:nesting:aw1:command",
              "form:c4:nesting:aw4:nesting:aw1:command"
        };

        HtmlPage page = getPage("/faces/composite/customAction.xhtml");
        for (String commandId : commandIds) {
            HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, commandId);
            assertNotNull(submit);
            page = submit.click();
            String message = "Custom action invoked: " + commandId;
            assertTrue(page.asText().contains(message));
        }

    }


    public void testActionListeners() throws Exception {

        final String[] commandIds = {
              "form:c0:command",
              "form:c1:nesting:aw1:command",
              "form:c2:nesting:aw2:command",
              "form:c3:nesting:aw3:nesting:aw1:command",
              "form:c4:nesting:aw4:nesting:aw1:command"
        };

        HtmlPage page = getPage("/faces/composite/actionListener.xhtml");
        for (String commandId : commandIds) {
            HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, commandId);
            assertNotNull(submit);
            page = submit.click();
            String message = "ActionListener invoked: " + commandId;
            assertTrue(page.asText().contains(message));
        }

    }


    public void testValidators() throws Exception {
        try {
            getPage("/faces/setApplicationMapProperty.xhtml?name=javax.faces.VALIDATE_EMPTY_FIELDS&value=true");

            HtmlPage page = getPage("/faces/composite/validator.xhtml");
            HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, "form:submit");
            assertNotNull(submit);
            page = submit.click();

            final String[] inputIds = {
                "form:c0:input",
                "form:c1:nesting:aw1:input",
                "form:c2:nesting:aw2:input",
                "form:c3:nesting:aw3:nesting:aw1:input",
                "form:c4:nesting:aw4:nesting:aw1:input"
            };

            String pageText = page.asText();
            for (String inputId : inputIds) {
                String message = "validator invoked: " + inputId;
                assertTrue(pageText.contains(message));
            }
        }
        finally {
            getPage("/faces/clearApplicationMapProperty.xhtml?name=javax.faces.VALIDATE_EMPTY_FIELDS");

        }

    }


    public void testValueChangeListeners() throws Exception {

        HtmlPage page = getPage("/faces/composite/valueChangeListener.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, "form:submit");
        assertNotNull(submit);
        page = submit.click();

        final String[] inputIds = {
              "form:c0:input",
              "form:c1:nesting:aw1:input",
              "form:c2:nesting:aw2:input",
              "form:c3:nesting:aw3:nesting:aw1:input",
              "form:c4:nesting:aw4:nesting:aw1:input"
        };

        String pageText = page.asText();
        for (String inputId : inputIds) {
            String message = "ValueChange invoked: " + inputId;
            assertTrue(pageText.contains(message));
        }

    }


    public void testNesting01() throws Exception {

        HtmlPage page = getPage("/faces/composite/nesting01.xhtml");
        List<HtmlSpan> spans = new ArrayList<HtmlSpan>(2);
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertEquals(2, spans.size());
        HtmlSpan span = spans.get(0);
        assertEquals("Should have a value: Hello World", span.asText());
        span = spans.get(1);
        assertEquals("Shouldn't have a value:", span.asText());

    }


    public void testNesting02() throws Exception {

        HtmlPage page = getPage("/faces/composite/nesting02.xhtml");
        HtmlSubmitInput input = (HtmlSubmitInput) getInputContainingGivenId(page, "commandButton");
        assertNotNull(input);
        page = input.click();
        assertEquals("Navigation Result", page.getTitleText());

    }


    public void testNesting03() throws Exception {

        HtmlPage page = getPage("/faces/composite/nesting03.xhtml");
        HtmlSubmitInput input = (HtmlSubmitInput) getInputContainingGivenId(page, "commandButton");
        assertNotNull(input);
        page = input.click();
        assertEquals("Navigation Result", page.getTitleText());

    }


    public void testNesting04() throws Exception {

        HtmlPage page = getPage("/faces/composite/nesting04.xhtml");
        List<HtmlSpan> spans = new ArrayList<HtmlSpan>(3);
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertEquals(3, spans.size());
        assertEquals("static", spans.get(0).asText());
        assertEquals("com.sun.faces.context.FacesContextImpl", spans.get(1).asText());
        assertEquals("form:nesting4", spans.get(2).asText());

        HtmlSubmitInput input = (HtmlSubmitInput) getInputContainingGivenId(page, "form:sub");
        page = input.click();
        spans.clear();
         getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertEquals(3, spans.size());
        assertEquals("static", spans.get(0).asText());
        assertEquals("com.sun.faces.context.FacesContextImpl", spans.get(1).asText());
        assertEquals("form:nesting4", spans.get(2).asText());
        
    }

    public void testNesting06() throws Exception {
        HtmlPage page = getPage("/faces/composite/addPhaseListener.xhtml");
        assertTrue(page.asText().contains("/composite/addPhaseListener.xhtml PASSED"));
    }


    /**
     * Added for issue 1238.
     */
    public void testNesting07() throws Exception {

        HtmlPage page = getPage("/faces/composite/nestingCompositeExpressionTreeCreation.xhtml");
        List<HtmlSpan> spans = new ArrayList<HtmlSpan>(5);
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertEquals(5, spans.size());
        final String[] expectedSpanValues = {
              "PASSED", "PASSED", "PASSED", "PASSED", "FAILED"
        };
        for (int i = 0; i < expectedSpanValues.length; i++) {
            assertEquals(expectedSpanValues[i], expectedSpanValues[i], spans.get(i).asText());
        }

        // redisplay the view to make sure nothing changes over post back
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, "form:submit");
        assertNotNull(submit);
        page = submit.click();
        spans.clear();
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertEquals(5, spans.size());
        for (int i = 0; i < expectedSpanValues.length; i++) {
            assertEquals(expectedSpanValues[i], expectedSpanValues[i], spans.get(i).asText());
        }
        
    }

    public void testChildrenAndFacets() throws Exception {

        HtmlPage page = getPage("/faces/composite/childrenfacets.xhtml");
        List<HtmlSpan> spans = new ArrayList<HtmlSpan>(6);
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        validateChildrenAndFacets(spans);
        page = pushButton(page, "form:submit");
        spans.clear();
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        validateChildrenAndFacets(spans);
        
    }


    public void testCompositeInsertChildrenNested() throws Exception {

        HtmlPage page = getPage("/faces/composite/compositeInsertChildrenNesting.xhtml");
        List<HtmlSpan> spans = new ArrayList<HtmlSpan>();
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        String[] expectedItems = {
            "Before Insert A(1)",
            "Before Nested compcomp (3)",
            "Before Insert B(1)",
            "Inside nested Component (4)",
            "After Insert B(2)",
            "After Nested compcomp(5)",
            "After Insert A(2)"
        };
        assertTrue(spans.size() == expectedItems.length);
        for (int i = 0, len = expectedItems.length; i < len; i++) {
            assertTrue(expectedItems[i].equals(spans.get(i).asText()));    
        }

        HtmlSubmitInput input = (HtmlSubmitInput) getInputContainingGivenId(page, "form:submit");
        assertNotNull(input);
        page = input.click();

        spans = new ArrayList<HtmlSpan>();
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertTrue(spans.size() == expectedItems.length);
        for (int i = 0, len = expectedItems.length; i < len; i++) {
            assertTrue(expectedItems[i].equals(spans.get(i).asText()));
        }
        
    }


    public void testCompositeInsertChildrenNested02() throws Exception {

        HtmlPage page = getPage("/faces/composite/compositeInsertChildrenNesting02.xhtml");
        List<HtmlDivision> divs = new ArrayList<HtmlDivision>(1);
        getAllElementsOfGivenClass(page, divs, HtmlDivision.class);
        assertTrue(Integer.toString(divs.size()), divs.size() == 1);
        HtmlDivision div = divs.get(0);
        int count = 0;
        Class[] expectedElements = {
              HtmlAnchor.class,
              HtmlBreak.class
        };
        for (HtmlElement element : div.getChildElements()) {
            if (count > 2) {
                fail("Expected two children of the div");
            }
            assertTrue(element.getClass().equals(expectedElements[count]));
            count++;
        }
        
    }


    public void testCompositeInsertFacetNested() throws Exception {

        HtmlPage page = getPage("/faces/composite/compositeInsertFacetNesting.xhtml");
        List<HtmlTable> tables = new ArrayList<HtmlTable>();
        getAllElementsOfGivenClass(page, tables, HtmlTable.class);
        assertTrue(tables.size() == 1);
        HtmlTable table = tables.get(0);
        HtmlTableHeader header = table.getHeader();
        assertNotNull(header);
        assertEquals(header.asText(), "header");
        HtmlTableFooter footer = table.getFooter();
        assertNotNull(footer);
        assertEquals(footer.asText(), "footer");

    }


    public void testCompositeInsertFacetNested02() throws Exception {

        HtmlPage page = getPage("/faces/composite/compositeInsertFacetNesting02.xhtml");
        List<HtmlTable> tables = new ArrayList<HtmlTable>();
        getAllElementsOfGivenClass(page, tables, HtmlTable.class);
        assertTrue(tables.size() == 1);
        HtmlTable table = tables.get(0);
        HtmlTableHeader header = table.getHeader();
        assertNotNull(header);
        assertEquals(header.asText(), "Header");
        HtmlTableFooter footer = table.getFooter();
        assertNotNull(footer);
        assertEquals(footer.asText(), "Footer");

    }


    public void testInsertFacetRequired01() throws Exception {

        // facet required but not present
        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/insertfacetrequired01.xhtml");
        assertTrue(page.asText().contains("Unable to find facet named 'header'"));

    }


    public void testInsertFacetRequired02() throws Exception {

        // facet required but not present
        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/insertfacetrequired02.xhtml");
        assertTrue(page.asText().contains("Unable to find facet named 'header'"));

    }


    public void testInsertFacetRequired03() throws Exception {

        // facet not required and not present
        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/insertfacetrequired03.xhtml");
        assertTrue(!page.asText().contains("Unable to find facet named 'header'"));

    }
    
    
    public void testRenderFacetRequired01() throws Exception {

        // facet required but not present
        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/renderfacetrequired01.xhtml");
        assertTrue(page.asText().contains("Unable to find facet named 'header'"));

    }


    public void testRenderFacetRequired02() throws Exception {

        // facet required but not present
        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/renderfacetrequired02.xhtml");
        assertTrue(page.asText().contains("Unable to find facet named 'header'"));

    }


    public void testRenderFacetRequired03() throws Exception {

        // facet not required and not present
        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/renderfacetrequired03.xhtml");
        assertTrue(!page.asText().contains("Unable to find facet named 'header'"));

    }
    
    
    public void testInsertChildrenRequired01() throws Exception {

        // facet required but not present
        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/insertchildrenrequired01.xhtml");
        assertTrue(page.asText().contains("Unable to find any children components nested within parent composite component"));

    }


    public void testInsertChildrenRequired02() throws Exception {

        // facet required but not present
        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/insertchildrenrequired02.xhtml");
        assertTrue(page.asText().contains("Unable to find any children components nested within parent composite component"));

    }


    public void testInsertChildrenRequired03() throws Exception {

        // facet not required and not present
        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/insertchildrenrequired03.xhtml");
        assertTrue(!page.asText().contains("Unable to find any children components nested within parent composite component"));

    }

    public void testTemplateDecorate() throws Exception {
        lastpage = getPage("/faces/composite/decorate.xhtml");

        assertTrue("Decorate Test".equals(lastpage.getTitleText()));

        String templateText = lastpage.getElementById("comp").getTextContent();
        assertTrue("Composition Text".equals(templateText));

        String toplevelContent = lastpage.getElementById("insert").getTextContent();
        assertTrue("Inserted Text".equals(toplevelContent));
    }

    public void testMetadataCache() throws Exception {
        HtmlPage page = getPage("/faces/composite/boostrapCompositeComponentMetadata.xhtml");
        String text = page.asText();
        assertTrue(text.contains("First call longer than second call"));
    }

    public void testMethodExprNotRequired() throws Exception {

        try {
            getPage("/faces/composite/methodExprNotRequired.xhtml");
        } catch (Exception e) {
            fail("Exception thrown when compiling page methodExprNotRequired.");
        }

    }

    public void testMethodExprRequired() throws Exception {

        try {
            getPage("/faces/composite/methodExprRequired.xhtml");
            fail("No exception thrown when composite component was missing a required MethodExpression enabled attribute");
        } catch (Exception e) {
            
        }

    }


    public void testCompositionWithinComposite() throws Exception {
        try {
            getPage("/faces/composite/compositionWithinCompositeUsingPage.xhtml");
            fail("No exception thrown when composite component contained ui:composition");
        } catch (Exception e) {

        }

    }


    /**
     * Added for issue 1265.
     */
    public void testCompositeComponentResolutionWithinRelocatableResources() throws Exception {

        HtmlPage page = getPage("/faces/composite/compAttributeResourceRelocation.xhtml");
        List<HtmlStyle> styles = new ArrayList<HtmlStyle>(4);
        List<HtmlScript> scripts = new ArrayList<HtmlScript>(1);
        getAllElementsOfGivenClass(page, styles, HtmlStyle.class);
        getAllElementsOfGivenClass(page, scripts, HtmlScript.class);
        assertEquals(5, styles.size());
        assertEquals(2, scripts.size());
        String[] styleValues = {
              "color:red",
              "color:blue",
              "color:red",
              "color:red",
              "color:red"
        };
        String[] scriptValues = {
              "var a = \"ss\";",
              "var a = \"ss\";"
        };

        for (int i = 0, len = styles.size(); i < len; i++) {
            assertTrue(styles.get(i).asXml().contains(styleValues[i]));
        }
        for (int i = 0, len = scripts.size(); i < len; i++) {
            System.out.println(scripts.get(i).asXml());
            assertTrue(scripts.get(i).asXml().contains(scriptValues[i]));
        }

        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, "form:submit");
        page = submit.click();

        styles.clear();
        scripts.clear();
        getAllElementsOfGivenClass(page, styles, HtmlStyle.class);
        getAllElementsOfGivenClass(page, scripts, HtmlScript.class);
        assertEquals(5, styles.size());
        assertEquals(2, scripts.size());
        for (int i = 0, len = styles.size(); i < len; i++) {
            assertTrue(styles.get(i).asXml().contains(styleValues[i]));
        }
        for (int i = 0, len = scripts.size(); i < len; i++) {
            assertTrue(scripts.get(i).asXml().contains(scriptValues[i]));
        }
    }


    /**
     * Added for issue 1290.
     */
    public void testCCParentExpressionEvaluationWithNestingLevels() throws Exception {

        HtmlPage page = getPage("/faces/composite/nesting07.xhtml");
        List<HtmlSpan> spans = new ArrayList<HtmlSpan>(4);
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);

        assertEquals(4, 4, spans.size());

        final String[] expectedSpanValues = {
              "A",  // runtime eval value
              "A",  // tree creation eval value
              "B",  // tree creation eval value
              "C"   // tree creation eval value
        };

        for (int i = 0, len = spans.size(); i < len; i++) {
            String spanText = spans.get(i).asText();
            assertEquals("Index: "
                         + i
                         + ", expected: "
                         + expectedSpanValues[i]
                         + ", received: "
                         + spanText,
                         expectedSpanValues[i],
                         spanText);
        }

        HtmlSubmitInput button = (HtmlSubmitInput) getInputContainingGivenId(page, "form:submit");
        assertNotNull(button);

        page = button.click();

        spans.clear();
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertEquals(4, 4, spans.size());
        for (int i = 0, len = spans.size(); i < len; i++) {
            String spanText = spans.get(i).asText();
            assertEquals("Index: "
                         + i
                         + ", expected: "
                         + expectedSpanValues[i]
                         + ", received: "
                         + spanText,
                         expectedSpanValues[i],
                         spanText);
        }

    }


    /**
     * Added for issue 1298.
     */
    /*
    public void testMethodExpressionNesting() throws Exception {

        HtmlPage page = getPage("/faces/composite/nesting08.xhtml");

        HtmlSubmitInput button = (HtmlSubmitInput) getInputContainingGivenId(page, "form:submit");
        assertNotNull(button);

        page = button.click();
        assertTrue(page.asText().contains("Action invoked"));

    }
    */


    public void testMethodExpressionDefaults() throws Exception {

        HtmlPage page = getPage("/faces/composite/defaultAttributeMethodExpression.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page, "def:form1:command");
        page = submit.click();
        assertTrue(page.asText().contains("Action invoked"));

        page = getPage("/faces/composite/defaultAttributeMethodExpression.xhtml");
        submit = (HtmlSubmitInput) getInputContainingGivenId(page, "def:form2:command2");
        page = submit.click();
        assertTrue(page.asText().contains("ActionListener invoked"));

        page = getPage("/faces/composite/defaultAttributeMethodExpression.xhtml");
        submit = (HtmlSubmitInput) getInputContainingGivenId(page, "def:form3:command3");
        page = submit.click();
        assertTrue(page.asText().contains("Custom action invoked"));

        page = getPage("/faces/composite/defaultAttributeMethodExpression.xhtml");
        submit = (HtmlSubmitInput) getInputContainingGivenId(page, "def:form4:command");
        HtmlTextInput text = (HtmlTextInput) getInputContainingGivenId(page, "def:form4:input");
        text.setValueAttribute("foo");
        page = submit.click();
        assertTrue(page.asText().contains("validator invoked"));

        page = getPage("/faces/composite/defaultAttributeMethodExpression.xhtml");
        submit = (HtmlSubmitInput) getInputContainingGivenId(page, "def:form5:command");
        page = submit.click();
        assertTrue(page.asText().contains("ValueChange invoked"));

    }


    public void testProgrammaticDefaultAttributeValueAccess() throws Exception {
        HtmlPage page = getPage("/faces/composite/programmaticDefaultAttributeValueAccess.xhtml");
        String pageText = page.asText();
        assertTrue(pageText.contains("attr1 value is attr1Value. attr2 value is attr2Value."));
        assertTrue(pageText.contains("attr3 value is"));
        assertTrue(pageText.contains("action value is"));
        assertTrue(pageText.contains("actionListener value is"));
        assertTrue(pageText.contains("validator value is"));
        assertTrue(pageText.contains("valueChangeListener value is"));
    }
    
    public void testDefaultAttributeValueELOverrides() throws Exception {
        HtmlPage page = getPage("/faces/composite/issue-1782-using.xhtml");
        String text = page.asText();
        System.out.println(text);
        assertTrue(text.matches("(?s).*collapsable\\s=\\strue.*"));
    }

    public void testCCPreRenderViewEvent() throws Exception {
        HtmlPage page = getPage("/faces/composite/1462-using.xhtml");
        String text = page.asText();
        System.out.println(text);
        assertTrue(text.matches("(?s).*Message:.*Received.*event:.*javax.faces.event.PreRenderViewEvent.*for.*component:.*javax.faces.component.UIViewRoot.*"));

    }

    public void testDefaultAttributeValues() throws Exception {
        HtmlPage page = getPage("/faces/composite/defaultAttributesUsingPage.xhtml");
        String pageAsText = page.asText();
        assertTrue(pageAsText.contains("rendered=true"));
        assertTrue(pageAsText.contains("foo=bar"));
    }

    // --------------------------------------------------------- Private Methods


    private void validateChildrenAndFacets(List<HtmlSpan> spans) throws Exception {

        String[] ids = new String[] {
            "form:cf:outheader2",
                  "form:cf:outheader",
                  "form:cf:out1",
                  "form:cf:out2",
                  "ccCount",
                  "header2Facet",
                  "header1Facet"

        };
        String[] values = new String[] {
              "Rendered",
              "Inserted",
              "v1",
              "v2",
              "0",
              "true",
              "true"
        };

        assertEquals(ids.length, spans.size());
        for (int i = 0, len = ids.length; i < len; i++) {
            HtmlSpan span = spans.get(i);
            assertEquals(ids[i], span.getId());
            assertEquals(values[i], span.asText());
        }
       
    }


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


    private void validateConverterMessages(HtmlPage page, String[] messageSuffixes) {

        List<HtmlUnorderedList> list = new ArrayList<HtmlUnorderedList>();
        getAllElementsOfGivenClass(page, list, HtmlUnorderedList.class);
        HtmlUnorderedList ulist = list.get(0);
        assertEquals("messages", ulist.getId());
        int count = 0;

        for (HtmlElement e : ulist.getAllHtmlChildElements()) {
            if (count > messageSuffixes.length) {
                fail("Expected only four message to be displayed");
            }
            String message = ("Converter Invoked : " + messageSuffixes[count]);
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
