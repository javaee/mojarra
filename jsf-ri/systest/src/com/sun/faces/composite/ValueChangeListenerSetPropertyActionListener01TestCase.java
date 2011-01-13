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


/**
 * Unit tests for Composite Components.
 */
public class ValueChangeListenerSetPropertyActionListener01TestCase extends AbstractTestCase {


    public ValueChangeListenerSetPropertyActionListener01TestCase() {
        this("ValueChangeListenerSetPropertyActionListener01TestCase");
    }

    public ValueChangeListenerSetPropertyActionListener01TestCase(String name) {
        super(name);
    }


    /**
     * Set up instance variables required by this test case.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ValueChangeListenerSetPropertyActionListener01TestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    @Override
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
    public void testValueChangeActionListener() throws Exception {

        HtmlPage page = getPage("/faces/composite/valueChangeListenerSetPropertyActionListener01.xhtml");
        HtmlInput input = getInputContainingGivenId(page, "form:composite:value");
        input.setValueAttribute("Cause A ValueChangeEvent");
        HtmlSubmitInput button = (HtmlSubmitInput)
                getInputContainingGivenId(page, "form:composite:submit");
        page = button.click();
        String pageText = page.asText();

        assertTrue(-1 != pageText.indexOf("ValueChangeSetPropertyActionListenerBean.processValueChange called"));

        String searchString = "Property set by setPropertyActionListener:";
        int searchStringLength = searchString.length(), i = 0, j = 0;
        long lesser, greater;

        // Get the value of the property set by the setPropertyActionListener
        assertTrue(-1 != (i = pageText.indexOf(searchString)));
        i += searchStringLength;
        assertTrue(-1 != (j = pageText.indexOf(";", i)));
        lesser = Long.valueOf(pageText.substring(i, j));

        // Get the value of the currentTimeMillis at the time the page rendered
        searchString = "System.currentTimeMillis():";
        searchStringLength = searchString.length();
        assertTrue(-1 != (i = pageText.indexOf(searchString)));
        i += searchStringLength;
        assertTrue(-1 != (j = pageText.indexOf(";", i)));
        greater = Long.valueOf(pageText.substring(i, j));

        assertTrue(lesser < greater);

        


    }


}
