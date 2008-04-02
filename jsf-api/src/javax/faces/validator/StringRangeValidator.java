/*
 * $Id: StringRangeValidator.java,v 1.11 2003/02/20 22:46:41 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
     * @param maximum Maximum value to allow (if any)
     *
     * @exception NullPointerException if a specified limit
     *  is <code>null</code>
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
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     * @exception NullPointerException if a specified limit
     *  is <code>null</code>
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
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     * @exception NullPointerException if <code>maximum</code>
     *  is <code>null</code>
     */
    public void setMaximum(String maximum) {

        if (maximum == null) {
            throw new NullPointerException();
        }
        this.maximum = maximum;
        this.maximumSet = true;
        if (this.minimumSet &&
            (this.minimum.compareTo(this.maximum) > 0)) {
            throw new IllegalArgumentException();
        }

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
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     * @exception NullPointerException if <code>minimum</code>
     *  is <code>null</code>
     */
    public void setMinimum(String minimum) {

        if (minimum == null) {
            throw new NullPointerException();
        }
        this.minimum = minimum;
        this.minimumSet = true;
        if (this.maximumSet &&
            (this.minimum.compareTo(this.maximum) > 0)) {
            throw new IllegalArgumentException();
        }

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


    public void validate(FacesContext context, UIComponent component) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
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
                component.setValid(false);
            }
            if (isMinimumSet() &&
                (converted.compareTo(minimum) < 0)) {
                context.addMessage(component,
                                   getMessage(context,
                                              MINIMUM_MESSAGE_ID,
                                              new Object[] {
                                       new String(minimum) }));
                component.setValid(false);
            }
        }

    }


}
