package com.sun.faces.systest;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Test h:link and h:button.
 */
public class TestOutputTargets extends AbstractTestCase {

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public TestOutputTargets(String name) {
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
        return (new TestSuite(TestOutputTargets.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    public void testOutputTargetButton() throws Exception {
        HtmlPage page = getPage("/faces/standard/outcometarget01.xhtml");
        // get the page twice to avoid jsession ID encoding in the results
        page = getPage("/faces/standard/outcometarget01.xhtml");
        assertNotNull(page);

        List<HtmlInput> buttonList = new ArrayList<HtmlInput>(7);
        getAllElementsOfGivenClass(page, buttonList, HtmlInput.class);
        assertTrue(buttonList.size() == 7);

        HtmlInput button = buttonList.get(0);
        String onclick = button.getOnClickAttribute();
        assertEquals("button", button.getTypeAttribute());
        assertEquals("button1", button.getIdAttribute());
        assertEquals("window.location.href='/jsf-systest/faces/standard/outcometarget01.xhtml'; return false;",
                     onclick);
        page = button.click();
        assertEquals("outcometarget01", page.getTitleText());

        // ---------------------------------------------------------------------

        button = buttonList.get(1);
        onclick = button.getOnClickAttribute();
        assertEquals("button", button.getTypeAttribute());
        assertEquals("button2", button.getIdAttribute());
        assertEquals("window.location.href='/jsf-systest/faces/standard/nav1.xhtml'; return false;", onclick);
        page = button.click();
        assertEquals("nav1", page.getTitleText());
        page = getPage("/faces/standard/outcometarget01.xhtml");

        // ---------------------------------------------------------------------

        button = buttonList.get(2);
        onclick = button.getOnClickAttribute();
        assertEquals("button", button.getTypeAttribute());
        assertEquals("button3", button.getIdAttribute());
        assertEquals("window.location.href='/jsf-systest/faces/standard/outcometarget01.xhtml?id=page&id2=view#about'; return false;",
                     onclick);
        page = button.click();
        assertEquals("outcometarget01", page.getTitleText());

        // ---------------------------------------------------------------------

        button = buttonList.get(3);
        onclick = button.getOnClickAttribute();
        assertEquals("button", button.getTypeAttribute());
        assertEquals("button4", button.getIdAttribute());
        assertEquals("alert('foo'); window.location.href='/jsf-systest/faces/standard/nav2.xhtml'; return false;", onclick);
        page = button.click();
        assertEquals("nav2", page.getTitleText());

        // ---------------------------------------------------------------------

        button = buttonList.get(4);
        onclick = button.getOnClickAttribute();
        assertEquals("button", button.getTypeAttribute());
        assertEquals("button5", button.getIdAttribute());
        assertEquals("window.location.href='/jsf-systest/faces/standard/outcometarget01.xhtml?id=page'; return false;", onclick);
        page = button.click();
        assertEquals("outcometarget01", page.getTitleText());

        // ---------------------------------------------------------------------

        button = buttonList.get(5);
        onclick = button.getOnClickAttribute();
        assertEquals("button", button.getTypeAttribute());
        assertEquals("button6", button.getIdAttribute());
        assertEquals("window.location.href='/jsf-systest/faces/standard/outcometarget01.xhtml?id=page&id2=view'; return false;", onclick);
        page = button.click();
        assertEquals("outcometarget01", page.getTitleText());

        // ---------------------------------------------------------------------

        button = buttonList.get(6);
        onclick = button.getOnClickAttribute();
        assertEquals("button", button.getTypeAttribute());
        assertEquals("button7", button.getIdAttribute());
        assertEquals("window.location.href='/jsf-systest/faces/standard/outcometarget01.xhtml?id=config&id2=view'; return false;", onclick);
        page = button.click();
        assertEquals("outcometarget01", page.getTitleText());

    }


    public void testOutputTargetLink() throws Exception {

        HtmlPage page = getPage("/faces/standard/outcometarget01.xhtml");
        // get the page twice to avoid jsession ID encoding in the results
        page = getPage("/faces/standard/outcometarget01.xhtml");
        assertNotNull(page);

        List<HtmlAnchor> linkList = new ArrayList<HtmlAnchor>(7);
        getAllElementsOfGivenClass(page, linkList, HtmlAnchor.class);
        assertTrue(linkList.size() == 7);

        HtmlAnchor link = linkList.get(0);
        String onclick = link.getOnClickAttribute();
        assertEquals("link1", link.getIdAttribute());
        assertEquals("", onclick);
        assertEquals("/jsf-systest/faces/standard/outcometarget01.xhtml", link.getHrefAttribute());
        page = link.click();
        assertEquals("outcometarget01", page.getTitleText());

        // ---------------------------------------------------------------------

        link = linkList.get(1);
        onclick = link.getOnClickAttribute();
        assertEquals("link2", link.getIdAttribute());
        assertEquals("", onclick);
        assertEquals("/jsf-systest/faces/standard/nav1.xhtml", link.getHrefAttribute());
        page = link.click();
        assertEquals("nav1", page.getTitleText());
        page = getPage("/faces/standard/outcometarget01.xhtml");

        // ---------------------------------------------------------------------

        link = linkList.get(2);
        onclick = link.getOnClickAttribute();
        assertEquals("link3", link.getIdAttribute());
        assertEquals("", onclick);
        assertEquals("/jsf-systest/faces/standard/outcometarget01.xhtml?id=page&id2=view#about",
                     link.getHrefAttribute());
        page = link.click();
        assertEquals("outcometarget01", page.getTitleText());

        // ---------------------------------------------------------------------

        link = linkList.get(3);
        onclick = link.getOnClickAttribute();
        assertEquals("link4", link.getIdAttribute());
        assertEquals("alert('foo');", onclick);
        assertEquals("/jsf-systest/faces/standard/nav2.xhtml", link.getHrefAttribute());
        page = link.click();
        assertEquals("nav2", page.getTitleText());

        // ---------------------------------------------------------------------

        link = linkList.get(4);
        onclick = link.getOnClickAttribute();
        assertEquals("link5", link.getIdAttribute());
        assertEquals("/jsf-systest/faces/standard/outcometarget01.xhtml?id=page", link.getHrefAttribute());
        page = link.click();
        assertEquals("outcometarget01", page.getTitleText());

        // ---------------------------------------------------------------------

        link = linkList.get(5);
        onclick = link.getOnClickAttribute();
        assertEquals("link6", link.getIdAttribute());
        assertEquals("/jsf-systest/faces/standard/outcometarget01.xhtml?id=page&id2=view", link.getHrefAttribute());
        page = link.click();
        assertEquals("outcometarget01", page.getTitleText());

        // ---------------------------------------------------------------------

        link = linkList.get(6);
        onclick = link.getOnClickAttribute();
        assertEquals("link7", link.getIdAttribute());
        assertEquals("/jsf-systest/faces/standard/outcometarget01.xhtml?id=config&id2=view", link.getHrefAttribute());
        page = link.click();
        assertEquals("outcometarget01", page.getTitleText());

    }
}
