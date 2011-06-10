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

package com.sun.faces.systest;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Test Case for JSP Interoperability.</p>
 */

public class ComponentMiscTestCase extends HtmlUnitFacesTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ComponentMiscTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


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
        return (new TestSuite(ComponentMiscTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    public void testModelCoercionForUISelectOne() throws Exception {
        HtmlPage page = getPage("/faces/ModelSelectItemConversion.jsp");
        HtmlSelect select = (HtmlSelect) getAllElementsOfGivenClass(page, 
                                                                    new ArrayList(),
                                                                    HtmlSelect.class).get(0);
        HtmlOption option = select.getOption(1);
        option.setSelected(true);
        
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page,
                                                                             "submit");
        
        // clicking this should yield no errors.
        submit.click();
        
    }
    
    public void testConverterForUISelectMany() throws Exception {
        HtmlPage page = getPage("/faces/SelectManyConverterTest.jsp");
        List selects = getAllElementsOfGivenClass(page, 
                                                  new ArrayList(), 
                                                  HtmlSelect.class);
        HtmlSelect select = (HtmlSelect) selects.get(0);
        HtmlSelect select2 = (HtmlSelect) selects.get(1);
        select.getOption(1).setSelected(true);
        select.getOption(2).setSelected(true);
        select2.getOption(1).setSelected(true);
        select2.getOption(2).setSelected(true);
        
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page,
                                                                             "submit");
        page = (HtmlPage) submit.click();
        
        // ensure no validator errors
        String pageText = page.asText();
        assertTrue(pageText.indexOf("Value is not valid") < 0);
    }
}
