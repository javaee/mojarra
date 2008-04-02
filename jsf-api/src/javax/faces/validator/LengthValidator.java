/*
 * $Id: LengthValidator.java,v 1.44 2005/03/11 21:05:28 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.convert.Converter;

/**
 * <p><strong>LengthValidator</strong> is a {@link Validator} that checks
 * the number of characters in the String representation of the value of the
 * associated component.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Convert the passed value to a String, if necessary, by calling its
 *     <code>toString()</code> method.</li>
 * <li>If a <code>maximum</code> property has been configured on this
 *     {@link Validator}, check the length of the converted
 *     String against this limit.  If the String length is larger than the
 *     specified maximum, throw a {@link ValidatorException} containing a
 *     a MAXIMUM_MESSAGE_ID message.</li>
 * <li>If a <code>minimum</code> property has been configured on this
 *     {@link Validator}, check the length of the converted
 *     String against this limit.  If the String length is less than the
 *     specified minimum, throw a {@link ValidatorException} containing a
 *     a MINIMUM_MESSAGE_ID message.</li>
 * </ul>
 * 
 * <p>For all of the above cases that cause a {@link ValidatorException}
 * to be thrown, if there are parameters to the message that match up
 * with validator parameters, the values of these parameters must be
 * converted using the {@link Converter} registered in the application
 * under the converter id <code>javax.faces.Number</code>.  This allows
 * the values to be localized according to the current
 * <code>Locale</code>.</p>
 */

public class LengthValidator implements Validator, StateHolder {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard validator id for this validator.</p>
     */
    public static final String VALIDATOR_ID = "javax.faces.Length";


    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the maximum length check fails.   The message format string for 
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the configured maximum length.</li>
     * <li><code>{1}</code> replaced by a <code>String</code> whose value
     *   is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String MAXIMUM_MESSAGE_ID =
        "javax.faces.validator.LengthValidator.MAXIMUM";


    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the minimum length check fails.   The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the configured minimum length.</li>
     * <li><code>{1}</code> replaced by a <code>String</code> whose value
     *   is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String MINIMUM_MESSAGE_ID =
        "javax.faces.validator.LengthValidator.MINIMUM";


    // ------------------------------------------------------------ Constructors


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
     */
    public LengthValidator(int maximum, int minimum) {

        super();
        setMaximum(maximum);
        setMinimum(minimum);

    }


    // -------------------------------------------------------------- Properties


    private int maximum = 0;
    private boolean maximumSet = false;


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
     */
    public void setMaximum(int maximum) {

        this.maximum = maximum;
        this.maximumSet = true;
    }


    private int minimum = 0;
    private boolean minimumSet = false;


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
     */
    public void setMinimum(int minimum) {

        this.minimum = minimum;
        this.minimumSet = true;

    }


    // ------------------------------------------------------- Validator Methods

    /**
     * @exception NullPointerException {@inheritDoc}     
     * @exception ValidatorException {@inheritDoc}     
     */ 
    public void validate(FacesContext context,
                         UIComponent  component,
                         Object       value) throws ValidatorException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        if (value != null) {
            String converted = stringValue(value);
            if (maximumSet &&
                (converted.length() > maximum)) {
                throw new ValidatorException(MessageFactory.getMessage
                                   (context,
                                    MAXIMUM_MESSAGE_ID,
                                    new Object[] {
                                    integerToString(component, 
						    new Integer(maximum)),
                                     MessageFactory.getLabel(context, component)} ));
            }
            if (minimumSet &&
                (converted.length() < minimum)) {
                throw new ValidatorException(MessageFactory.getMessage
                                   (context,
                                    MINIMUM_MESSAGE_ID,
                                    new Object[] {
                                    integerToString(component,
						    new Integer(minimum)),
                                     MessageFactory.getLabel(context, component)}));
            }
        }

    }


    public boolean equals(Object otherObj) {

	if (!(otherObj instanceof LengthValidator)) {
	    return false;
	}
	LengthValidator other = (LengthValidator) otherObj;
	return ((maximum == other.maximum) &&
                (minimum == other.minimum) &&
		(maximumSet == other.maximumSet) &&
                (minimumSet == other.minimumSet));

    }


    // -------------------------------------------------------- Private Methods


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

    private String integerToString(UIComponent component, Integer toConvert) {
	String result = null;
	Converter converter = null;
	FacesContext context = FacesContext.getCurrentInstance();

	converter = (Converter)
	    context.getApplication().createConverter("javax.faces.Number");
	result = converter.getAsString(context, component, toConvert);
	return result;
    }

    // ----------------------------------------------------- StateHolder Methods
    

    public Object saveState(FacesContext context) {

        Object values[] = new Object[4];
        values[0] = new Integer(maximum);
        values[1] = maximumSet ? Boolean.TRUE : Boolean.FALSE;
        values[2] = new Integer(minimum);
        values[3] = minimumSet ? Boolean.TRUE : Boolean.FALSE;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        maximum = ((Integer) values[0]).intValue();
        maximumSet = ((Boolean) values[1]).booleanValue();
        minimum = ((Integer) values[2]).intValue();
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
