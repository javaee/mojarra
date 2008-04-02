/*
 * $Id: FormInputTestCase.java,v 1.1 2003/09/30 22:23:42 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.jsptest;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;



/**
 * <p>Test Case for Form Input.</p>
 */

public class FormInputTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public FormInputTestCase(String name) {
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
        return (new TestSuite(FormInputTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables


    // ---------- form-input-02.jsp values ----------

    private String formInput02_name = "formInput02_form";

    private String formInput02_names[] =
    { "booleanProperty", "byteProperty", "doubleProperty", "floatProperty",
      "intProperty", "longProperty", "shortProperty", "stringProperty" };

    private String formInput02_pristine[] =
    { "true", "12", "123.45", "12.34",
      "123", "12345", "1234", "This is a String property" };

    private String formInput02_updated[] =
    { "false", "21", "543.21", "43.21",
      "321", "54321", "4321", "This was a String property" };

    // ------------------------------------------------- Individual Test Methods


    // ---------- form-input-02.jsp tests ----------

    // Request a pristine copy of the form a couple of times
    public void testFormInput02_pristine() throws Exception {

        checkFormInput00();
        checkFormInput02(getFormInput02(), formInput02_pristine);
        checkFormInput01();
        checkFormInput02(getFormInput02(), formInput02_pristine);
        checkFormInput00();

    }


    // Submit the form with no modifications
    public void testFormInput02_submit01() throws Exception {

        checkFormInput00();
        HtmlPage page = getFormInput02();
        checkFormInput02(page, formInput02_pristine);
        HtmlForm form = getFormById(page, formInput02_name);
        assertNotNull("form exists", form);
        HtmlSubmitInput submit = (HtmlSubmitInput)
            form.getInputByName(formInput02_name + UIComponent.SEPARATOR_CHAR +
                                "submit");
        page = (HtmlPage) submit.click();
        checkFormInput02(page, formInput02_pristine);

    }


    // Submit the form with updates to every field
    public void testFormInput02_submit02() throws Exception {

        checkFormInput00();
        HtmlPage page = getFormInput02();
        checkFormInput02(page, formInput02_pristine);
        HtmlForm form = getFormById(page, formInput02_name);
        assertNotNull("form exists", form);
        for (int i = 0; i < formInput02_names.length; i++) {
            HtmlTextInput input = (HtmlTextInput)
                form.getInputByName(formInput02_name +
                                    UIComponent.SEPARATOR_CHAR +
                                    formInput02_names[i]);
            assertNotNull("field '" + formInput02_names[i] + "' exists", input);
            input.setValueAttribute(formInput02_updated[i]);
        }
        HtmlSubmitInput submit = (HtmlSubmitInput)
            form.getInputByName(formInput02_name + UIComponent.SEPARATOR_CHAR +
                                "submit");
        page = (HtmlPage) submit.click();
        checkFormInput02(page, formInput02_updated);
        checkFormInput01();

    }


    // --------------------------------------------------------- Private Methods


    // Check the reset page to force a new component tree
    private void checkFormInput00() throws Exception {

        HtmlPage page = getPage("/faces/jsp/form-input-00.jsp");
        assertEquals("Correct page title",
                     "form-input-00", page.getTitleText());

    }


    // Check the success page was received
    private void checkFormInput01() throws Exception {

        checkFormInput01(getPage("/faces/jsp/form-input-01.jsp"));

    }


    // Check the success page was received
    private void checkFormInput01(HtmlPage page) {

        assertEquals("Correct page title",
                     "form-input-01", page.getTitleText());

    }


    // Check the values of the input fields against the specified list
    private void checkFormInput02(HtmlPage page, String expected[]) {


        assertEquals("Correct page title",
                     "form-input-02", page.getTitleText());
        HtmlForm form = getFormById(page, formInput02_name);
        assertNotNull("form exists", form);
        for (int i = 0; i < expected.length; i++) {
            HtmlTextInput input = (HtmlTextInput)
                form.getInputByName(formInput02_name +
                                    UIComponent.SEPARATOR_CHAR +
                                    formInput02_names[i]);
            assertNotNull("field '" + formInput02_names[i] + "' exists", input);
            assertEquals("field '" + formInput02_names[i] + "' value",
                         expected[i], input.getValueAttribute());
        }

    }


    // Return the form with the specified "id" from the specified page
    // (HtmlPage.getFormByName() looks at "name" instead)
    private HtmlForm getFormById(HtmlPage page, String id) {

        Iterator forms = page.getAllForms().iterator();
        while (forms.hasNext()) {
            HtmlForm form = (HtmlForm) forms.next();
            if (id.equals(form.getAttributeValue("id"))) {
                return (form);
            }
        }
        return (null);

    }


    // Retrieve the form-input-02 page
    private HtmlPage getFormInput02() throws Exception {

        return (getPage("/faces/jsp/form-input-02.jsp"));

    }


}
