/*
 * $Id: StringRangeValidator.java,v 1.23 2003/09/30 17:37:47 rlubke Exp $
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
 * <p><strong>StringRangeValidator</strong> is a {@link Validator} that checks
 * the value of the corresponding component against specified minimum and
 * maximum values.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.</li>
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

public class StringRangeValidator implements Validator, StateHolder {


    // ------------------------------------------------------ Manifest Constants


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


    // ------------------------------------------------------------ Constructors


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


    // -------------------------------------------------------------- Properties


    private String maximum = null;


    /**
     * <p>Return the maximum value to be enforced by this {@link Validator}.</p>
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
        if ((this.minimum != null) &&
            (this.minimum.compareTo(this.maximum) > 0)) {
            throw new IllegalArgumentException();
        }

    }


    private String minimum = null;


    /**
     * <p>Return the minimum value to be enforced by this {@link Validator}.</p>
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
        if ((this.maximum != null) &&
            (this.minimum.compareTo(this.maximum) > 0)) {
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
            String converted = stringValue(value);
            if ((maximum != null) &&
                (converted.compareTo(maximum) > 0)) {
                context.addMessage(component,
                                   ValidatorMessages.getMessage
                                   (context,
                                    MAXIMUM_MESSAGE_ID,
                                    new Object[] {
                                        new String(maximum) }));
                component.setValid(false);
            }
            if ((minimum != null) &&
                (converted.compareTo(minimum) < 0)) {
                context.addMessage(component,
                                   ValidatorMessages.getMessage
                                   (context,
                                    MINIMUM_MESSAGE_ID,
                                    new Object[] {
                                        new String(minimum) }));
                component.setValid(false);
            }
        }

    }


    public boolean equals(Object otherObj) {

	if (!(otherObj instanceof StringRangeValidator)) {
	    return false;
	}
	StringRangeValidator other = (StringRangeValidator) otherObj;
        if ((maximum == null) && (other.maximum != null)) {
            return (false);
        } else if ((maximum != null) && (other.maximum == null)) {
            return (false);
        } else if ((maximum != null) && (other.maximum != null) &&
                   !maximum.equals(other.maximum)) {
            return (false);
        }
        if ((minimum == null) && (other.minimum != null)) {
            return (false);
        } else if ((minimum != null) && (other.minimum == null)) {
            return (false);
        } else if ((minimum != null) && (other.minimum != null) &&
                   !minimum.equals(other.minimum)) {
            return (false);
        }
        return (true);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the specified attribute value, converted to a
     * <code>String</code>.</p>
     *
     * @param attributeValue The attribute value to be converted
     */
    private String stringValue(Object attributeValue) {

        if (attributeValue == null) {
            return (null);
        } else if (attributeValue instanceof String) {
            return ((String) attributeValue);
        } else {
            return (attributeValue.toString());
        }

    }


    // ----------------------------------------------------- StateHolder Methods
    

    public Object saveState(FacesContext context) {

        Object values[] = new Object[2];
        values[0] = maximum;
        values[1] = minimum;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        maximum = (String) values[0];
        minimum = (String) values[1];

    }


    private boolean transientValue = false;


    public boolean isTransient() {

        return (this.transientValue);

    }

    public void setTransient(boolean transientValue) {

        this.transientValue = transientValue;

    }

}
