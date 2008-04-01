/*
 * $Id: StringRangeValidator.java,v 1.6 2002/08/29 05:52:33 craigmcc Exp $
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
 * <p><strong>StringRangeValidator</strong> is a {@link Validator} that checks
 * the value of the corresponding component against specified minimum and
 * maximum values.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.  (If null values
 *     should not be allowed, a {@link RequiredValidator} can be configured
 *     to check for this case.)</li>
 * <li>Convert the current component value to String, if necessary,
 *     by calling <code>toString()</code>.</p>
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

public class StringRangeValidator extends ValidatorBase {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the maximum value check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured maximum value.</p>
     */
    public static final String MAXIMUM_MESSAGE_ID =
        "javax.faces.validator.StringRangeValidator.MAXIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the minimum value check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured minimum value.</p>
     */
    public static final String MINIMUM_MESSAGE_ID =
        "javax.faces.validator.StringRangeValidator.MINIMUM";


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a {@link Validator} with no preconfigured limits.</p>
     */
    public StringRangeValidator() {

        super();

    }


    /**
     * <p>Construct a {@link Validator} with the specified preconfigured
     * limit.</p>
     *
     * @param maximum Maximum value to allow
     */
    public StringRangeValidator(String maximum) {

        super();
        setMaximum(maximum);

    }


    /**
     * <p>Construct a {@link Validator} with the specified preconfigured
     * limits.</p>
     *
     * @param maximum Maximum value to allow
     * @param minimum Minimum value to allow
     */
    public StringRangeValidator(String maximum, String minimum) {

        super();
        setMaximum(maximum);
        setMinimum(minimum);

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The maximum value to be enforced by this {@link Validator}, if
     * <code>maximumSet</code> is <code>true</code>.</p>
     */
    private String maximum = null;


    /**
     * <p>Return the maximum value to be enforced by this {@link Validator},
     * if <code>isMaximumSet()</code> returns <code>true</code>.</p>
     */
    public String getMaximum() {

        return (this.maximum);

    }


    /**
     * <p>Set the maximum value to be enforced by this {@link Validator}.</p>
     *
     * @param maximum The new maximum value
     */
    public void setMaximum(String maximum) {

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
    private String minimum = null;


    /**
     * <p>Return the minimum value to be enforced by this {@link Validator},
     * if <code>isMinimumSet()</code> returns <code>true</code>.</p>
     */
    public String getMinimum() {

        return (this.minimum);

    }


    /**
     * <p>Set the minimum value to be enforced by this {@link Validator}.</p>
     *
     * @param minimum The new minimum value
     */
    public void setMinimum(String minimum) {

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
            String converted = stringValue(value);
            if (isMaximumSet() &&
                (converted.compareTo(maximum) > 0)) {
                context.addMessage(component,
                                   getMessage(context,
                                              MAXIMUM_MESSAGE_ID,
                                              new Object[] {
                                       new String(maximum) }));
                result = false;
            }
            if (isMinimumSet() &&
                (converted.compareTo(minimum) < 0)) {
                context.addMessage(component,
                                   getMessage(context,
                                              MINIMUM_MESSAGE_ID,
                                              new Object[] {
                                       new String(minimum) }));
                result = false;
            }
        }
        return (result);

    }


}
