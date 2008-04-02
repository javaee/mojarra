/*
 * $Id: TestDataValidator.java,v 1.1 2003/10/22 23:23:51 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.application.Message;
import javax.faces.application.MessageImpl;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;


/**
 * <p>Test {@link Validator} implementation.</p>
 */

public class TestDataValidator implements Validator {


    // ------------------------------------------------------------ Constructors


    public TestDataValidator() {
    }


    // ---------------------------------------------------------- Public Methods


    public void validate(FacesContext context, UIInput component) {

        trace(component.getClientId(context));
        String value = (String) component.getValue();
        if (value == null) {
            value = "";
        }
        trace(value);
        if ("bad".equals(value)) {
            trace("ERROR");
            context.addMessage(component.getClientId(context),
                               new MessageImpl(Message.SEVERITY_ERROR,
                                               component.getClientId(context),
                                               null));
            component.setValid(false);
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
