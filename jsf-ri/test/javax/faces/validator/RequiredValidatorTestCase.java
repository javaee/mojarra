/*
 * $Id: RequiredValidatorTestCase.java,v 1.3 2003/04/03 18:40:55 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;

import com.sun.faces.ServletFacesTestCase;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.component.UIInput;
import javax.faces.component.UIComponent;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;



public class RequiredValidatorTestCase extends ServletFacesTestCase {


    // ----------------------------------------------------- Instance Variables


    /**
     * The component to be tested for each test.
     */
    protected UIComponent component = null;
    protected Validator validator = null;


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public RequiredValidatorTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
	super.setUp();

        component = new UIInput();
	validator = new RequiredValidator();

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(RequiredValidatorTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
	super.tearDown();
        component = null;
	validator = null;

    }

    // ------------------------------------------------ Individual Test Methods
    public void testValidatorNull() {
	component.addValidator(validator);
	((UIInput)component).setValue(null);
	validator.validate(getFacesContext(), component);
	Iterator messages = getFacesContext().getMessages();
	assertTrue(messages.hasNext());
    }

    public void testValidatorZeroLength() {
	component.addValidator(validator);
	((UIInput)component).setValue(new String(""));
	validator.validate(getFacesContext(), component);
	Iterator messages = getFacesContext().getMessages();
	assertTrue(messages.hasNext());
    }

    public void testValidatorValid() {
	component.addValidator(validator);
	((UIInput)component).setValue("hello");
	validator.validate(getFacesContext(), component);
	Iterator messages = getFacesContext().getMessages();
	assertTrue(!messages.hasNext());
    }

}
