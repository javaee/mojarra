/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.systest.el;

import java.util.List;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlInput;

/**
 * Validate new EL features such as the component implicit object
 */
public class ELTestCase extends AbstractTestCase {

    public ELTestCase(String name) {
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
        return (new TestSuite(ELTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }

    // ------------------------------------------------------------ Test Methods


    public void testComponentImplicitObject() throws Exception {
        HtmlPage page = getPage("/faces/componentImplicitObject.jsp");
        List<HtmlSpan> outputs = new ArrayList<HtmlSpan>(2);
        getAllElementsOfGivenClass(page, outputs, HtmlSpan.class);
        assertTrue(outputs.size() ==2);
        HtmlSpan s = outputs.get(0);
        assertTrue("ot".equals(s.getId()));
        assertTrue("ot".equals(s.asText()));
        s = outputs.get(1);
        assertTrue(s.getId().contains("facetOT"));
        assertTrue("facetOT".equals(s.asText()));
        List<HtmlInput> inputs = new ArrayList<HtmlInput>(2);
        getAllElementsOfGivenClass(page, inputs, HtmlInput.class);
        HtmlInput i = inputs.get(2);
        assertTrue(i.getId().contains("0:it"));
        assertTrue("it".equals(i.asText()));
        i = inputs.get(3);
        assertTrue(i.getId().contains("1:it"));
        assertTrue("it".equals(i.asText()));

    }


    public void testProgrammaticExpressionFunctionEval() throws Exception {

        HtmlPage page = getPage("/faces/elfunction.xhtml");
        assertTrue(page.asText().contains("PASSED"));
        
    }

}