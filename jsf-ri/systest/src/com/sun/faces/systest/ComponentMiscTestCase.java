package com.sun.faces.systest;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Test Case for JSP Interoperability.</p>
 */

public class ComponentMiscTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ComponentMiscTestCase(String name) {
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
        return (new TestSuite(ComponentMiscTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    public void testModelCoercionForUISelectOne() throws Exception {
        HtmlPage page = getPage("/faces/ModelSelectItemConversion.jsp");
        HtmlSelect select = (HtmlSelect) getAllElementsOfGivenClass(page, 
                                                                    new ArrayList(),
                                                                    HtmlSelect.class).get(0);
        HtmlOption option = select.getOption(1);
        option.setSelected(true);
        
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page,
                                                                             "submit");
        
        // clicking this should yield no errors.
        submit.click();
        
    }
    
    public void testConverterForUISelectMany() throws Exception {
        HtmlPage page = getPage("/faces/SelectManyConverterTest.jsp");
        List selects = getAllElementsOfGivenClass(page, 
                                                  new ArrayList(), 
                                                  HtmlSelect.class);
        HtmlSelect select = (HtmlSelect) selects.get(0);
        HtmlSelect select2 = (HtmlSelect) selects.get(1);
        select.getOption(1).setSelected(true);
        select.getOption(2).setSelected(true);
        select2.getOption(1).setSelected(true);
        select2.getOption(2).setSelected(true);
        
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(page,
                                                                             "submit");
        page = (HtmlPage) submit.click();
        
        // ensure no validator errors
        String pageText = page.asText();
        assertTrue(pageText.indexOf("Value is not valid") < 0);
    }
}
