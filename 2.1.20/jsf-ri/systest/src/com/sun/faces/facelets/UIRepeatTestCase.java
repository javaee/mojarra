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

package com.sun.faces.facelets;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for Facelets functionality
 */
public class UIRepeatTestCase extends HtmlUnitFacesTestCase {


    // --------------------------------------------------------------- Test Init


    public UIRepeatTestCase() {
        this("UIRepeatTestCase");

        // this test is excluded because it won't pass in tomcat due to an issue with NumberConverter
        addExclusion(Container.TOMCAT6, "testUIRepeatStateNotLostOnNonUIRepeatMessage");
        addExclusion(Container.TOMCAT7, "testUIRepeatStateNotLostOnNonUIRepeatMessage");
        addExclusion(Container.WLS_10_3_4_NO_CLUSTER, "testUIRepeatStateNotLostOnNonUIRepeatMessage");

    }


    public UIRepeatTestCase(String name) {
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
        return (new TestSuite(UIRepeatTestCase.class));
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
    public void testUIRepeat() throws Exception {

        HtmlPage page = getPage("/faces/facelets/uirepeat.xhtml") ;
        
        String text = page.asText();
        
        assertTrue(-1 != text.indexOf("ListFlavor is chocolate. Begin is . End is . Index is 0. Step is . Index is even: true. Index is odd: false. Index is first: true. Index is last: false."));
        assertTrue(-1 != text.indexOf("ListFlavor is vanilla. Begin is . End is . Index is 1. Step is . Index is even: false. Index is odd: true. Index is first: false. Index is last: false."));
        assertTrue(-1 != text.indexOf("ListFlavor is strawberry. Begin is . End is . Index is 2. Step is . Index is even: true. Index is odd: false. Index is first: false. Index is last: false."));
        assertTrue(-1 != text.indexOf("ListFlavor is chocolate peanut butter. Begin is . End is . Index is 3. Step is . Index is even: false. Index is odd: true. Index is first: false. Index is last: true."));
        assertTrue(-1 != text.indexOf("ArrayFlavor is chocolate. Begin is . End is . Index is 0. Step is . Index is even: true. Index is odd: false. Index is first: true. Index is last: false."));
        assertTrue(-1 != text.indexOf("ArrayFlavor is vanilla. Begin is . End is . Index is 1. Step is . Index is even: false. Index is odd: true. Index is first: false. Index is last: false."));
        assertTrue(-1 != text.indexOf("ArrayFlavor is strawberry. Begin is . End is . Index is 2. Step is . Index is even: true. Index is odd: false. Index is first: false. Index is last: false."));
        assertTrue(-1 != text.indexOf("ArrayFlavor is chocolate peanut butter. Begin is . End is . Index is 3. Step is . Index is even: false. Index is odd: true. Index is first: false. Index is last: true."));
        
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


    public void testUIRepeatVarBeginEndStepProperties() throws Exception {

        HtmlPage page = getPage("/faces/facelets/uirepeat4.xhtml");
        List<HtmlSpan> spans = new ArrayList<HtmlSpan>(9);
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertEquals("Expected 9 spans", 9, spans.size());
        String[] expectedValues = {
              "vanilla : index=1 : begin=1 : end= : step= : first=true : last=false : even=true : odd=false",
              "strawberry : index=2 : begin=1 : end= : step= : first=false : last=false : even=false : odd=true",
              "chocolate peanut butter : index=3 : begin=1 : end= : step= : first=false : last=true : even=true : odd=false",
              "strawberry: index=2 : begin=2 : end=3 : step= : first=true : last=false : even=true : odd=false",
              "chocolate peanut butter: index=3 : begin=2 : end=3 : step= : first=false : last=true : even=false : odd=true",
              "chocolate: index=0 : begin= : end= : step=2 : first=true : last=false : even=true : odd=false",
              "strawberry: index=2 : begin= : end= : step=2 : first=false : last=true : even=false : odd=true",
              "vanilla: index=1 : begin=1 : end=1 : step=2 : first=true : last=true : even=true : odd=false",
              "chocolate: index=0 : begin= : end= : step= : first=true : last=true : even=true : odd=false"
        };
        for (int i = 0, len = spans.size(); i < len; i++) {
            assertEquals("Expected: " + expectedValues[i] + ", received: " + spans.get(i).asText(),
                         expectedValues[i],
                         spans.get(i).asText());
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
