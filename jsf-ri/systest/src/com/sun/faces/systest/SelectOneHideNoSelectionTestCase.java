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

package com.sun.faces.systest;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.sun.faces.htmlunit.AbstractTestCase;


public class SelectOneHideNoSelectionTestCase extends AbstractTestCase {


    private HtmlPage page;

	/**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public SelectOneHideNoSelectionTestCase(String name) {
        super(name);
    }


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
        this.page = getPage(getPath());
    }


	protected String getPath() {
		return "/faces/standard/selectOneLiteralHideNoSelectionOption.xhtml";
	}


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(SelectOneHideNoSelectionTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    public void testSelectItemsWithSelectedValueDoesntShowNoSelectionOption() throws Exception {
        HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemsSelectedValue");
        assertEquals(4, select.getOptionSize());
    }
    
    public void testSelectItemsWithNoSelectedValueShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemsNoSelectedValue");
    	assertEquals(5, select.getOptionSize());
    }
    
    public void testSelectItemsWithNoSelectionSelectedValueShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemsNoSelectionSelectedValue");
    	assertEquals(5, select.getOptionSize());
    }
    
    public void testSelectItemWithSelectedValueDoesntShowNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemSelectedValue");
    	assertEquals(4, select.getOptionSize());
    }
    
    //same test as testSelectItemWithSelectedValueDoesntShowNoSelectionOption, but now the
    //no selection option is put as last option in the selectOneMenu
    public void testSelectItemAsLastWithSelectedValueDoesntShowNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemSelectedValueLast");
    	assertEquals(4, select.getOptionSize());
    }
    
    public void testSelectItemWithNoSelectedValueShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemNoSelectedValue");
    	assertEquals(5, select.getOptionSize());
    }
    
    public void testSelectItemWithNoSelectionSelectedValueShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemNoSelectionSelectedValue");
    	assertEquals(5, select.getOptionSize());
    }
    
    
    public void testSelectItemsWithSelectedValueNoHidingShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemsSelectedValueNoHiding");
    	assertEquals(5, select.getOptionSize());
    }
    
    public void testSelectItemsWithNoSelectedValueNoHidingShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemsNoSelectedValueNoHiding");
    	assertEquals(5, select.getOptionSize());
    }
    
    public void testSelectItemsWithNoSelectionSelectedValueNoHidingShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemsNoSelectionSelectedValueNoHiding");
    	assertEquals(5, select.getOptionSize());
    }
    
    public void testSelectItemWithSelectedValueNoHidingShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemSelectedValueNoHiding");
    	assertEquals(5, select.getOptionSize());
    }
    
    public void testSelectItemWithNoSelectedValueNoHidingShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemNoSelectedValueNoHiding");
    	assertEquals(5, select.getOptionSize());
    }
    
    public void testSelectItemWithNoSelectionSelectedValueNoHidingShowsNoSelectionOption() throws Exception {
    	HtmlSelect select = (HtmlSelect) this.page.getElementById("f:selectItemNoSelectionSelectedValueNoHiding");
    	assertEquals(5, select.getOptionSize());
    }

}
