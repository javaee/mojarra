/*
 * $Id: RequiredValidator.java,v 1.6 2002/09/20 02:48:37 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;


/**
 * <p><strong>RequiredValidator</strong> is a {@link Validator} that checks
 * for the existence of a value for the associated component.  For the purposes
 * of this Validator, "existence" means a non-<code>null</code> value returned
 * by the <code>getValue()</code> method.</p>
 *
 * <p><strong>FIXME</strong> - In .03, a zero-length String would also fail
 * this Validator -- which behavior do we want?</p>
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


    /**
     * <p>Perform the correctness checks implemented by this
     * <code>Validator</code> against the specified {@link UIComponent}.
     * Add {@link Message}s describing any correctness violations to the
     * specified {@link FacesContext}.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     *
     * @return <code>true</code> if all validations performed by this
     *  method passed successfully, or <code>false</code> if one or more
     *  validations performed by this method failed
     *
     * @exception NullPointerException if any parameter is <code>null</code>
     */
    public boolean validate(FacesContext context, UIComponent component) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        boolean result = true;
        Object value = component.getValue();
        if (value == null) {
            context.addMessage(component,
                               getMessage(context, FAILED_MESSAGE_ID));
            result = false;
        }
        return (result);

    }


}
