/*
 * $Id: TestInputValidator.java,v 1.1 2003/10/27 04:10:07 craigmcc Exp $
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

public class TestInputValidator implements Validator {

    protected String validatorId = null;
 
    public TestInputValidator(String validatorId) {
	this.validatorId = validatorId;
    }
    
    public void validate(FacesContext context, UIInput component) {
        trace(validatorId);
    }


    // ---------------------------------------------------- Static Trace Methods


    // Accumulated trace log
    private static StringBuffer trace = new StringBuffer();

    // Append to the current trace log (or clear if null)
    public static void trace(String text) {
        if (text == null) {
            trace.setLength(0);
        } else {
            trace.append('/');
            trace.append(text);
        }
    }

    // Retrieve the current trace log
    public static String trace() {
        return (trace.toString());
    }


}
