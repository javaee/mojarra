/*
 * $Id: TestDataValidator.java,v 1.5 2004/02/26 20:31:27 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


/**
 * <p>Test {@link Validator} implementation.</p>
 */

public class TestDataValidator implements Validator {


    // ------------------------------------------------------------ Constructors


    public TestDataValidator() {
    }


    // ---------------------------------------------------------- Public Methods


    public void validate(FacesContext context, UIComponent component, Object valueObj) {
        trace(component.getClientId(context));
        String value = (String) valueObj;
        if (value == null) {
            value = "";
        }
        trace(value);
        if ("bad".equals(value)) {
            trace("ERROR");
            throw new ValidatorException(
                               new FacesMessage(FacesMessage.SEVERITY_ERROR,
						component.getClientId(context),
						null));
        }

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
