/*
 * $Id: LongRangeValidator.java,v 1.2 2002/08/29 05:39:13 craigmcc Exp $
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
 * <p><strong>LongRangeValidator</strong> is a {@link Validator} that checks
 * the value of the corresponding component against specified minimum and
 * maximum values.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.  (If null values
 *     should not be allowed, a {@link RequiredValidator} can be configured
 *     to check for this case.)</li>
 * <li>If the current component value is not a floating point type, or
 *     a String that is convertible to long,
 *     add a TYPE_MESSAGE_ID message to the {@link FacesContext} for this
 *     request, and skip subsequent checks.</li>
 * <li>If a <code>maximum</code> property has been configured on this
 *     {@link Validator}, check the component value against
 *     this limit.  If the component value is greater than the
 *     specified minimum, add a MAXIMUM_MESSAGE_ID message to the
 *     {@link FacesContext} for this request.</li>
 * <li>If a <code>minimum</code> property has been configured on this
 *     {@link Validator}, check the component value against
 *     this limit.  If the component value is less than the
 *     specified minimum, add a MINIMUM_MESSAGE_ID message to the
 *     {@link FacesContext} for this request.</li>
 * </ul>
 */

public class LongRangeValidator extends ValidatorBase {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the maximum value check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured maximum value.</p>
     */
    public static final String MAXIMUM_MESSAGE_ID =
        "javax.faces.validator.LongRangeValidator.MAXIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the minimum value check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured minimum value.</p>
     */
    public static final String MINIMUM_MESSAGE_ID =
        "javax.faces.validator.LongRangeValidator.MINIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the current value of this component is not of the correct type.
     */
    public static final String TYPE_MESSAGE_ID =
        "javax.faces.validator.LongRangeValidator.TYPE";


    // ------------------------------------------------------------- Properties


    /**
     * <p>The maximum value to be enforced by this {@link Validator}, if
     * <code>maximumSet</code> is <code>true</code>.</p>
     */
    private long maximum = 0;


    /**
     * <p>Return the maximum value to be enforced by this {@link Validator},
     * if <code>isMaximumSet()</code> returns <code>true</code>.</p>
     */
    public long getMaximum() {

        return (this.maximum);

    }


    /**
     * <p>Set the maximum value to be enforced by this {@link Validator}.</p>
     *
     * @param maximum The new maximum value
     */
    public void setMaximum(long maximum) {

        this.maximum = maximum;
        this.maximumSet = true;

    }


    /**
     * <p>Flag indicating whether a maximum limit has been set.</p>
     */
    private boolean maximumSet = false;


    /**
     * <p>Return a flag indicating whether a maximum limit has been set.</p>
     */
    public boolean isMaximumSet() {

        return (this.maximumSet);

    }


    /**
     * <p>The minimum value to be enforced by this {@link Validator}, if
     * <code>minimumSet</code> is <code>true</code>.</p>
     */
    private long minimum = 0;


    /**
     * <p>Return the minimum value to be enforced by this {@link Validator},
     * if <code>isMinimumSet()</code> returns <code>true</code>.</p>
     */
    public long getMinimum() {

        return (this.minimum);

    }


    /**
     * <p>Set the minimum value to be enforced by this {@link Validator}.</p>
     *
     * @param minimum The new minimum value
     */
    public void setMinimum(long minimum) {

        this.minimum = minimum;
        this.minimumSet = true;

    }


    /**
     * <p>Flag indicating whether a minimum limit has been set.</p>
     */
    private boolean minimumSet = false;


    /**
     * <p>Return a flag indicating whether a minimum limit has been set.</p>
     */
    public boolean isMinimumSet() {

        return (this.minimumSet);

    }


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
     */
    public boolean validate(FacesContext context, UIComponent component) {

        boolean result = true;
        Object value = component.getValue();
        if (value != null) {
            try {
                long converted = longValue(value);
                if (isMaximumSet() &&
                    (converted > maximum)) {
                    context.addMessage(component,
                                       getMessage(context,
                                                  MAXIMUM_MESSAGE_ID,
                                                  new Object[] {
                                           new Long(maximum) }));
                    result = false;
                }
                if (isMinimumSet() &&
                    (converted < minimum)) {
                    context.addMessage(component,
                                       getMessage(context,
                                                  MINIMUM_MESSAGE_ID,
                                                  new Object[] {
                                           new Long(minimum) }));
                    result = false;
                }
            } catch (NumberFormatException e) {
                context.addMessage(component,
                                   getMessage(context, TYPE_MESSAGE_ID));
                result = false;
            }
        }
        return (result);

    }


}
