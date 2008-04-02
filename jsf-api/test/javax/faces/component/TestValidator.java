/*
 * $Id: TestValidator.java,v 1.13 2005/08/22 22:08:17 ofung Exp $
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

package javax.faces.component;


import java.util.Collections;
import java.util.Iterator;
import javax.faces.component.UIInput;
import javax.faces.component.UIComponent;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;


/**
 * <p>Test implementation of {@link Validator}.</p>
 */

public class TestValidator implements Validator, StateHolder {

    protected String validateState = null;
 
    public TestValidator(String newState) {
	validateState = newState;
    }
    
    public TestValidator() {}

    public void validate(FacesContext context, UIComponent component, Object value) {
        ;  // No action taken
    }

    public boolean equals(Object otherObj) {
	if (!(otherObj instanceof TestValidator)) {
	    return false;
	}
	TestValidator other = (TestValidator) otherObj;
	if (!((validateState == null && other.validateState == null) ||
	    (validateState != null && other.validateState != null))) {
	    return  false;
	}
	if (validateState != null) {
	    if (!validateState.equals(other.validateState)) {
		return false;
	    }
	}
	return true;
    }

    //
    // methods from StateHolder
    //
    
    public Object saveState(FacesContext context) {
	return validateState;
    }

    public void restoreState(FacesContext context, Object state) {
	validateState = (String) state;
    }

    public boolean isTransient() { return false;
    }

    public void setTransient(boolean newT) {}

}
