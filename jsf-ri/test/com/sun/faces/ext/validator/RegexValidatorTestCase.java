/*
 * $Id: RegexValidatorTestCase.java,v 1.2 2008/04/01 22:31:21 driscoll Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.ext.validator;


import javax.faces.component.UIInput;

import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;
import javax.el.ValueExpression;
import javax.el.ELContext;
import javax.el.ExpressionFactory;


import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Unit tests for {@link LengthValidator}.</p>
 */

public class RegexValidatorTestCase extends ValidatorTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public RegexValidatorTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(RegexValidatorTestCase.class));
    }


    // ------------------------------------------------- Individual Test Methods

    public void testPatternMatch() {
        String patternStr = "t.*";
        RegexValidator validator = new RegexValidator();
        ELContext elcontext = facesContext.getELContext();
        ExpressionFactory exfact = application.getExpressionFactory();
        ValueExpression pattern = exfact.createValueExpression(patternStr, java.lang.String.class);
        validator.setPattern(pattern);
        boolean exceptionThrown = false;
        UIInput component = new UIInput();
        String checkme = "test";
        try {
            validator.validate(facesContext, component, checkme);
            assertTrue(true);
        } catch (ValidatorException ve) {
            fail("Exception thrown "+ve.getMessage());
        }
    }

    public void testPatterMismatch() {
        String patternStr = "t.*";
        RegexValidator validator = new RegexValidator();
        ELContext elcontext = facesContext.getELContext();
        ExpressionFactory exfact = application.getExpressionFactory();
        ValueExpression pattern = exfact.createValueExpression(patternStr, java.lang.String.class);
        validator.setPattern(pattern);
        boolean exceptionThrown = false;
        UIInput component = new UIInput();
        String checkme = "jest";
        try {
            validator.validate(facesContext, component, checkme);
            fail("Exception not thrown when tested "+checkme+" against "+patternStr);
        } catch (ValidatorException ve) {
            FacesMessage fmsg = ve.getFacesMessage();
            String detail = ve.getFacesMessage().getDetail();
            System.out.println("Detail in test: "+detail);
            assertTrue(detail.equalsIgnoreCase("Regex pattern of 't.*' not matched"));
        }
    }
}
