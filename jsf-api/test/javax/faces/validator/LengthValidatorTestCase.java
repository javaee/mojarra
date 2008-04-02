/*
 * $Id: LengthValidatorTestCase.java,v 1.1 2004/05/12 02:00:48 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Locale;

import javax.faces.component.UIInput;

/**
 * <p>Unit tests for {@link LengthValidator}.</p>
 */

public class LengthValidatorTestCase extends ValidatorTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public LengthValidatorTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(LengthValidatorTestCase.class));
    }


    // ------------------------------------------------- Individual Test Methods

    public void testLocaleHonored() {
	LengthValidator validator = new LengthValidator();
	validator.setMinimum(1000);
	validator.setMaximum(2000);
	boolean exceptionThrown = false;
	UIInput component = new UIInput();
	String message;
	Locale.setDefault(Locale.US);
	facesContext.getViewRoot().setLocale(Locale.US);
	
	try {
	    validator.validate(facesContext, component, 
			       "Not at all long enough");
	    fail("Exception not thrown");
	}
	catch (ValidatorException e) {
	    exceptionThrown = true;
	    message = e.getMessage();
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("1,000"));
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	Locale.setDefault(Locale.GERMAN);
	facesContext.getViewRoot().setLocale(Locale.GERMAN);

	try {
	    validator.validate(facesContext, component, 
			       "Still not long enough");
	    fail("Exception not thrown");
	}
	catch (ValidatorException e) {
	    exceptionThrown = true;
	    message = e.getMessage();
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("1.000"));
	}
	assertTrue(exceptionThrown);

    }

}
