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

import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.List;

/**
 * <p>Test that invalid values don't cause valueChangeEvents to occur.</p>
 */

public class ValueChangeListenerTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ValueChangeListenerTestCase(String name) {
        super(name);
    }

    // ------------------------------------------------------ Instance Variables

    // ---------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ValueChangeListenerTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods
    public void testValueChangeListener() throws Exception {
        HtmlPage page = getPage("/faces/valueChangeListener.jsp");
        List list;
        list = getAllElementsOfGivenClass(page, null,
                HtmlTextInput.class);

        // set the initial value to be 1 for both fields
        ((HtmlTextInput) list.get(0)).setValueAttribute("1");
        ((HtmlTextInput) list.get(1)).setValueAttribute("1");

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 !=
                page.asText().indexOf("Received valueChangeEvent for textA"));

        assertTrue(-1 !=
                page.asText().indexOf("Received valueChangeEvent for textB"));

        // re-submit the form, make sure no valueChangeEvents are fired
        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 ==
                page.asText().indexOf("Received valueChangeEvent for textA"));

        assertTrue(-1 ==
                page.asText().indexOf("Received valueChangeEvent for textB"));

        // give invalid values to one field and make sure no
        // valueChangeEvents are fired.
        list = getAllElementsOfGivenClass(page, null,
                HtmlTextInput.class);

        ((HtmlTextInput) list.get(1)).setValueAttribute("-123");

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 ==
                page.asText().indexOf("Received valueChangeEvent for textA"));

        assertTrue(-1 ==
                page.asText().indexOf("Received valueChangeEvent for textB"));

        assertTrue(-1 !=
                page.asText().indexOf("Validation Error"));

        // make sure dir and lang are passed through as expected for
        // message and messages
        list = getAllElementsOfGivenClass(page, null,
                HtmlSpan.class);

        boolean
                hasMessageContent = false, // do we have the h:message
                // content we're looking for
                hasMessagesContent = false; // do we have the h:messages
        // content we're looking for.
        HtmlSpan span = null;
        HtmlUnorderedList ulist = null;

        for (int i = 0; i < list.size(); i++) {
            span = (HtmlSpan) list.get(i);
            if (-1 != span.asXml().indexOf("dir=\"RTL\"")
                    && span.asXml().indexOf("lang=\"de\"") != -1) {
                hasMessageContent = true;
            }
        }
        list = getAllElementsOfGivenClass(page, null, HtmlUnorderedList.class);
        for (int i = 0; i < list.size(); i++) {
            ulist = (HtmlUnorderedList) list.get(i);
            if (-1 != ulist.asXml().indexOf("dir=\"LTR\"")
                    && ulist.asXml().indexOf("lang=\"en\"") != -1) {
                hasMessagesContent = true;
            }
        }
        assertTrue(hasMessagesContent && hasMessageContent);

    }

    // Test case for Issue 752
    // https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=752
    public void testValueChangeListener02() throws Exception {

        HtmlPage page = getPage("/faces/valueChangeListener02.jsp");
        List list = getAllElementsOfGivenClass(page,
                null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();
        assertTrue(!page.asText().contains("old value"));
        assertTrue(!page.asText().contains("new value"));

        list = getAllElementsOfGivenClass(page,
                null,
                HtmlCheckBoxInput.class);
        HtmlCheckBoxInput input = (HtmlCheckBoxInput) list.get(0);
        input.setChecked(false);
        list = getAllElementsOfGivenClass(page,
                null,
                HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(page.asText().contains("old value: true"));
        assertTrue(page.asText().contains("new value: false"));

        list = getAllElementsOfGivenClass(page,
                null,
                HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();
        assertTrue(!page.asText().contains("old value"));
        assertTrue(!page.asText().contains("new value"));

    }

}
