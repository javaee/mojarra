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