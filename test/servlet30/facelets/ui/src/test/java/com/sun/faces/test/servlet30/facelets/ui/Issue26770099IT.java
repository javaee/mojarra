package com.sun.faces.test.servlet30.facelets.ui;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.faces.component.html.HtmlOutputText;

import static org.junit.Assert.*;

/**
 * Created by xinyuan.zhang on 9/18/17.
 */
public class Issue26770099IT {


    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testAbcInRepeatedInputTexts() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/issue26770099.xhtml");
        HtmlHiddenInput htmlHiddenInput1 = page.getHtmlElementById("form:repeat:0:hidden1");
        assertEquals("OneBean", htmlHiddenInput1.getValueAttribute());
        HtmlHiddenInput htmlHiddenInput2 = page.getHtmlElementById("form:repeat:0:hidden2");
        assertEquals("0", htmlHiddenInput2.getValueAttribute());
        HtmlInput htmlInput1 = page.getHtmlElementById("form:repeat:0:value1");
        assertEquals("aaa", htmlInput1.getValueAttribute());
        HtmlInput htmlInput2 = page.getHtmlElementById("form:repeat:0:value2");
        assertEquals("AAA", htmlInput2.getValueAttribute());
        assertTrue(page.asText().contains("anotherBean"));
        assertTrue(page.asText().contains("abc"));
    }
}
