package com.sun.faces.render;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;

public class SelectStarTestCase extends AbstractTestCase {

    public SelectStarTestCase(String name) {
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
        return (new TestSuite(SelectStarTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testSelectStarXhtml() throws Exception {
        String failMsg;

        getPage("/faces/render/selectStarNoSelection.xhtml");
        System.out.println("Start select star test case - facelets");

        // Check SelectManyListbox
        HtmlSelect selectOneList = (HtmlSelect) lastpage.getHtmlElementById("selectOneListbox");
        List<HtmlOption> selectOneListOptions = selectOneList.getOptions();
        failMsg = "Wrong number of options for SelectManyListbox, expected 5, got "+selectOneListOptions.size();
        assertTrue(failMsg,selectOneListOptions.size() == 5);
        HtmlOption selectOption = selectOneListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectOneListOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneListbox
        HtmlSelect selectOneMenu = (HtmlSelect) lastpage.getHtmlElementById("selectOneMenu");
        List<HtmlOption> selectOneMenuOptions = selectOneMenu.getOptions();
        assertTrue(selectOneMenuOptions.size() == 5);
        selectOption = selectOneMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectOneMenuOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneRadio
        HtmlInput selectOneRadio0 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:0");
        assertTrue(selectOneRadio0.getValueAttribute().equals("noSelection"));
        HtmlInput selectOneRadio1 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:1");
        assertTrue(selectOneRadio1.getValueAttribute().equals("Apple"));

        // Check SelectManyCheckbox
        HtmlInput selectManyCheckbox0 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:0");
        assertTrue(selectManyCheckbox0.getValueAttribute().equals("noSelection"));
        HtmlInput selectManyCheckbox1 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:1");
        assertTrue(selectManyCheckbox1.getValueAttribute().equals("Apple"));

        // Check SelectManyListbox
        HtmlSelect selectManyListbox = (HtmlSelect) lastpage.getHtmlElementById("selectManyListbox");
        List<HtmlOption> selectManyListOptions = selectManyListbox.getOptions();
        assertTrue(selectManyListOptions.size() == 5);
        selectOption = selectManyListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectManyListOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectManyMenu
        HtmlSelect selectManyMenu = (HtmlSelect) lastpage.getHtmlElementById("selectManyMenu");
        List<HtmlOption> selectManyMenuOptions = selectManyMenu.getOptions();
        assertTrue(selectManyMenuOptions.size() == 5);
        selectOption = selectManyMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectManyMenuOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));
    }

    public void testSelectStarJspx() throws Exception {
        String failMsg;

        getPage("/faces/render/selectStarNoSelection.jspx");
        System.out.println("Start select star test case - jspx");

        // Check SelectManyListbox
        HtmlSelect selectOneList = (HtmlSelect) lastpage.getHtmlElementById("selectOneListbox");
        List<HtmlOption> selectOneListOptions = selectOneList.getOptions();
        failMsg = "Wrong number of options for SelectManyListbox, expected 5, got "+selectOneListOptions.size();
        assertTrue(failMsg,selectOneListOptions.size() == 5);
        HtmlOption selectOption = selectOneListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectOneListOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneListbox
        HtmlSelect selectOneMenu = (HtmlSelect) lastpage.getHtmlElementById("selectOneMenu");
        List<HtmlOption> selectOneMenuOptions = selectOneMenu.getOptions();
        assertTrue(selectOneMenuOptions.size() == 5);
        selectOption = selectOneMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectOneMenuOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneRadio
        HtmlInput selectOneRadio0 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:0");
        assertTrue(selectOneRadio0.getValueAttribute().equals("noSelection"));
        HtmlInput selectOneRadio1 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:1");
        assertTrue(selectOneRadio1.getValueAttribute().equals("Apple"));

        // Check SelectManyCheckbox
        HtmlInput selectManyCheckbox0 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:0");
        assertTrue(selectManyCheckbox0.getValueAttribute().equals("noSelection"));
        HtmlInput selectManyCheckbox1 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:1");
        assertTrue(selectManyCheckbox1.getValueAttribute().equals("Apple"));

        // Check SelectManyListbox
        HtmlSelect selectManyListbox = (HtmlSelect) lastpage.getHtmlElementById("selectManyListbox");
        List<HtmlOption> selectManyListOptions = selectManyListbox.getOptions();
        assertTrue(selectManyListOptions.size() == 5);
        selectOption = selectManyListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectManyListOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectManyMenu
        HtmlSelect selectManyMenu = (HtmlSelect) lastpage.getHtmlElementById("selectManyMenu");
        List<HtmlOption> selectManyMenuOptions = selectManyMenu.getOptions();
        assertTrue(selectManyMenuOptions.size() == 5);
        selectOption = selectManyMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectManyMenuOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

    }

    public void testSelectStarXhtmlHide() throws Exception {
        String failMsg;

        getPage("/faces/render/selectStarSelectionHideNoSelection.xhtml");
        System.out.println("Start select star test case - facelets");

        // Check SelectManyListbox
        HtmlSelect selectOneList = (HtmlSelect) lastpage.getHtmlElementById("selectOneListbox");
        List<HtmlOption> selectOneListOptions = selectOneList.getOptions();
        failMsg = "Wrong number of options for SelectManyListbox, expected 4, got "+selectOneListOptions.size();
        assertTrue(failMsg,selectOneListOptions.size() == 4);
        HtmlOption selectOption = selectOneListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneListbox
        HtmlSelect selectOneMenu = (HtmlSelect) lastpage.getHtmlElementById("selectOneMenu");
        List<HtmlOption> selectOneMenuOptions = selectOneMenu.getOptions();
        assertTrue(selectOneMenuOptions.size() == 4);
        selectOption = selectOneMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneRadio
        HtmlInput selectOneRadio0 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:1");
        assertTrue(selectOneRadio0.getValueAttribute().equals("Apple"));

        // Check SelectManyCheckbox
        HtmlInput selectManyCheckbox0 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:1");
        assertTrue(selectManyCheckbox0.getValueAttribute().equals("Apple"));

        // Check SelectManyListbox
        HtmlSelect selectManyListbox = (HtmlSelect) lastpage.getHtmlElementById("selectManyListbox");
        List<HtmlOption> selectManyListOptions = selectManyListbox.getOptions();
        assertTrue(selectManyListOptions.size() == 4);
        selectOption = selectManyListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectManyMenu
        HtmlSelect selectManyMenu = (HtmlSelect) lastpage.getHtmlElementById("selectManyMenu");
        List<HtmlOption> selectManyMenuOptions = selectManyMenu.getOptions();
        assertTrue(selectManyMenuOptions.size() == 4);
        selectOption = selectManyMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));
    }


}