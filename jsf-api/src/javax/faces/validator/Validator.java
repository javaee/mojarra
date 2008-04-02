/*
 * $Id: Validator.java,v 1.28 2005/12/05 16:43:03 edburns Exp $
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
 * containing a {@link javax.faces.application.FacesMessage}, documenting
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
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the maximum or minimum value check fails, and both the maximum
     * and minimum values for this validator have been set.  The message
     * format string for this message may optionally include a
     * <code>{0}</code> placeholder, which will be replaced by the
     * configured minimum value, and a <code>{1}</code> placeholder,
     * which will be replaced by the configured maximum value.</p>
     * @deprecated Use {@link DoubleRangeValidator#NOT_IN_RANGE_MESSAGE_ID} or 
     *   {@link LongRangeValidator#NOT_IN_RANGE_MESSAGE_ID} instead.
     */
    public static final String NOT_IN_RANGE_MESSAGE_ID =
        "javax.faces.validator.NOT_IN_RANGE";

    /**
     * <p>Perform the correctness checks implemented by this
     * {@link Validator} against the specified {@link UIComponent}.
     * If any violations are found, a {@link ValidatorException}
     * will be thrown containing the {@link javax.faces.application.FacesMessage} describing
     * the failure.
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     * @param value     the value to validate
     *
     * @throws ValidatorException if validation fails
     * @throws NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public void validate(FacesContext context,
                         UIComponent  component,
                         Object       value) throws ValidatorException;


}
