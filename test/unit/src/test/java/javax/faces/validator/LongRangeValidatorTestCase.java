/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link LongRangeValidator}.</p>
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
        } catch (ValidatorException e) {
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
        } catch (ValidatorException e) {
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
