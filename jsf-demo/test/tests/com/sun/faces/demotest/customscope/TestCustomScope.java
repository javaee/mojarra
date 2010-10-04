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

package com.sun.faces.demotest.customscope;

import com.sun.faces.demotest.HtmlUnitTestCase;
import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;
import java.util.ArrayList;

public class TestCustomScope extends HtmlUnitTestCase {

    public void testCustomScope() throws Exception {

        HtmlPage page = getPage("/custom.xhtml");
        assertNotNull(page);
        validateSpanContent(page);
        validateTableStatus(page, true, false);

        HtmlSubmitInput reload = getReloadButton(page);
        page = reload.click();
        validateSpanContent(page);
        validateTableStatus(page, false, false);

        HtmlSubmitInput destroyScope = getDestoryScopeButton(page);
        page = destroyScope.click();
        validateSpanContent(page);
        validateTableStatus(page, true, true);

    }


    // --------------------------------------------------------- Private Methods


    private HtmlSubmitInput getReloadButton(HtmlPage page) {

        HtmlSubmitInput input = (HtmlSubmitInput) getInputContainingGivenId(page, "form:reload");
        assertNotNull(input);
        return input;

    }


    private HtmlSubmitInput getDestoryScopeButton(HtmlPage page) {

        HtmlSubmitInput input = (HtmlSubmitInput) getInputContainingGivenId(page, "form:destroy");
        assertNotNull(input);
        return input;

    }


    private void validateTableStatus(HtmlPage page,
                                     boolean postConstructStatus,
                                     boolean preDestroyStatus) {

        HtmlTable table = (HtmlTable) page.getElementById("grid");
        assertNotNull(table);
        HtmlTableRow pcRow = table.getRow(1);
        HtmlTableCell pcStatus = pcRow.getCell(1);
        assertTrue(((postConstructStatus)
                    ? "Invoked".equals(pcStatus.asText())
                    : pcStatus.asText().length() == 0));
        HtmlTableRow pdRow = table.getRow(2);
        HtmlTableCell pdStatus = pdRow.getCell(1);
        assertTrue(((preDestroyStatus) ? "Invoked".equals(pdStatus.asText()) : pdStatus.asText().length() == 0));
        
    }


    private void validateSpanContent(HtmlPage page) {

        List<HtmlSpan> spans = new ArrayList<HtmlSpan>(3);
        getAllElementsOfGivenClass(page, spans, HtmlSpan.class);
        HtmlSpan span = spans.get(0);
        assertEquals("create", span.getId());
        assertEquals("Resolved", span.asText());
        span = spans.get(1);
        assertEquals("scopeReference", span.getId());
        assertEquals("Resolved", span.asText());
        span = spans.get(2);
        assertEquals("nonCreate", span.getId());
        assertEquals("Resolved", span.asText());

    }
}
