/*
 * $Id: TestValidator.java,v 1.9 2003/09/25 07:46:05 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
