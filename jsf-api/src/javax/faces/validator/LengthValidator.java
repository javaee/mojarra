/*
 * $Id: LengthValidator.java,v 1.10 2002/09/20 02:43:36 craigmcc Exp $
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
 * <p><strong>LengthValidator</strong> is a {@link Validator} that checks
 * the number of characters in the String representation of the value of the
 * associated component.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.  (If null values
 *     should not be allowed, a {@link RequiredValidator} can be configured
 *     to check for this case.)</li>
 * <li>Convert the value to a String, if necessary, by calling its
 *     <code>toString()</code> method.</li>
 * <li>If a <code>maximum</code> property has been configured on this
 *     {@link Validator}, check the length of the converted
 *     String against this limit.  If the String length is larger than the
 *     specified minimum, add a MAXIMUM_MESSAGE_ID message to the
 *     {@link FacesContext} for this request.</li>
 * <li>If a <code>minimum</code> property has been configured on this
 *     {@link Validator}, check the length of the converted
 *     String against this limit.  If the String length is less than the
 *     specified minimum, add a MINIMUM_MESSAGE_ID message to the
 *     {@link FacesContext} for this request.</li>
 * </ul>
 */

public class LengthValidator extends ValidatorBase {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the maximum length check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured maximum length.</p>
     */
    public static final String MAXIMUM_MESSAGE_ID =
        "javax.faces.validator.LengthValidator.MAXIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the minimum length check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured minimum length.</p>
     */
    public static final String MINIMUM_MESSAGE_ID =
        "javax.faces.validator.LengthValidator.MINIMUM";


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a {@link Validator} with no preconfigured limits.</p>
     */
    public LengthValidator() {

        super();

    }


    /**
     * <p>Construct a {@link Validator} with the specified preconfigured
     * limit.</p>
     *
     * @param maximum Maximum value to allow
     */
    public LengthValidator(int maximum) {

        super();
        setMaximum(maximum);

    }


    /**
     * <p>Construct a {@link Validator} with the specified preconfigured
     * limits.</p>
     *
     * @param maximum Maximum value to allow
     * @param minimum Minimum value to allow
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     */
    public LengthValidator(int maximum, int minimum) {

        super();
        setMaximum(maximum);
        setMinimum(minimum);

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The maximum length to be enforced by this {@link Validator}, if
     * <code>maximumSet</code> is <code>true</code>.</p>
     */
    private int maximum = 0;


    /**
     * <p>Return the maximum length to be enforced by this {@link Validator},
     * if <code>isMaximumSet()</code> returns <code>true</code>.</p>
     */
    public int getMaximum() {

        return (this.maximum);

    }


    /**
     * <p>Set the maximum length to be enforced by this {@link Validator}.</p>
     *
     * @param maximum The new maximum value
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     */
    public void setMaximum(int maximum) {

        this.maximum = maximum;
        this.maximumSet = true;
        if (this.minimumSet && (this.minimum > this.maximum)) {
            throw new IllegalArgumentException();
        }

    }


    /**
     * <p>Flag indicating whether a maximum length has been set.</p>
     */
    private boolean maximumSet = false;


    /**
     * <p>Return a flag indicating whether a maximum length has been set.</p>
     */
    public boolean isMaximumSet() {

        return (this.maximumSet);

    }


    /**
     * <p>The minimum length to be enforced by this {@link Validator}, if
     * <code>minimumSet</code> is <code>true</code>.</p>
     */
    private int minimum = 0;


    /**
     * <p>Return the minimum length to be enforced by this {@link Validator},
     * if <code>isMinimumSet()</code> returns <code>true</code>.</p>
     */
    public int getMinimum() {

        return (this.minimum);

    }


    /**
     * <p>Set the minimum length to be enforced by this {@link Validator}.</p>
     *
     * @param minimum The new minimum value
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     */
    public void setMinimum(int minimum) {

        this.minimum = minimum;
        this.minimumSet = true;
        if (this.maximumSet && (this.minimum > this.maximum)) {
            throw new IllegalArgumentException();
        }

    }


    /**
     * <p>Flag indicating whether a minimum length has been set.</p>
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
                (converted.length() > maximum)) {
                context.addMessage(component,
                                   getMessage(context,
                                              MAXIMUM_MESSAGE_ID,
                                              new Object[] {
                                       new Integer(maximum) } ));
                result = false;
            }
            if (isMinimumSet() &&
                (converted.length() < minimum)) {
                context.addMessage(component,
                                   getMessage(context,
                                              MINIMUM_MESSAGE_ID,
                                              new Object[] {
                                       new Integer(minimum) } ));
                result = false;
            }
        }
        return (result);

    }


}
