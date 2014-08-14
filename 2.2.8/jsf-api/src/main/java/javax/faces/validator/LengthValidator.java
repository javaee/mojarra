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
 * class="changed_modified_2_0_rev_a">LengthValidator</strong> is a
 * {@link Validator} that checks the number of characters in the String
 * representation of the value of the associated component.  The
 * following algorithm is implemented:</p>

 * <ul>
 * <li>Convert the passed value to a String, if necessary, by calling its
 * <code>toString()</code> method.</li>
 * <li>If a <code>maximum</code> property has been configured on this
 * {@link Validator}, check the length of the converted
 * String against this limit.  If the String length is larger than the
 * specified maximum, throw a {@link ValidatorException} containing a
 * a MAXIMUM_MESSAGE_ID message.</li>
 * <li>If a <code>minimum</code> property has been configured on this
 * {@link Validator}, check the length of the converted
 * String against this limit.  If the String length is less than the
 * specified minimum, throw a {@link ValidatorException} containing a
 * a MINIMUM_MESSAGE_ID message.</li>
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

public class LengthValidator implements Validator, PartialStateHolder {

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
     * is the label of the input component that produced this message.</li>
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
     * is the label of the input component that produced this message.</li>
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
     */
    public LengthValidator(int maximum, int minimum) {

        super();
        setMaximum(maximum);
        setMinimum(minimum);

    }

    // -------------------------------------------------------------- Properties


    private Integer maximum;


    /**
     * <p>Return the maximum length to be enforced by this {@link
     * Validator}, or <code>0</code> if the maximum has not been
     * set.</p>
     */
    public int getMaximum() {

        return (this.maximum != null ? this.maximum : 0);

    }


    /**
     * <p>Set the maximum length to be enforced by this {@link Validator}.</p>
     *
     * @param maximum The new maximum value
     */
    public void setMaximum(int maximum) {

        clearInitialState();
        this.maximum = maximum;

    }


    private Integer minimum;


    /**
     * <p>Return the minimum length to be enforced by this {@link
     * Validator}, or <code>0</code> if the minimum has not been
     * set.</p>
     */
    public int getMinimum() {

        return (this.minimum != null ? this.minimum : 0);

    }


    /**
     * <p>Set the minimum length to be enforced by this {@link Validator}.</p>
     *
     * @param minimum The new minimum value
     */
    public void setMinimum(int minimum) {

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
            String converted = stringValue(value);
            if (isMaximumSet() &&
                 (converted.length() > maximum)) {
                throw new ValidatorException(MessageFactory.getMessage
                     (context,
                          MAXIMUM_MESSAGE_ID,
                          integerToString(component, maximum, context),
                          MessageFactory.getLabel(context, component)));
            }
            if (isMinimumSet() &&
                 (converted.length() < minimum)) {
                throw new ValidatorException(MessageFactory.getMessage
                     (context,
                          MINIMUM_MESSAGE_ID,
                          integerToString(component, minimum, context),
                          MessageFactory.getLabel(context, component)));
            }
        }

    }


    public boolean equals(Object otherObj) {

        if (!(otherObj instanceof LengthValidator)) {
            return false;
        }
        LengthValidator other = (LengthValidator) otherObj;
        return ((this.getMaximum() == other.getMaximum())
                && (this.getMinimum() == other.getMinimum())
                && (this.isMinimumSet() == other.isMinimumSet())
                && (this.isMaximumSet() == other.isMaximumSet()));

    }

    public int hashCode() {

        int hashCode = (Integer.valueOf(getMinimum()).hashCode()
                         + Integer.valueOf(getMaximum()).hashCode()
                         + Boolean.valueOf(isMaximumSet()).hashCode()
                         + Boolean.valueOf(isMinimumSet()).hashCode());
        return (hashCode);

    }

    // -------------------------------------------------------- Private Methods


    /**
     * <p>Return the specified attribute value, converted to a
     * <code>String</code>.</p>
     *
     * @param attributeValue The attribute value to be converted
     */
    private static String stringValue(Object attributeValue) {

        if (attributeValue == null) {
            return (null);
        } else if (attributeValue instanceof String) {
            return ((String) attributeValue);
        } else {
            return (attributeValue.toString());
        }

    }

    private static String integerToString(UIComponent component,
                                          Integer toConvert,
                                          FacesContext context) {

        Converter converter =
             context.getApplication().createConverter("javax.faces.Number");
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
            maximum = (Integer) values[0];
            minimum = (Integer) values[1];
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
