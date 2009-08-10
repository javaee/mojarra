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

package com.sun.faces.demotest.treevisit;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.sun.faces.demotest.HtmlUnitTestCase;


public class TestTreeVisit extends HtmlUnitTestCase {


    public void testTreeVisitSelectList() throws Exception {
        String welcomeTitle = "Tree Visit";

        HtmlPage greetingPage = (HtmlPage) getInitialPage();

        assertTrue(greetingPage.getTitleText().equals(welcomeTitle));

        HtmlSelect select = (HtmlSelect) greetingPage.getElementById("form:componentsList");

        assertTrue("wrong number of options: "+select.getOptionSize(), select.getOptionSize() == 48);
        
        assertTrue(select.getOption(0).getValueAttribute().equals("form:mainGrid"));
        assertTrue(select.getOption(20).getValueAttribute().equals("form:testTable"));
        assertTrue(select.getOption(21).getValueAttribute().equals("form:testTable:labelColumnHeader"));
        assertTrue(select.getOption(30).getValueAttribute().equals("form:testTable:actionsColumn"));
        assertTrue(select.getOption(31).getValueAttribute().equals("form:testTable:actionsColumnHeader"));
        assertTrue(select.getOption(32).getValueAttribute().equals("form:testTable:incrementLink"));
        assertTrue(select.getOption(33).getValueAttribute().equals("form:testTable:0:labelText"));
        assertTrue(select.getOption(34).getValueAttribute().equals("form:testTable:0:countText"));
        assertTrue(select.getOption(35).getValueAttribute().equals("form:testTable:0:incrementLink"));
        assertTrue(select.getOption(36).getValueAttribute().equals("form:testTable:1:labelText"));


    }

}

/*
<select id="form:componentsList" name="form:componentsList" multiple="multiple" size="10">
	<option value="form:mainGrid">form:mainGrid</option>
	<option value="form:instructions">form:instructions</option>
	<option value="form:componentsListLabel">form:componentsListLabel</option>
	<option value="form:componentsList">form:componentsList</option>
	<option value="form:j_id1283414979_4c7f5b34">form:j_id1283414979_4c7f5b34</option>
	<option value="form:visitRenderedGroup">form:visitRenderedGroup</option>
	<option value="form:visitRenderedCheckbox">form:visitRenderedCheckbox</option>
	<option value="form:visitRenderedLabel">form:visitRenderedLabel</option>
	<option value="form:visitButton">form:visitButton</option>
	<option value="form:namingContainers">form:namingContainers</option>
	<option value="form:subviews">form:subviews</option>
	<option value="form:subviewA">form:subviewA</option>
	<option value="form:subviewA:groupA">form:subviewA:groupA</option>
	<option value="form:subviewA:textA">form:subviewA:textA</option>
	<option value="form:subviewB">form:subviewB</option>
	<option value="form:subviewB:groupB">form:subviewB:groupB</option>
	<option value="form:subviewB:textB">form:subviewB:textB</option>
	<option value="form:nonRenderedSubview">form:nonRenderedSubview</option>
	<option value="form:nonRenderedSubview:nonRenderedGroup">form:nonRenderedSubview:nonRenderedGroup</option>
	<option value="form:nonRenderedSubview:nonRenderedText">form:nonRenderedSubview:nonRenderedText</option>
	<option value="form:testTable">form:testTable</option>
	<option value="form:testTable:labelColumnHeader">form:testTable:labelColumnHeader</option>
	<option value="form:testTable:countColumnHeader">form:testTable:countColumnHeader</option>
	<option value="form:testTable:actionsColumnHeader">form:testTable:actionsColumnHeader</option>
	<option value="form:testTable:labelColumn">form:testTable:labelColumn</option>
	<option value="form:testTable:labelColumnHeader">form:testTable:labelColumnHeader</option>
	<option value="form:testTable:labelText">form:testTable:labelText</option>
	<option value="form:testTable:countColumn">form:testTable:countColumn</option>
	<option value="form:testTable:countColumnHeader">form:testTable:countColumnHeader</option>
	<option value="form:testTable:countText">form:testTable:countText</option>
	<option value="form:testTable:actionsColumn">form:testTable:actionsColumn</option>
	<option value="form:testTable:actionsColumnHeader">form:testTable:actionsColumnHeader</option>
	<option value="form:testTable:incrementLink">form:testTable:incrementLink</option>
	<option value="form:testTable:0:labelText">form:testTable:0:labelText</option>
	<option value="form:testTable:0:countText">form:testTable:0:countText</option>
	<option value="form:testTable:0:incrementLink">form:testTable:0:incrementLink</option>
	<option value="form:testTable:1:labelText">form:testTable:1:labelText</option>
	<option value="form:testTable:1:countText">form:testTable:1:countText</option>
	<option value="form:testTable:1:incrementLink">form:testTable:1:incrementLink</option>
	<option value="form:testTable:2:labelText">form:testTable:2:labelText</option>
	<option value="form:testTable:2:countText">form:testTable:2:countText</option>
	<option value="form:testTable:2:incrementLink">form:testTable:2:incrementLink</option>
	<option value="form:testTable:3:labelText">form:testTable:3:labelText</option>
	<option value="form:testTable:3:countText">form:testTable:3:countText</option>
	<option value="form:testTable:3:incrementLink">form:testTable:3:incrementLink</option>
	<option value="form:testTable:4:labelText">form:testTable:4:labelText</option>
	<option value="form:testTable:4:countText">form:testTable:4:countText</option>
	<option value="form:testTable:4:incrementLink">form:testTable:4:incrementLink</option>
</select>
*/