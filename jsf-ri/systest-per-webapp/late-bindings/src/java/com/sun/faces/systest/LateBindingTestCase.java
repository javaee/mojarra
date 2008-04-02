package com.sun.faces.systest;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.List;

/**
 * <p>Make sure that an application that replaces the ApplicationFactory
 * but uses the decorator pattern to allow the existing ApplicationImpl
 * to do the bulk of the requests works.</p>
 */

public class LateBindingTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public LateBindingTestCase(String name) {
        super(name);
    }

    // ------------------------------------------------------ Instance Variables

    // ---------------------------------------------------- Overall Test Methods


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
        return (new TestSuite(LateBindingTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }

    // ------------------------------------------------------ Instance Variables

    // ------------------------------------------------- Individual Test Methods

    /**
     * <p>Verify that selectOne conversion has successfully occurred.</p>
     */

    public void testConverterPropertyEditor() throws Exception {
        
        HtmlPage page = getPage("/faces/Test.jsp");
        assertTrue(!(page.asText().contains("Custom Converter") && (page.asText().contains("Custom Validator"))));

        HtmlSubmitInput button = (HtmlSubmitInput) 
             getAllElementsOfGivenClass(page, null, HtmlSubmitInput.class).get(0);
        page = (HtmlPage) button.click();
        assertTrue((page.asText().contains("CustomConverter1 invoked"))
             && (page.asText().contains("CustomValidator2 invoked"))
             && !(page.asText().contains("CustomConverter2 invoked"))
             && !(page.asText().contains("CustomValidator1 invoked")));

        button = (HtmlSubmitInput)
             getAllElementsOfGivenClass(page, null, HtmlSubmitInput.class).get(0);
        page = (HtmlPage) button.click();
        assertTrue((page.asText().contains("CustomConverter2 invoked"))
             && (page.asText().contains("CustomValidator1 invoked"))
             && !(page.asText().contains("CustomConverter1 invoked"))
             && !(page.asText().contains("CustomValidator2 invoked")));
    }

}
