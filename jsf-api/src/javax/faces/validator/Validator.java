/*
 * $Id: Validator.java,v 1.11 2003/08/15 17:23:48 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.io.Serializable;
import java.util.Iterator;
import javax.faces.component.StateHolder;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.application.Message;


/**
 * <p>A <strong>Validator</strong> implementation is a class that can perform
 * validation (correctness checks) on a {@link UIInput}.  Zero or more
 * <code>Validator</code>s can be associated with each {@link UIInput}
 * in the component tree, and are called during the <em>Process
 * Validations</em> phase of the request processing lifecycle.</p>
 *
 * <p>Individual {@link Validator}s should examine the component that
 * they are passed, and add {@link Message} instances to the
 * {@link FacesContext} for the current request, documenting
 * any failures to conform to the required rules.  In general, such
 * messages should be associated with the {@link UIInput} on which
 * the validation failure occurred.  In addition, the <code>valid</code>
 * property of the corresponding {@link UIInput} should be set to
 * <code>false</code> on validation failures.</p>
 *
 * <p>For maximum generality, {@link Validator} instances may be
 * configurable based on properties of the {@link Validator} implementation
 * class.  For example, a range check {@link Validator} might support
 * configuration of the minimum and maximum values to be used.  In
 * addition, because {@link Validator}s are part of the saved and restored
 * state of a component tree, classes that implement this interface must also
 * be serializable.</p>
 *
 * <p>{@link Validator} implementations must have a zero-arguments public
 * constructor.  In addition, if the {@link Validator} class wishes to have
 * configuration property values saved and restored with the component tree,
 * the implementation must also implement {@link StateHolder}.</p>
 */

public interface Validator extends Serializable {


    /**
     * <p>Perform the correctness checks implemented by this
     * {@link Validator} against the specified {@link UIInput}.
     * If any violations are found:</p>
     * <ul>
     * <li>Add zero or more {@link Message}s to the specified
     *     {@link FacesContext}, specifying this {@link UIInput} as
     *     associated with the message, describing the nature of the
     *     violation(s) encountered.</li>
     * <li>Set the <code>valid</code> property on the specified
     *     {@link UIInput} to <code>false</code>.</li>
     * </ul>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIInput we are checking for correctness
     *
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public void validate(FacesContext context, UIInput component);


}
