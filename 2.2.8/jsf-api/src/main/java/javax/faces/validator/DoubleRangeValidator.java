/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.validator;


import javax.faces.component.UIComponent;
import javax.faces.component.PartialStateHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * <p><strong
 * class="changed_modified_2_0_rev_a">DoubleRangeValidator</strong> is a
 * {@link Validator} that checks the value of the corresponding
 * component against specified minimum and maximum values.  The
 * following algorithm is implemented:</p>

 * <ul>
 * <li>If the passed value is <code>null</code>, exit immediately.</li>
 * <li>If the current component value is not a floating point type, or
 * a String that is convertible to double, throw a
 * {@link ValidatorException} containing a
 * TYPE_MESSAGE_ID message.</li>
 * <li>If both a <code>maximum</code> and <code>minimum</code> property
 * has been configured on this {@link Validator}, check the component
 * value against both limits.  If the component value is not within
 * this specified range, throw a {@link ValidatorException} containing a
 * {@link #NOT_IN_RANGE_MESSAGE_ID} message.</li>
 * <li>If a <code>maximum</code> property has been configured on this
 * {@link Validator}, check the component value against
 * this limit.  If the component value is greater than the
 * specified maximum, throw a {@link ValidatorException} containing a
 * MAXIMUM_MESSAGE_ID message.</li>
 * <li>If a <code>minimum</code> property has been configured on this
 * {@link Validator}, check the component value against
 * this limit.  If the component value is less than the
 * specified minimum, throw a {@link ValidatorException} containing a
 * MINIMUM_MESSAGE_ID message.</li>
 * </ul>
 * <p/>
 * <p>For all of the above cases that cause a {@link ValidatorException}
 * to be thrown, if there are parameters to the message that match up
 * with validator parameters, the values of these parameters must be
 * converted using the {@link Converter} registered in the application
 * under the converter id <code>javax.faces.Number</code>.  This allows
 * the values to be localized according to the current
 * <code>Locale</code>.</p>
 */

public class DoubleRangeValidator implements Validator, PartialStateHolder {

    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String VALIDATOR_ID = "javax.faces.DoubleRange";


    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage}
     * to be created if the maximum value check fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the configured maximum value.</li>
     * <li><code>{1}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String MAXIMUM_MESSAGE_ID =
         "javax.faces.validator.DoubleRangeValidator.MAXIMUM";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage}
     * to be created if the minimum value check fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the configured minimum value.</li>
     * <li><code>{1}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String MINIMUM_MESSAGE_ID =
         "javax.faces.validator.DoubleRangeValidator.MINIMUM";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the maximum or minimum value check fails, and both the maximum
     * and minimum values for this validator have been set.  The message
     * format string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the configured minimum value.</li>
     * <li><code>{1}</code> replaced by the configured maximum value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String NOT_IN_RANGE_MESSAGE_ID =
         "javax.faces.validator.DoubleRangeValidator.NOT_IN_RANGE";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage}
     * to be created if the current value of this component is not of the
     * correct type.   The message format string for this message may
     * optionally include a <code>{0}</code> placeholder that will be
     * replaced by a <code>String</code> whose value is the label of
     * the input component that produced this message.</p>
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
     */
    public DoubleRangeValidator(double maximum, double minimum) {

        super();
        setMaximum(maximum);
        setMinimum(minimum);

    }

    // -------------------------------------------------------------- Properties


    private Double maximum;

    /**
     * <p>Return the maximum value to be enforced by this {@link
     * Validator} or <code>Double.MAX_VALUE</code> if it has not been
     * set.</p>
     */
    public double getMaximum() {

        return (this.maximum != null ? this.maximum : Double.MAX_VALUE);

    }


    /**
     * <p>Set the maximum value to be enforced by this {@link Validator}.</p>
     *
     * @param maximum The new maximum value
     */
    public void setMaximum(double maximum) {

        clearInitialState();
        this.maximum = maximum;

    }


    private Double minimum;


    /**
     * <p>Return the minimum value to be enforced by this {@link
     * Validator}, or <code>Double.MIN_VALUE</code> if it has not been
     * set.</p>
     */
    public double getMinimum() {

        return (this.minimum != null ? this.minimum : Double.MIN_VALUE);

    }


    /**
     * <p>Set the minimum value to be enforced by this {@link Validator}.</p>
     *
     * @param minimum The new minimum value
     */
    public void setMinimum(double minimum) {

        clearInitialState();
        this.minimum = minimum;

    }

    // ------------------------------------------------------- Validator Methods

    /**
     * @throws NullPointerException {@inheritDoc}
     * @throws ValidatorException   {@inheritDoc}
     */
    public void validate(FacesContext context,
                         UIComponent component,
                         Object value) throws ValidatorException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        if (value != null) {
            try {
                double converted = doubleValue(value);
                if (isMaximumSet() &&
                     (converted > maximum)) {
                    if (isMinimumSet()) {
                        throw new ValidatorException(MessageFactory.getMessage
                             (context,
                                  NOT_IN_RANGE_MESSAGE_ID,
                                  stringValue(component, minimum, context),
                                  stringValue(component, maximum, context),
                                  MessageFactory.getLabel(context, component)));

                    } else {
                        throw new ValidatorException(MessageFactory.getMessage
                             (context,
                                  MAXIMUM_MESSAGE_ID,
                                  stringValue(component, maximum, context),
                                  MessageFactory.getLabel(context, component)));
                    }
                }
                if (isMinimumSet() &&
                     (converted < minimum)) {
                    if (isMaximumSet()) {
                        throw new ValidatorException(MessageFactory.getMessage
                             (context,
                                  NOT_IN_RANGE_MESSAGE_ID,
                                  stringValue(component, minimum, context),
                                  stringValue(component, maximum, context),
                                  MessageFactory.getLabel(context, component)));

                    } else {
                        throw new ValidatorException(MessageFactory.getMessage
                             (context,
                                  MINIMUM_MESSAGE_ID,
                                  stringValue(component, minimum, context),
                                  MessageFactory.getLabel(context, component)));
                    }
                }
            } catch (NumberFormatException e) {
                throw new ValidatorException(MessageFactory.getMessage
                     (context, TYPE_MESSAGE_ID,
                          MessageFactory.getLabel(context, component)), e);
            }
        }

    }


    public boolean equals(Object otherObj) {

        if (!(otherObj instanceof DoubleRangeValidator)) {
            return false;
        }
        DoubleRangeValidator other = (DoubleRangeValidator) otherObj;
        return ((this.getMaximum() == other.getMaximum())
                && (this.getMinimum() == other.getMinimum())
                && (this.isMaximumSet() == other.isMaximumSet())
                && (this.isMinimumSet() == other.isMinimumSet()));

    }


    public int hashCode() {

        int hashCode = (Double.valueOf(this.getMinimum()).hashCode()
             + Double.valueOf(this.getMaximum()).hashCode()
             + Boolean.valueOf(isMinimumSet()).hashCode()
             + Boolean.valueOf(isMaximumSet()).hashCode());
        return (hashCode);

    }

    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the specified attribute value, converted to a
     * <code>double</code>.</p>
     *
     * @param attributeValue The attribute value to be converted
     * @throws NumberFormatException if conversion is not possible
     */
    private static double doubleValue(Object attributeValue)
         throws NumberFormatException {

        if (attributeValue instanceof Number) {
            return (((Number) attributeValue).doubleValue());
        } else {
            return (Double.parseDouble(attributeValue.toString()));
        }

    }

    private static String stringValue(UIComponent component,
                                      Double toConvert,
                                      FacesContext context) {

        Converter converter = context.getApplication().createConverter("javax.faces.Number");
        return converter.getAsString(context, component, toConvert);

    }

    private boolean isMaximumSet() {

        return (maximum != null);

    }


    private boolean isMinimumSet() {

        return (minimum != null);

    }

    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[2];
            values[0] = maximum;
            values[1] = minimum;
            return (values);
        }
        return null;

    }


    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (state != null) {
            Object values[] = (Object[]) state;
            maximum = (Double) values[0];
            minimum = (Double) values[1];
        }

    }


    private boolean transientValue = false;


    public boolean isTransient() {

        return (this.transientValue);

    }


    public void setTransient(boolean transientValue) {

        this.transientValue = transientValue;

    }

    private boolean initialState;

    public void markInitialState() {
        initialState = true;
    }

    public boolean initialStateMarked() {
        return initialState;
    }

    public void clearInitialState() {
        initialState = false;
    }
}
