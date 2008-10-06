/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

import java.util.List;
import java.util.ArrayList;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import junit.framework.Test;
import junit.framework.TestSuite;


public class SelectManyCollectionTestCase extends AbstractTestCase {


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public SelectManyCollectionTestCase(String name) {
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
        return (new TestSuite(SelectManyCollectionTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    public void testSelectManyCollections() throws Exception {

        HtmlPage page = getPage("/faces/standard/selectmany05.xhtml");
        String[] selectIds = {
              "array",
              "list",
              "set",
              "sortedset",
              "collection",
              "ilist",
              "iset",
              "isortedset",
              "icollection",
              "hintString",
              "hintClass",
              "object"
        };
        String[][] initialSelections = {
              new String[0],
              new String[0],
              new String[0],
              new String[0],
              new String[0],
              new String[] { "Bilbo", "Pippin", "Merry" },
              new String[] { "Frodo" },
              new String[] { "Pippin", "Frodo" },
              new String[] { "Bilbo", "Merry" },
              new String[0],
              new String[0],
              new String[0]
        };

        String[][] postBackSelections = {
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" },
              new String[] { "Bilbo" }
        };

        // =====================================================================
        //  Validate initial page state
        //
        List<HtmlSelect> selects = new ArrayList<HtmlSelect>(12);
        getAllElementsOfGivenClass(page, selects, HtmlSelect.class);
        assertTrue(selects.size() == 12);
        for (int i = 0; i < selectIds.length; i++) {
            String id = selectIds[i];
            System.out.println("Validating HtmlSelect with ID: " + id);
            String[] initialSelection = initialSelections[i];
            String[] newSelection = postBackSelections[i];
            HtmlSelect select = getHtmlSelectForId(selects, id);
            assertNotNull(select);
            validateState(select, initialSelection);
            updateSelections(select, newSelection);
        }

        HtmlInput input = getInputContainingGivenId(page, "command");
        page = (HtmlPage) input.click();

        // ensure no messages were queued by the post-back
        assertTrue(!page.asText().contains("Error:"));

        selects.clear();
        getAllElementsOfGivenClass(page, selects, HtmlSelect.class);
        assertTrue(selects.size() == 12);
        for (int i = 0; i < selectIds.length; i++) {
            String id = selectIds[i];
            String[] newSelection = postBackSelections[i];
            HtmlSelect select = getHtmlSelectForId(selects, id);
            assertNotNull(select);
            validateState(select, newSelection);
        }
        
    }


    // --------------------------------------------------------- Private Methods

    private void updateSelections(HtmlSelect select, String[] selectedOptions) {

        assertNotNull(select);
        List<HtmlOption> options = select.getOptions();
        assertTrue(options.size() == 4);
        for (String s : selectedOptions) {
            for (HtmlOption option : options) {
                option.setSelected(s.equals(option.asText()));
            }
        }
    }


    private void validateState(HtmlSelect select, String[] selectedOptions) {

        assertNotNull(select);
        List<HtmlOption> options = select.getOptions();
        assertTrue(options.size() == 4);
        if (selectedOptions == null || selectedOptions.length == 0) {
            for (HtmlOption option : options) {
                assertTrue(!option.isSelected());
            }
        } else {
            for (String text : selectedOptions) {
                for (HtmlOption option : options) {
                    if (text.equals(option.asText())) {
                        assertTrue(option.isSelected());
                    } 
                }
            }
        }

    }


    private HtmlSelect getHtmlSelectForId(List<HtmlSelect> selects, String id) {

        for (HtmlSelect select : selects) {
            if (select.getId().contains(id)) {
                return select;
            }
        }
        
        return null;

    }

}
