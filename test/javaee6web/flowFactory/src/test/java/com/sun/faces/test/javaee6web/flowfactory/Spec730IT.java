package com.sun.faces.test.javaee6web.flowfactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Spec730IT {

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
    public void testCustomFlowFactory() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        assertTrue(page.getBody().asText().contains("Page with link to flow entry"));

        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("start");
        page = button.click();

        String pageText = page.getBody().asText();
        assertTrue(pageText.contains("First page in the flow"));
        assertTrue(pageText.contains("basicFlow"));
        assertTrue(pageText.contains("Did we wrap: true"));
        assertTrue(pageText.contains("Did we inject: MyAppBean"));
    }
}
