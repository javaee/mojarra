/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.validator;


import java.util.Locale;

import javax.faces.component.UIInput;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Unit tests for {@link DoubleRangeValidator}.</p>
 */

public class DoubleRangeValidatorTestCase extends ValidatorTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public DoubleRangeValidatorTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(DoubleRangeValidatorTestCase.class));
    }


    // ------------------------------------------------- Individual Test Methods

    public void testLocaleHonored() {
	DoubleRangeValidator validator = new DoubleRangeValidator();
	validator.setMinimum(10.1);
	validator.setMaximum(20.1);
	boolean exceptionThrown = false;
	UIInput component = new UIInput();
	String message;
	Locale.setDefault(Locale.US);
	facesContext.getViewRoot().setLocale(Locale.US);
	
	try {
	    validator.validate(facesContext, component, "5.1");
	    fail("Exception not thrown");
	}
	catch (ValidatorException e) {
	    exceptionThrown = true;
	    message = e.getMessage();
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("10.1"));
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("20.1"));
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	Locale.setDefault(Locale.GERMAN);
	facesContext.getViewRoot().setLocale(Locale.GERMAN);

	try {
	    validator.validate(facesContext, component, "5");
	    fail("Exception not thrown");
	}
	catch (ValidatorException e) {
	    exceptionThrown = true;
	    message = e.getMessage();
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("10,1"));
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("20,1"));
	}
	assertTrue(exceptionThrown);

    }

    public void testHashCode() {
        DoubleRangeValidator validator1 = new DoubleRangeValidator();
        DoubleRangeValidator validator2 = new DoubleRangeValidator();

        validator1.setMinimum(10.0d);
        validator1.setMaximum(15.1d);
        validator2.setMinimum(10.0d);
        validator2.setMaximum(15.1d);

        assertTrue(validator1.hashCode() == validator2.hashCode());
        assertTrue(validator1.hashCode() == validator2.hashCode());

        validator2.setMaximum(15.2d);

        assertTrue(validator1.hashCode() != validator2.hashCode());

        validator1 = new DoubleRangeValidator();
        validator2 = new DoubleRangeValidator();

        validator1.setMinimum(10.0d);
        validator2.setMinimum(10.0d);

        assertTrue(validator1.hashCode() == validator2.hashCode());
        assertTrue(validator1.hashCode() == validator2.hashCode());

        validator1.setMinimum(11.0d);

        assertTrue(validator1.hashCode() != validator2.hashCode());

        validator1.setMinimum(10.0d);
        validator1.setMaximum(10.1d);

        assertTrue(validator1.hashCode() != validator2.hashCode());
    }

}
