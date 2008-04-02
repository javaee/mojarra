/*
 * $Id: DoubleRangeValidator.java,v 1.42 2004/06/11 14:56:15 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import javax.faces.convert.Converter;

/**
 * <p><strong>DoubleRangeValidator</strong> is a {@link Validator} that checks
 * the value of the corresponding component against specified minimum and
 * maximum values.  The following algorithm is implemented:</p>
 * <ul>
 * <li>If the passed value is <code>null</code>, exit immediately.</li>
 * <li>If the current component value is not a floating point type, or
 *     a String that is convertible to double, throw a 
 *     {@link ValidatorException} containing a
 *     TYPE_MESSAGE_ID message.</li>
 * <li>If both a <code>maximum</code> and <code>minimum</code> property
 *     has been configured on this {@link Validator}, check the component
 *     value against both limits.  If the component value is not within
 *     this specified range, throw a {@link ValidatorException} containing a
 *     {@link Validator#NOT_IN_RANGE_MESSAGE_ID} message.</li>
 * <li>If a <code>maximum</code> property has been configured on this
 *     {@link Validator}, check the component value against
 *     this limit.  If the component value is greater than the
 *     specified maximum, throw a {@link ValidatorException} containing a
 *     MAXIMUM_MESSAGE_ID message.</li>
 * <li>If a <code>minimum</code> property has been configured on this
 *     {@link Validator}, check the component value against
 *     this limit.  If the component value is less than the
 *     specified minimum, throw a {@link ValidatorException} containing a
 *     MINIMUM_MESSAGE_ID message.</li>
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

public class DoubleRangeValidator implements Validator, StateHolder {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String VALIDATOR_ID = "javax.faces.DoubleRange";


    /**
     * <p>The message identifier of the {@link FacesMessage}
     * to be created if the maximum value check fails.  The message format
     * string for this message may optionally include a <code>{0}</code>
     * placeholder, which will be replaced by the configured maximum value.</p>
     */
    public static final String MAXIMUM_MESSAGE_ID =
        "javax.faces.validator.DoubleRangeValidator.MAXIMUM";

    /**
     * <p>The message identifier of the {@link FacesMessage}
     * to be created if the minimum value check fails.  The message format
     * string for this message may optionally include a <code>{0}</code>
     * placeholder, which will be replaced by the configured minimum value.</p>
     */
    public static final String MINIMUM_MESSAGE_ID =
        "javax.faces.validator.DoubleRangeValidator.MINIMUM";


    /**
     * <p>The message identifier of the {@link FacesMessage}
     * to be created if the current value of this component is not of the
     * correct type.</p>
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
     */
    public DoubleRangeValidator(double maximum, double minimum) {

        super();
        setMaximum(maximum);
        setMinimum(minimum);

    }


    // -------------------------------------------------------------- Properties


    private double maximum = Double.MAX_VALUE;
    private boolean maximumSet = false;


    /**
     * <p>Return the maximum value to be enforced by this {@link
     * Validator} or <code>Double.MAX_VALUE</code> if it has not been
     * set.</p>
     */
    public double getMaximum() {

        return (this.maximum);

    }


    /**
     * <p>Set the maximum value to be enforced by this {@link Validator}.</p>
     *
     * @param maximum The new maximum value
     *
     */
    public void setMaximum(double maximum) {

        this.maximum = maximum;
        this.maximumSet = true;
    }


    private double minimum = Double.MIN_VALUE;
    private boolean minimumSet = false;


    /**
     * <p>Return the minimum value to be enforced by this {@link
     * Validator}, or <code>Double.MIN_VALUE</code> if it has not been
     * set.</p>
     */
    public double getMinimum() {

        return (this.minimum);

    }


    /**
     * <p>Set the minimum value to be enforced by this {@link Validator}.</p>
     *
     * @param minimum The new minimum value
     *
     */
    public void setMinimum(double minimum) {

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
            try {
                double converted = doubleValue(value);
                if (maximumSet &&
                    (converted > maximum)) {
		    if (minimumSet) {
                        throw new ValidatorException(MessageFactory.getMessage
					   (context,
					    Validator.NOT_IN_RANGE_MESSAGE_ID,
					    new Object[] {
						stringValue(component, new Double(minimum)),
						stringValue(component, new Double(maximum)) }));
			
		    }
		    else {
                        throw new ValidatorException(MessageFactory.getMessage
					   (context,
					    MAXIMUM_MESSAGE_ID,
					    new Object[] {
				            stringValue(component, new Double(maximum)) }));
		    }
                }
                if (minimumSet &&
                    (converted < minimum)) {
		    if (maximumSet) {
                        throw new ValidatorException(MessageFactory.getMessage
					   (context,
					    Validator.NOT_IN_RANGE_MESSAGE_ID,
					    new Object[] {
				            stringValue(component, new Double(minimum)),
				            stringValue(component, new Double(maximum)) }));
			
		    }
		    else {
                        throw new ValidatorException(MessageFactory.getMessage
					   (context,
					    MINIMUM_MESSAGE_ID,
					    new Object[] {
					    stringValue(component, new Double(minimum)) }));
		    }
                }
            } catch (NumberFormatException e) {
                throw new ValidatorException(MessageFactory.getMessage
                                   (context, TYPE_MESSAGE_ID));
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

    private String stringValue(UIComponent component, Double toConvert) {
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
