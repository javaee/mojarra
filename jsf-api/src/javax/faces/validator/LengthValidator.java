/*
 * $Id: LengthValidator.java,v 1.20 2003/07/28 22:19:05 eburns Exp $
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
 * <p><strong>LengthValidator</strong> is a {@link Validator} that checks
 * the number of characters in the String representation of the value of the
 * associated component.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.</li>
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

public class LengthValidator extends ValidatorBase implements StateHolder {


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
     * <p>Return the maximum length to be enforced by this {@link
     * Validator}, or <code>0</code> if the maximum has not been
     * set.</p>
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
     * <p>Return the minimum length to be enforced by this {@link
     * Validator}, or <code>0</code> if the minimum has not been
     * set.</p>
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

    public void validate(FacesContext context, UIInput component) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        Object value = ((UIInput) component).getValue();
        if (value != null) {
            String converted = stringValue(value);
            if (isMaximumSet() &&
                (converted.length() > maximum)) {
                context.addMessage(component,
                                   getMessage(context,
                                              MAXIMUM_MESSAGE_ID,
                                              new Object[] {
                                       new Integer(maximum) } ));
                component.setValid(false);
            }
            if (isMinimumSet() &&
                (converted.length() < minimum)) {
                context.addMessage(component,
                                   getMessage(context,
                                              MINIMUM_MESSAGE_ID,
                                              new Object[] {
                                       new Integer(minimum) } ));
                component.setValid(false);
            }
        }

    }

    public boolean equals(Object otherObj) {
	if (!(otherObj instanceof LengthValidator)) {
	    return false;
	}
	LengthValidator other = (LengthValidator) otherObj;
	return (maximum == other.maximum && minimum == other.minimum &&
		maximumSet == other.maximumSet && minimumSet == other.minimumSet);
    }

    // ------------------------------------------ methods from StateHolder
    
    public Object getState(FacesContext context) {
	return maximum + "_" + maximumSet + "_" + minimum + "_" + minimumSet;
    }

    public void restoreState(FacesContext context, Object state) {
	String stateStr = (String) state;
	int i = stateStr.indexOf("_"), j;
	maximum = Integer.valueOf(stateStr.substring(0,i)).intValue();
	j = stateStr.indexOf("_", i + 1);
	maximumSet = Boolean.valueOf(stateStr.substring(i + 1, j)).booleanValue();
	i = stateStr.indexOf("_", j + 1);
	minimum = Integer.valueOf(stateStr.substring(j + 1 ,i)).intValue();
	minimumSet = Boolean.valueOf(stateStr.substring(i + 1)).booleanValue();
    }

    public boolean isTransient() { return false;
    }

    public void setTransient(boolean newT) {}    



}
