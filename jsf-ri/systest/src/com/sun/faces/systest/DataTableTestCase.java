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

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class DataTableTestCase extends AbstractTestCase {

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public DataTableTestCase(String name) {
        super(name);
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(DataTableTestCase.class));
    }


    // ------------------------------------------------------------ Test Methods


    /**
     * Test for https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=774
     */
    public void testVarNotOverrwrittenByNull() throws Exception {

        HtmlPage page = getPage("/faces/standard/dtablevarnotoverwritten.jsp");
        List<HtmlAnchor> links = new ArrayList<HtmlAnchor>(3);
        getAllElementsOfGivenClass(page, links, HtmlAnchor.class);

        // should have three links rendered by the table
        // with their display values being abc, def, ghi in that
        // order *if* the var attribute wasn't overwritten by the tag.
        assertEquals(3, links.size());
        List<String> expectedValues = new ArrayList<String>(3);
        expectedValues.add("abc");
        expectedValues.add("def");
        expectedValues.add("ghi");
        for (int i = 0, len = links.size(); i < len; i++) {
            HtmlAnchor anchor = links.get(i);
            String expectedValue = expectedValues.get(i);
            assertEquals(expectedValue, expectedValue, anchor.asText().trim());
        }

    }


    /**
     * Test regression https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=902.
     * @throws Exception
     */
    public void testRowClasses() throws Exception {

        HtmlPage page = getPage("/faces/standard/dtablerowclasses.jsp");
        assertNotNull(page);
        List<HtmlTable> tableList = new ArrayList<HtmlTable>(1);
        getAllElementsOfGivenClass(page, tableList, HtmlTable.class);
        assertTrue(tableList.size() == 1);
        HtmlTable table = tableList.get(0);
        List<HtmlTableRow> rows = table.getRows();
        assertTrue(rows.size() == 6);
        for (int i = 0, len = rows.size(); i < len; i++) {
            HtmlTableRow row = rows.get(i);
            if (i % 2 == 0) {
                assertTrue(row.getClassAttribute().equals("b1"));
            } else {
                assertTrue(row.getClassAttribute().equals("b2"));
            }
        }
        
    }
}
