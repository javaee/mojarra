/*
 * $Id: LongRangeValidatorTestCase.java,v 1.3 2005/08/22 22:08:30 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.validator;


import java.util.Locale;

import javax.faces.component.UIInput;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Unit tests for {@link LongRangeValidator}.</p>
 */

public class LongRangeValidatorTestCase extends ValidatorTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public LongRangeValidatorTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(LongRangeValidatorTestCase.class));
    }


    // ------------------------------------------------- Individual Test Methods

    public void testLocaleHonored() {
	LongRangeValidator validator = new LongRangeValidator();
	validator.setMinimum(10100);
	validator.setMaximum(20100);
	boolean exceptionThrown = false;
	UIInput component = new UIInput();
	String message;
	Locale.setDefault(Locale.US);
	facesContext.getViewRoot().setLocale(Locale.US);
	
	try {
	    validator.validate(facesContext, component, "5100");
	    fail("Exception not thrown");
	}
	catch (ValidatorException e) {
	    exceptionThrown = true;
	    message = e.getMessage();
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("10,100"));
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("20,100"));
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	Locale.setDefault(Locale.GERMAN);
	facesContext.getViewRoot().setLocale(Locale.GERMAN);

	try {
	    validator.validate(facesContext, component, "5100");
	    fail("Exception not thrown");
	}
	catch (ValidatorException e) {
	    exceptionThrown = true;
	    message = e.getMessage();
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("10.100"));
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("20.100"));
	}
	assertTrue(exceptionThrown);

    }

    public void testHashCode() {
        LongRangeValidator validator1 = new LongRangeValidator();
        LongRangeValidator validator2 = new LongRangeValidator();

        validator1.setMinimum(10l);
        validator1.setMaximum(15l);
        validator2.setMinimum(10l);
        validator2.setMaximum(15l);

        assertTrue(validator1.hashCode() == validator2.hashCode());
        assertTrue(validator1.hashCode() == validator2.hashCode());

        validator2.setMaximum(16l);

        assertTrue(validator1.hashCode() != validator2.hashCode());

        validator1 = new LongRangeValidator();
        validator2 = new LongRangeValidator();

        validator1.setMinimum(10l);
        validator2.setMinimum(10l);

        assertTrue(validator1.hashCode() == validator2.hashCode());
        assertTrue(validator1.hashCode() == validator2.hashCode());

        validator1.setMinimum(11l);

        assertTrue(validator1.hashCode() != validator2.hashCode());

        validator1.setMinimum(10l);
        validator1.setMaximum(11l);

        assertTrue(validator1.hashCode() != validator2.hashCode());
    }

}
