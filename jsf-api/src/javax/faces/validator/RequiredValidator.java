/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * $Id: RequiredValidator.java,v 1.14 2003/04/29 18:51:46 eburns Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.application.Message;


/**
 * <p><strong>RequiredValidator</strong> is a {@link Validator} that checks
 * for the existence of a value for the associated component.  For the purposes
 * of this Validator, existence means a non-null value returned by calling
 * <code>getValue()</code>.  In addition, if the component value is a String,
 * it must have a length greater than zero.</p>
 */

public class RequiredValidator extends ValidatorBase {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * this validation fails.</p>
     */
    public static final String FAILED_MESSAGE_ID =
        "javax.faces.validator.RequiredValidator.FAILED";



    // --------------------------------------------------------- Public Methods


    public void validate(FacesContext context, UIComponent component) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        boolean isValid = true;
        Object value = ((UIInput) component).getValue();
        if (value == null) {
            isValid = false;
        }
	else if (value instanceof String) {
	    isValid = ((String)value).length() > 0;
	}
	if (!isValid) {
            context.addMessage(component,
                               getMessage(context, FAILED_MESSAGE_ID));
            component.setValid(false);
	}

    }


}
