/*
 * $Id: LongRangeValidator.java,v 1.14 2003/08/01 18:05:50 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import javax.faces.component.UIInput;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.application.Message;


/**
 * <p><strong>LongRangeValidator</strong> is a {@link Validator} that checks
 * the value of the corresponding component against specified minimum and
 * maximum values.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.</li>
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

public class LongRangeValidator extends ValidatorBase implements StateHolder {


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


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a {@link Validator} with no preconfigured limits.</p>
     */
    public LongRangeValidator() {

        super();

    }


    /**
     * <p>Construct a {@link Validator} with the specified preconfigured
     * limit.</p>
     *
     * @param maximum Maximum value to allow
     */
    public LongRangeValidator(long maximum) {

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
    public LongRangeValidator(long maximum, long minimum) {

        super();
        setMaximum(maximum);
        setMinimum(minimum);

    }


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
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     */
    public void setMaximum(long maximum) {

        this.maximum = maximum;
        this.maximumSet = true;
        if (this.minimumSet && (this.minimum > this.maximum)) {
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
     *
     * @exception IllegalArgumentException if a specified maximum value is
     *  less than a specified minimum value
     */
    public void setMinimum(long minimum) {

        this.minimum = minimum;
        this.minimumSet = true;
        if (this.maximumSet && (this.minimum > this.maximum)) {
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


    public void validate(FacesContext context, UIInput component) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        Object value = ((UIInput) component).getValue();
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
                    component.setValid(false);
                }
                if (isMinimumSet() &&
                    (converted < minimum)) {
                    context.addMessage(component,
                                       getMessage(context,
                                                  MINIMUM_MESSAGE_ID,
                                                  new Object[] {
                                           new Long(minimum) }));
                    component.setValid(false);
                }
            } catch (NumberFormatException e) {
                context.addMessage(component,
                                   getMessage(context, TYPE_MESSAGE_ID));
                component.setValid(false);
            }
        }

    }


    public boolean equals(Object otherObj) {
	if (!(otherObj instanceof LongRangeValidator)) {
	    return false;
	}
	LongRangeValidator other = (LongRangeValidator) otherObj;
	return (maximum == other.maximum && minimum == other.minimum &&
		maximumSet == other.maximumSet && minimumSet == other.minimumSet);
    }


    // -------------------------------------------------------- Private Methods


    /**
     * <p>Return the specified attribute value, converted to a
     * <code>long</code>.</p>
     *
     * @param attributeValue The attribute value to be converted
     *
     * @exception NumberFormatException if conversion is not possible
     */
    private long longValue(Object attributeValue)
        throws NumberFormatException {

        if (attributeValue instanceof Number) {
            return ( ((Number) attributeValue).longValue() );
        } else {
            return (Long.parseLong(attributeValue.toString()));
        }

    }


    // ------------------------------------------ methods from StateHolder
    
    public Object getState(FacesContext context) {
	return maximum + "_" + maximumSet + "_" + minimum + "_" + minimumSet;
    }

    public void restoreState(FacesContext context, Object state) {
	String stateStr = (String) state;
	int i = stateStr.indexOf("_"), j;
	maximum = Long.valueOf(stateStr.substring(0,i)).longValue();
	j = stateStr.indexOf("_", i + 1);
	maximumSet = Boolean.valueOf(stateStr.substring(i + 1, j)).booleanValue();
	i = stateStr.indexOf("_", j + 1);
	minimum = Long.valueOf(stateStr.substring(j + 1 ,i)).longValue();
	minimumSet = Boolean.valueOf(stateStr.substring(i + 1)).booleanValue();
    }

    public boolean isTransient() { return false;
    }

    public void setTransient(boolean newT) {}

}
