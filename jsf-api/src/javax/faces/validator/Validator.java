/*
 * $Id: Validator.java,v 1.24 2005/02/24 15:18:53 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.util.EventListener;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;


/**
 * <p>A <strong>Validator</strong> implementation is a class that can
 * perform validation (correctness checks) on a {@link
 * javax.faces.component.EditableValueHolder}.  Zero or more
 * <code>Validator</code>s can be associated with each
 * {@link javax.faces.component.EditableValueHolder} in
 * the view, and are called during the <em>Process Validations</em>
 * phase of the request processing lifecycle.</p>
 *
 * <p>Individual {@link Validator}s should examine the value and
 * component that they are passed, and throw a {@link ValidatorException}
 * containing a {@link FacesMessage}, documenting
 * any failures to conform to the required rules.
 *
 * <p>For maximum generality, {@link Validator} instances may be
 * configurable based on properties of the {@link Validator} implementation
 * class.  For example, a range check {@link Validator} might support
 * configuration of the minimum and maximum values to be used.</p>
 *
 * <p>{@link Validator} implementations must have a zero-arguments
 * public constructor.  In addition, if the {@link Validator} class
 * wishes to have configuration property values saved and restored with
 * the view, the implementation must also implement {@link
 * StateHolder}.</p>
 */

public interface Validator extends EventListener {

    /**
     * <p>The message identifier of the {@link FacesMessage} to be created if
     * the maximum or minimum value check fails, and both the maximum
     * and minimum values for this validator have been set.  The message
     * format string for this message may optionally include a
     * <code>{0}</code> placeholder, which will be replaced by the
     * configured minimum value, a <code>{1}</code> placeholder,
     * which will be replaced by the configured maximum value, and
     * a <code>{2}</code> placeholder, which will be replaced by a 
     * <code>String</code> whose value is the label of the input component 
     * that produced this message.</p>
     */
    public static final String NOT_IN_RANGE_MESSAGE_ID =
        "javax.faces.validator.NOT_IN_RANGE";

    /**
     * <p>Perform the correctness checks implemented by this
     * {@link Validator} against the specified {@link UIComponent}.
     * If any violations are found, a {@link ValidatorException}
     * will be thrown containing the {@link FacesMessage} describing
     * the failure.
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     * @param value     the value to validate
     *
     * @exception ValidatorException if validation fails
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public void validate(FacesContext context,
                         UIComponent  component,
                         Object       value) throws ValidatorException;


}
