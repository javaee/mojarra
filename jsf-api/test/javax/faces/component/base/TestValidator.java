/*
 * $Id: TestValidator.java,v 1.3 2003/07/28 22:22:27 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.Collections;
import java.util.Iterator;
import javax.faces.component.UIInput;
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

    public void validate(FacesContext context, UIInput component) {
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
    
    public Object getState(FacesContext context) {
	return validateState;
    }

    public void restoreState(FacesContext context, Object state) {
	validateState = (String) state;
    }

    public boolean isTransient() { return false;
    }

    public void setTransient(boolean newT) {}



}
