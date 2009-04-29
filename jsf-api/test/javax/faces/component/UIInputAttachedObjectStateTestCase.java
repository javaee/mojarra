/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package javax.faces.component;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import javax.faces.context.FacesContext;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;

import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockApplication;


public class UIInputAttachedObjectStateTestCase extends TestCase {

    private MockFacesContext facesContext;

    public UIInputAttachedObjectStateTestCase(String arg0) {
        super(arg0);
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIInputAttachedObjectStateTestCase.class));
    }

    public void setUp() throws Exception {
        super.setUp();
        facesContext = new MockFacesContext();
        facesContext.setApplication(new MockApplication());
    }


    // ------------------------------------------------------------ Test Methods


    public void testValidatorState() {

        UIInput input = new UIInput();
        LengthValidator l1 = new LengthValidator();
        LengthValidator l2 = new LengthValidator();
        input.addValidator(l1);
        input.addValidator(l2);
        l1.setMinimum(1);
        l2.setMinimum(2);
        input.markInitialState();
        assertTrue(input.initialStateMarked());
        assertTrue(l1.initialStateMarked());
        assertTrue(l2.initialStateMarked());

        Object state = input.saveState(facesContext);
        assertTrue(state instanceof Object[]);
        assertTrue(((Object[]) state)[3] == null);

        input = new UIInput();
        l1 = new LengthValidator();
        l2 = new LengthValidator();
        input.addValidator(l1);
        input.addValidator(l2);
        l1.setMinimum(1);
        l2.setMinimum(2);
        input.markInitialState();
        l2.setMinimum(3);
        assertTrue(input.initialStateMarked());
        assertTrue(l1.initialStateMarked());
        assertTrue(!l2.initialStateMarked());
        state = input.saveState(facesContext);
        assertTrue(state instanceof Object[]);
        Object[] validatorState = (Object[]) ((Object[]) state)[3];
        assertNotNull(validatorState);
        assertNull(validatorState[0]);
        assertNotNull(validatorState[1]);
        assertTrue(!(validatorState[1] instanceof StateHolderSaver));
        input = new UIInput();
        l1 = new LengthValidator();
        l2 = new LengthValidator();
        l1.setMinimum(1);
        l2.setMinimum(2);
        input.addValidator(l1);
        input.addValidator(l2);
        input.restoreState(facesContext, state);
        assertTrue(l1.getMinimum() == 1);
        assertTrue(l2.getMinimum() == 3);
        assertTrue(input.getValidators().length == 2);

        input = new UIInput();
        l1 = new LengthValidator();
        l2 = new LengthValidator();
        input.addValidator(l1);
        input.addValidator(l2);
        l1.setMinimum(1);
        l2.setMinimum(2);
        input.markInitialState();
        LengthValidator l3 = new LengthValidator();
        l3.setMinimum(3);
        input.addValidator(l3);
        state = input.saveState(facesContext);
        assertNotNull(validatorState);
        assertTrue(state instanceof Object[]);
        validatorState = (Object[]) ((Object[]) state)[3];
        assertNotNull(validatorState);
        assertTrue(validatorState.length == 3);
        assertNotNull(validatorState[0]);
        assertNotNull(validatorState[1]);
        assertNotNull(validatorState[2]);
        assertTrue(validatorState[0] instanceof StateHolderSaver);
        assertTrue(validatorState[1] instanceof StateHolderSaver);
        assertTrue(validatorState[2] instanceof StateHolderSaver);

        input = new UIInput();
        l1 = new LengthValidator();
        l2 = new LengthValidator();
        l3 = new LengthValidator();
        LengthValidator l4 = new LengthValidator();
        input.addValidator(l1);
        input.addValidator(l2);
        input.addValidator(l3);
        input.addValidator(l4);
        l1.setMinimum(100);
        l2.setMinimum(101);
        l3.setMinimum(102);
        l4.setMinimum(103);
        assertTrue(input.getValidators().length == 4);
        input.markInitialState();
        input.restoreState(facesContext, state);
        assertTrue(input.getValidators().length == 3);
       
        Validator[] validators = input.getValidators();
        for (int i = 0, len = validators.length; i < len; i++) {
            LengthValidator v = (LengthValidator) validators[i];
            assertTrue(v.getMinimum() == (i + 1));
        }

    }
    
}
