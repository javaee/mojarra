/*
 * $Id: LongRangeValidator.java,v 1.1 2002/07/28 23:16:58 craigmcc Exp $
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
 * <p><strong>LongRangeValidator</strong> is a {@link Validator} that checks
 * the value of the corresponding component against specified minimum and
 * maximum values.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.  (If null values
 *     should not be allowed, a {@link RequiredValidator} can be configured
 *     to check for this case.)</li>
 * <li>If the current component value is not an integer type (byte, char,
 *     short, int, long), or a String that can be converted to a long,
 *     add a TYPE_MESSAGE_ID message to the {@link FacesContext} for this
 *     request, and skip subsequent checks.</li>
 * <li>If a MAXIMUM_ATTRIBUTE_NAME attribute has been configured on this
 *     component, and it is an integer type (or a String that can be
 *     converted to a long), check the component value against
 *     this limit.  If the component value is greater than the
 *     specified minimum, add a MAXIMUM_MESSAGE_ID message to the
 *     {@link FacesContext} for this request.</li>
 * <li>If a MINIMUM_ATTRIBUTE_NAME attribute has been configured on this
 *     component, and it is an integer type (or a String that can be
 *     converted to a long), check the component value against
 *     this limit.  If the component value is less than the
 *     specified minimum, add a MINIMUM_MESSAGE_ID message to the
 *     {@link FacesContext} for this request.</li>
 * </ul>
 */

public class LongRangeValidator extends ValidatorBase {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The attribute name of an integer value representing
     * the maximum value to check for.</p>
     */
    public static final String MAXIMUM_ATTRIBUTE_NAME =
        "javax.faces.validator.LongRangeValidator.MAXIMUM";


    /**
     * <p>The attribute name of an integer value representing
     * the minimum value to check for.</p>
     */
    public static final String MINIMUM_ATTRIBUTE_NAME =
        "javax.faces.validator.LongRangeValidator.MINIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * one of the limit attributes is not of the correct type.</p>
     */
    public static final String LIMIT_MESSAGE_ID =
        "javax.faces.validator.LongRangeValidator.LIMIT";


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


    // ----------------------------------------------------- Static Initializer


    static { // FIXME - i18n !!!

        descriptors.put
            (MAXIMUM_ATTRIBUTE_NAME,
             new AttributeDescriptorImpl(MAXIMUM_ATTRIBUTE_NAME,
                                         Long.class,
                                         "maxValue"));

        descriptors.put
            (MINIMUM_ATTRIBUTE_NAME,
             new AttributeDescriptorImpl(MINIMUM_ATTRIBUTE_NAME,
                                         Long.class,
                                         "minValue"));

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
     */
    public void validate(FacesContext context, UIComponent component) {

        Object value = component.getValue();
        if (value != null) {
            try {
                long converted = longValue(value);
                checkMaximum(context, component, converted);
                checkMinimum(context, component, converted);
            } catch (NumberFormatException e) {
                context.addMessage(component,
                                   getMessage(context, TYPE_MESSAGE_ID));
                return;
            }
        }

    }


    // -------------------------------------------------------- Private Methods


    /**
     * <p>Check the specified value against the maximum constraint
     * (if any).</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     * @param value Component value being checked
     */
    private void checkMaximum(FacesContext context, UIComponent component,
                              long value) {

        long attribute = 0L;
	Object attrObj = null;
        try {
	    attrObj = component.getAttribute(MAXIMUM_ATTRIBUTE_NAME);
            if (attrObj == null) {
                return;
            }
            attribute = longValue(attrObj);
        } catch (NumberFormatException e) {
            context.addMessage(component,
                               getMessage(context, LIMIT_MESSAGE_ID));
            return;
        }

        if (value > attribute) {
            context.addMessage(component,
                               getMessage(context, MAXIMUM_MESSAGE_ID,
                                         new Object[]
                               { new Long(attribute) }));
        }

    }


    /**
     * <p>Check the specified value against the minimum constraint
     * (if any).</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     * @param value Component value being checked
     */
    private void checkMinimum(FacesContext context, UIComponent component,
                              long value) {

        long attribute = 0L;
	Object attrObj = null;
        try {
	    attrObj = component.getAttribute(MINIMUM_ATTRIBUTE_NAME);
            if (attrObj == null) {
                return;
            }
            attribute = longValue(attrObj);
        } catch (NumberFormatException e) {
            context.addMessage(component,
                               getMessage(context, LIMIT_MESSAGE_ID));
            return;
        }

        if (value < attribute) {
            context.addMessage(component,
                               getMessage(context, MINIMUM_MESSAGE_ID,
                                         new Object[]
                               { new Long(attribute) }));
        }

    }


}
