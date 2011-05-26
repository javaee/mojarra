/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.jsptest;

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
 * <p>Test that invalid values don't cause valueChangeEvents to occur.</p>
 */

public class ForEachTestCase extends HtmlUnitFacesTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ForEachTestCase(String name) {
        super(name);
        addExclusion(Container.WLS_10_3_4_NO_CLUSTER, "testForEachIssue714");
        addExclusion(Container.WLS_10_3_4_NO_CLUSTER, "testForEachIssue714");
    }

    // ------------------------------------------------------ Instance Variables

    // ---------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ForEachTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods
    public void testForEach() throws Exception {
        HtmlPage page = getPage("/faces/forEach01.jsp");

        // Make sure values are displayed properly for the initial request 
        //assert outputText values are as expected
        assertTrue(-1 != page.asText().indexOf("output1"));
        assertTrue(-1 != page.asText().indexOf("output2"));
        assertTrue(-1 != page.asText().indexOf("output3"));

        //assert inputText without "id" values are as expected
        assertTrue(-1 != page.asText().indexOf("inputText1=input1"));
        assertTrue(-1 != page.asText().indexOf("inputText2=input2"));
        assertTrue(-1 != page.asText().indexOf("inputText3=input3"));

        //assert inputText with "id" values are as expected
        assertTrue(-1 != page.asText().indexOf("inputid1"));
        assertTrue(-1 != page.asText().indexOf("inputid2"));
        assertTrue(-1 != page.asText().indexOf("inputid3"));

        // Assign new values to input fields, submit the form.
        List list;
        list = getAllElementsOfGivenClass(page, null,
                HtmlTextInput.class);

        ((HtmlTextInput) list.get(0)).setValueAttribute("newValue1");
        ((HtmlTextInput) list.get(1)).setValueAttribute("newValue2");
        ((HtmlTextInput) list.get(2)).setValueAttribute("newValue3");

        ((HtmlTextInput) list.get(3)).setValueAttribute("newValueid1");
        ((HtmlTextInput) list.get(4)).setValueAttribute("newValueid2");
        ((HtmlTextInput) list.get(5)).setValueAttribute("newValueid3");

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        // make sure the values are as expected on post back.
        assertTrue(-1 != page.asText().indexOf("output1"));
        assertTrue(-1 != page.asText().indexOf("output2"));
        assertTrue(-1 != page.asText().indexOf("output3"));

        //assert inputText without "id" values are as expected
        assertTrue(-1 != page.asText().indexOf("inputText1=input1"));
        assertTrue(-1 != page.asText().indexOf("inputText2=input2"));
        assertTrue(-1 != page.asText().indexOf("inputText3=input3"));

        assertTrue(-1 != page.asText().indexOf("newValue1"));
        assertTrue(-1 != page.asText().indexOf("newValue2"));
        assertTrue(-1 != page.asText().indexOf("newValue3"));

        //assert inputText with "id" values are as expected
        assertTrue(-1 != page.asText().indexOf("inputid1"));
        assertTrue(-1 != page.asText().indexOf("inputid2"));
        assertTrue(-1 != page.asText().indexOf("inputid3"));

        assertTrue(-1 != page.asText().indexOf("newValueid1"));
        assertTrue(-1 != page.asText().indexOf("newValueid2"));
        assertTrue(-1 != page.asText().indexOf("newValueid3"));
    }

    public void testForEachIssue714() throws Exception {

        HtmlPage page = getPage("/faces/forEach04.jsp");
        List<HtmlSpan> spans = new ArrayList<HtmlSpan>(2);
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertTrue(spans.size() == 2);
        HtmlSpan span = spans.get(0);
        assertTrue("j_id_id16:idfrag1:frag1".equals(span.getIdAttribute()));
        span = spans.get(1);
        assertTrue("j_id_id16:idfrag2:frag2".equals(span.getIdAttribute()));

        // submit the form to ensure no duplicate ID exceptions are
        // raised during post-back
        List<HtmlSubmitInput> buttons = new ArrayList<HtmlSubmitInput>(1);
        buttons = getAllElementsOfGivenClass(page, buttons, HtmlSubmitInput.class);
        assertTrue(buttons.size() == 1);
        HtmlSubmitInput submit = buttons.get(0);
        page = (HtmlPage) submit.click();

        // validate the IDs are as expected after post-back
        spans = new ArrayList<HtmlSpan>(2);
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        assertTrue(spans.size() == 2);
        span = spans.get(0);
        assertTrue("j_id_id16:idfrag1:frag1".equals(span.getIdAttribute()));
        span = spans.get(1);
        assertTrue("j_id_id16:idfrag2:frag2".equals(span.getIdAttribute()));

    }

}
