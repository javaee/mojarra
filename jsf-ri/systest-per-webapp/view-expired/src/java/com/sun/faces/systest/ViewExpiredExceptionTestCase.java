package com.sun.faces.systest;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class ViewExpiredExceptionTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    public ViewExpiredExceptionTestCase(String name) {

        super(name);

    }

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(ViewExpiredExceptionTestCase.class));

    }


    /** Set up instance variables required by this test case. */
    public void setUp() throws Exception {

        super.setUp();

    }


    /** Tear down instance variables required by this test case. */
    public void tearDown() {

        super.tearDown();

    }

    // ------------------------------------------------------------ Test Methods

    public void testViewExpiredExceptionErrorPage() throws Exception {

        WebClient client = new WebClient();
        client.setThrowExceptionOnFailingStatusCode(false);
        client.setTimeout(0);
        URL url = null;
        try {
            url = new URL(
                  "http://localhost:8080/jsf-view-expired/faces/test.jsp");
        } catch (Exception e) {
            fail(e.toString());
        }
        HtmlPage page = (HtmlPage) client.getPage(url);

        HtmlSubmitInput submit = (HtmlSubmitInput)
              getInputContainingGivenId(page, "submit");

        Thread.sleep(65000);

        HtmlPage errorPage = (HtmlPage) submit.click();
        assertTrue(errorPage.asText().indexOf("Error page invoked") >= 0);

    }

}
