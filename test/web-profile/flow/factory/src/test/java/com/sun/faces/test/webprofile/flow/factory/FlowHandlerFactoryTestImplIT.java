package com.sun.faces.test.webprofile.flow.factory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlowHandlerFactoryTestImplIT {

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
    public void testFlowHandlerFactoryWrapping() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        
        Thread.sleep(3000);

        assertTrue(page.getBody().asText().indexOf("Page with link to flow entry") != -1);

        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("start");
        page = button.click();

        String pageText = page.getBody().asText();
        assertTrue(pageText.indexOf("First page in the flow") != -1);
        assertTrue(pageText.contains("basicFlow"));
        assertTrue(pageText.contains("Did we wrap: true"));
        assertTrue(pageText.contains("Did we inject: MyAppBean"));
    }
}
