/*
 * $Id: UniqueViewIdTestCase.java,v 1.1.32.3 2007/04/27 21:28:15 ofung Exp $
 */

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

package com.sun.faces.systest;


import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.List;

/**
 * <p>Make sure that only unique view ids are saved in the session</p>
 */

public class UniqueViewIdTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UniqueViewIdTestCase(String name) {
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
        return (new TestSuite(UniqueViewIdTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables



    // ------------------------------------------------- Individual Test Methods

    /**
     *
     * <p>See that clicking the re-submit button 20 times doesn't
     * increment the view counter.</p>
     */

    public void testReSubmitDoesNotIncrementCounter() throws Exception {
	HtmlPage page = getPage("/faces/reset-statemanager.jsp");
        page = getPage("/faces/test.jsp");
	List list;
	HtmlSubmitInput button = null;
	for (int i = 0; i < 20; i++) {
	    list = getAllElementsOfGivenClass(page, null, 
					      HtmlSubmitInput.class); 
	    button = (HtmlSubmitInput) list.get(0);
	    page = (HtmlPage) button.click();
	}
        HtmlElement element = page.getHtmlElementById("com.sun.faces.VIEW");
        assertTrue(element instanceof HtmlHiddenInput);
        HtmlHiddenInput hidden = (HtmlHiddenInput) element;
        String value = hidden.getValueAttribute();
        assertTrue(value.startsWith("_id2:"));
    }

    /**
     *
     * <p>See that clicking to a new page and back again does increment
     * the view counter.</p>
     */

    public void testNewPageDoesIncrementCounter() throws Exception {
	HtmlPage page = getPage("/faces/reset-statemanager.jsp");
        page = getPage("/faces/test.jsp");
	List list;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(1);
	page = (HtmlPage) button.click();

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

        HtmlElement element = page.getHtmlElementById("com.sun.faces.VIEW");
        assertTrue(element instanceof HtmlHiddenInput);
        HtmlHiddenInput hidden = (HtmlHiddenInput) element;
        String value = hidden.getValueAttribute();
        assertEquals("_id2:_id5", value);

    }


}
