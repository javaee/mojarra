package com.sun.faces.systest;

import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/** <p>Test Case for JSP Interoperability.</p> */

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

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(ComponentMiscTestCase.class));

    }

    // ------------------------------------------------------ Instance Variables

    // ---------------------------------------------------- Overall Test Methods


    /** Set up instance variables required by this test case. */
    public void setUp() throws Exception {

        super.setUp();

    }


    /** Tear down instance variables required by this test case. */
    public void tearDown() {

        super.tearDown();

    }

    // ------------------------------------------------- Individual Test Methods


    public void testModelCoercionForUISelectOne() throws Exception {

        HtmlPage page = getPage("/faces/ModelSelectItemConversion.jsp");
        HtmlSelect select = (HtmlSelect) getAllElementsOfGivenClass(page,
                                                                    new ArrayList(),
                                                                    HtmlSelect.class)
              .get(0);
        HtmlOption option = select.getOption(1);
        option.setSelected(true);

        HtmlSubmitInput submit =
              (HtmlSubmitInput) getInputContainingGivenId(page,
                                                          "submit");

        // clicking this should yield no errors.
        submit.click();

    }

}
