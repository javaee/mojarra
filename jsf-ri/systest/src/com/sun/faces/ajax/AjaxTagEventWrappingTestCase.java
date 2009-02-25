package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;

public class AjaxTagEventWrappingTestCase extends AbstractTestCase {

    public AjaxTagEventWrappingTestCase(String name) {
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
        return (new TestSuite(AjaxTagEventWrappingTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testAjaxTagEventWrapping() throws Exception {
        getPage("/faces/ajax/ajaxTagEventWrapping.xhtml");
        System.out.println("Start ajax tag event wrapping test");

        // Check initial values
        assertTrue(check("out1","0"));
        assertTrue(check("say","init"));
        assertTrue(check("paramOut",""));
        assertTrue(check("out2","1"));

        // Press Count
        HtmlSubmitInput button = (HtmlSubmitInput) lastpage.getHtmlElementById("button1");
        lastpage = (HtmlPage) button.click();

        assertTrue(check("out1","2"));
        assertTrue(check("out2","1"));

        // Press Say
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button2");
        lastpage = (HtmlPage) button.click();

        assertTrue(check("say","1"));
        assertTrue(check("out1","2"));
        assertTrue(check("out2","1"));

        // Press Count and Say
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button3");
        lastpage = (HtmlPage) button.click();

        assertTrue(check("say","2"));
        assertTrue(check("out1","3"));
        assertTrue(check("out2","1"));

        // Press Param
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button4");
        lastpage = (HtmlPage) button.click();

        assertTrue(check("say","init"));
        assertTrue(check("out1","4"));
        assertTrue(check("out2","5"));
        assertTrue(check("paramOut","testval"));

        // Reset Page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reset");
        lastpage = (HtmlPage) button.click();
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reload");
        lastpage = (HtmlPage) button.click();

        // Check initial values
        assertTrue(check("out1","0"));
        assertTrue(check("say","init"));
        assertTrue(check("paramOut",""));
        assertTrue(check("out2","1"));

        // Press Count and Param
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button5");
        lastpage = (HtmlPage) button.click();

        assertTrue(check("out1","2"));
        assertTrue(check("say","init"));
        assertTrue(check("paramOut","testval"));
        assertTrue(check("out2","1"));

        // Reset Page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reset");
        lastpage = (HtmlPage) button.click();
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reload");
        lastpage = (HtmlPage) button.click();

        // Check initial values
        assertTrue(check("out1","0"));
        assertTrue(check("say","init"));
        assertTrue(check("paramOut",""));
        assertTrue(check("out2","1"));

        // Press Count and Say and Param
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("button6");
        lastpage = (HtmlPage) button.click();

        assertTrue(check("out1","2"));
        assertTrue(check("say","1"));
        assertTrue(check("paramOut","testval"));
        assertTrue(check("out2","1"));

        // leaving out button 7

        // Reset Page
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reset");
        lastpage = (HtmlPage) button.click();
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("reload");
        lastpage = (HtmlPage) button.click();

        // Check initial values
        assertTrue(check("out1","0"));
        assertTrue(check("say","init"));
        assertTrue(check("paramOut",""));
        assertTrue(check("out2","1"));


        // Check ajax checkbox
//        HtmlCheckBoxInput checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox1"));
//        lastpage = (HtmlPage)checked.setChecked(true);

//        System.out.println(getText("checkedvalue1"));
//        assertTrue(check("checkedvalue1","true"));
//        assertTrue(check("out2","1"));

        // Check ajax + userwrap checkbox
//        checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox2"));
//        lastpage = (HtmlPage)checked.setChecked(true);

//        assertTrue(check("checkedvalue2","true"));
//        assertTrue(check("say","1"));
//        assertTrue(check("out2","1"));

        // Check user onchange checkbox
        HtmlCheckBoxInput checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox3"));
        lastpage = (HtmlPage)checked.setChecked(true);

        assertTrue(check("checkedvalue3","false"));
//        assertTrue(check("say","2"));
//        assertTrue(check("out2","1"));

    }

}
