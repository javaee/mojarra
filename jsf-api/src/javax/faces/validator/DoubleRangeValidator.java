/*
 * $Id: DoubleRangeValidator.java,v 1.26 2003/10/19 21:13:06 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import javax.faces.application.Message;
import javax.faces.component.StateHolder;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;


/**
 * <p><strong>DoubleRangeValidator</strong> is a {@link Validator} that checks
 * the value of the corresponding component against specified minimum and
 * maximum values.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.</li>
 * <li>If the current component value is not a floating point type, or
 *     a String that is convertible to double,
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

public class DoubleRangeValidator implements Validator, StateHolder {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the maximum value check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured maximum value.</p>
     */
    public static final String MAXIMUM_MESSAGE_ID =
        "javax.faces.validator.DoubleRangeValidator.MAXIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the minimum value check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured minimum value.</p>
     */
    public static final String MINIMUM_MESSAGE_ID =
        "javax.faces.validator.DoubleRangeValidator.MINIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the current value of this component is not of the correct type.
     */
    public static final String TYPE_MESSAGE_ID =
        "javax.faces.validator.DoubleRangeValidator.TYPE";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a {@link Validator} with no preconfigured limits.</p>
     */
    public DoubleRangeValidator() {

        super();

    }


    /**
     * <p>Construct a {@link Validator} with the specified preconfigured
     * limit.</p>
     *
     * @param maximum Maximum value to allow
     */
    public DoubleRangeValidator(double maximum) {

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
    public DoubleRangeValidator(double maximum, double minimum) {

        super();
        setMaximum(maximum);
        setMinimum(minimum);

    }


    // -------------------------------------------------------------- Properties


    private double maximum = 0.0;
    private boolean maximumSet = false;


    /**
     * <p>Return the maximum value to be enforced by this {@link Validator}.</p>
     */
    public double getMaximum() {

        return (this.maximum);

    }


    /**
     * <p>Set the maximum value to be enforced by this {@link Validator}.</p>
     *
     * @param maximum The new maximum value
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     */
    public void setMaximum(double maximum) {

        this.maximum = maximum;
        this.maximumSet = true;
        if (this.minimumSet && (this.minimum > this.maximum)) {
            throw new IllegalArgumentException();
        }

    }


    private double minimum = 0.0;
    private boolean minimumSet = false;


    /**
     * <p>Return the minimum value to be enforced by this {@link Validator}.</p>
     */
    public double getMinimum() {

        return (this.minimum);

    }


    /**
     * <p>Set the minimum value to be enforced by this {@link Validator}.</p>
     *
     * @param minimum The new minimum value
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     */
    public void setMinimum(double minimum) {

        this.minimum = minimum;
        this.minimumSet = true;
        if (this.maximumSet && (this.minimum > this.maximum)) {
            throw new IllegalArgumentException();
        }

    }


    // ------------------------------------------------------- Validator Methods

    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void validate(FacesContext context, UIInput component) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        Object value = component.getValue();
        if (value != null) {
            try {
                double converted = doubleValue(value);
                if (maximumSet &&
                    (converted > maximum)) {
                    context.addMessage(component.getClientId(context),
                                       ValidatorMessages.getMessage
                                       (context,
                                        MAXIMUM_MESSAGE_ID,
                                        new Object[] {
                                            new Double(maximum) }));
                    component.setValid(false);
                }
                if (minimumSet &&
                    (converted < minimum)) {
                    context.addMessage(component.getClientId(context),
                                       ValidatorMessages.getMessage
                                       (context,
                                        MINIMUM_MESSAGE_ID,
                                        new Object[] {
                                            new Double(minimum) }));
                    component.setValid(false);
                }
            } catch (NumberFormatException e) {
                context.addMessage(component.getClientId(context),
                                   ValidatorMessages.getMessage
                                   (context, TYPE_MESSAGE_ID));
                component.setValid(false);
            }
        }

    }


    public boolean equals(Object otherObj) {

	if (!(otherObj instanceof DoubleRangeValidator)) {
	    return false;
	}
	DoubleRangeValidator other = (DoubleRangeValidator) otherObj;
	return ((maximum == other.maximum) &&
                (minimum == other.minimum) &&
		(maximumSet == other.maximumSet) &&
                (minimumSet == other.minimumSet));

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the specified attribute value, converted to a
     * <code>double</code>.</p>
     *
     * @param attributeValue The attribute value to be converted
     *
     * @exception NumberFormatException if conversion is not possible
     */
    private double doubleValue(Object attributeValue)
        throws NumberFormatException {

        if (attributeValue instanceof Number) {
            return ( ((Number) attributeValue).doubleValue() );
        } else {
            return (Double.parseDouble(attributeValue.toString()));
        }

    }


    // ----------------------------------------------------- StateHolder Methods
    

    public Object saveState(FacesContext context) {

        Object values[] = new Object[4];
        values[0] = new Double(maximum);
        values[1] = maximumSet ? Boolean.TRUE : Boolean.FALSE;
        values[2] = new Double(minimum);
        values[3] = minimumSet ? Boolean.TRUE : Boolean.FALSE;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        maximum = ((Double) values[0]).doubleValue();
        maximumSet = ((Boolean) values[1]).booleanValue();
        minimum = ((Double) values[2]).doubleValue();
        minimumSet = ((Boolean) values[3]).booleanValue();

    }


    private boolean transientValue = false;


    public boolean isTransient() {

        return (this.transientValue);

    }


    public void setTransient(boolean transientValue) {

        this.transientValue = transientValue;

    }

}
