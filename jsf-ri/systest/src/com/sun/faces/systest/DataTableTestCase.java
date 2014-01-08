/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

import java.util.List;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import com.gargoylesoftware.htmlunit.html.*;

public class DataTableTestCase extends HtmlUnitFacesTestCase {

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


    /*
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


    /*
     * Test regression https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=902.
     */
    public void testRowClasses() throws Exception {

        HtmlPage page = getPage("/faces/standard/dtablerowclasses.jsp");
        assertNotNull(page);
        List<HtmlTable> tableList = new ArrayList<HtmlTable>(1);
        getAllElementsOfGivenClass(page, tableList, HtmlTable.class);
        assertTrue(tableList.size() == 1);
        HtmlTable table = tableList.get(0);
        List<HtmlTableRow> rows = table.getRows();
        assertEquals(6,rows.size());
        for (int i = 0, len = rows.size(); i < len; i++) {
            HtmlTableRow row = rows.get(i);
            if (i % 2 == 0) {
            	assertEquals("b1",row.getClassAttribute());
            } else {
            	assertEquals("b2",row.getClassAttribute());
            }
        }

    }

    // For Issue 2066: Assert rowHeader attribute has been rendered correctly.

    public void testRowsWithRowHeader() throws Exception {
        HtmlPage page = getPage("/faces/standard/dtablerowclasses.jsp");
        assertNotNull(page);
        List<HtmlTable> tableList = new ArrayList<HtmlTable>(1);
        getAllElementsOfGivenClass(page, tableList, HtmlTable.class);
        assertTrue(tableList.size() == 1);
        HtmlTable table = tableList.get(0);
        List<HtmlTableRow> rows = table.getRows();
        for (int i = 0, len = rows.size(); i < len; i++) {
            HtmlTableRow row = rows.get(i);
            for (final HtmlTableCell cell : row.getCells()) {
                assertEquals("row", cell.getAttribute("scope"));
            }
        }
    }

    public void testTablesWithEmptyBody() throws Exception {

        HtmlPage page = getPage("/faces/standard/dtablewithemptybody.jsp");
        String xml = page.asText();
        System.out.println(xml);
        assertNotNull(page);

        assertEmptyTable("Empty", page, false);

        HtmlTable table = (HtmlTable) page.getElementById("Some");
        assertNotNull(table);
        HtmlTableHeader header = table.getHeader();
        HtmlTableRow row = assertSingle(header.getRows());
        HtmlTableCell cell = assertSingle(row.getCells());
        assertFalse(cell.hasAttribute("colspan"));
        HtmlTableFooter footer = table.getFooter();
        row = assertSingle(footer.getRows());
        cell = assertSingle(row.getCells());
        assertFalse(cell.hasAttribute("colspan"));
        HtmlTableBody body = assertSingle(table.getBodies());
        assertSingle(body.getRows());
    }

    /**
     * tests that a table with the given id exists, and that it is rendered as
     * &lt;table>
     * <tr>
     * <td></td>
     * </tr>
     * </table>
     *
     * @param tableId
     *            the id of the table
     * @param page
     *            the page to lookup the table in
     */
    private static void assertEmptyTable(final String tableId,
            final HtmlPage page, final boolean hasHeader) {
        HtmlTable table = (HtmlTable) page.getElementById(tableId);
        assertNotNull("Should find Table with ID: " + tableId, table);
        // Test that we have only one row at all
        List<HtmlTableRow> allRows = table.getRows();
        int expectedRowCount = hasHeader ? 2 : 1;
        assertEquals("Table " + tableId
                + " should have " + expectedRowCount
                + " row(s)", expectedRowCount,
                allRows.size());
        // test that we have <tbody><tr>...</tr></tbody>
        HtmlTableBody body = assertSingle(tableId + "should have one tbody",
                table.getBodies());
        HtmlTableRow row = assertSingle(tableId + ":tbody should have one tr",
                body.getRows());
        assertTrue(row.getCells().size() == 2);
    }

    private static <T> T assertSingle(final String msg, final List<T> input) {
        assertEquals(msg, 1, input.size());
        return input.get(0);
    }

    private static <T> T assertSingle(final List<T> input) {
        assertEquals(1, input.size());
        return input.get(0);
    }

    public void testTableForms() throws Exception {
        getPage("/faces/standard/dtablemultiforms.jsp");
        checkTrue("out","");

        HtmlTextInput in0 = (HtmlTextInput) lastpage.getHtmlElementById("table:0:columnform:columninput");
        in0.setValueAttribute("test0");

        HtmlSubmitInput button0 = (HtmlSubmitInput) lastpage.getHtmlElementById("table:0:columnform:columnbutton");
        lastpage = (HtmlPage) button0.click();

        checkTrue("out","test0");

        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("table:1:columnform:columninput");
        in1.setValueAttribute("test1");

        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("table:1:columnform:columnbutton");
        lastpage = (HtmlPage) button1.click();

        checkTrue("out","test1");

        HtmlTextInput in2 = (HtmlTextInput) lastpage.getHtmlElementById("table:2:columnform:columninput");
        in2.setValueAttribute("test2");

        HtmlSubmitInput button2 = (HtmlSubmitInput) lastpage.getHtmlElementById("table:2:columnform:columnbutton");
        lastpage = (HtmlPage) button2.click();

        checkTrue("out","test2");

        HtmlTextInput finalin = (HtmlTextInput) lastpage.getHtmlElementById("finalform:finalinput");
        finalin.setValueAttribute("testfinal");

        HtmlSubmitInput finalbutton = (HtmlSubmitInput) lastpage.getHtmlElementById("finalform:finalbutton");
        lastpage = (HtmlPage) finalbutton.click();

        checkTrue("out","testfinal");

    }
}
