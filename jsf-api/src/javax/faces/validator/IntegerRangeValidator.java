/*
 * $Id: IntegerRangeValidator.java,v 1.1 2002/06/03 22:24:29 craigmcc Exp $
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
import javax.faces.context.MessageList;


/**
 * <p><strong>IntegerRangeValidator</strong> is a {@link Validator} that checks
 * the value of the corresponding component against specified minimum and
 * maximum values.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.  (If null values
 *     should not be allowed, a {@link RequiredValidator} can be configured
 *     to check for this case.)</li>
 * <li>If the current component value is not an <code>Integer</code>,
 *     add a TYPE_MESSAGE_ID message to the {@link MessageList} for this
 *     request, and skip subsequent checks.</li>
 * <li>If a MAXIMUM_ATTRIBUTE_NAME attribute has been configured on this
 *     component, and it is a Integer, check the component value against
 *     this limit.  If the component value is greater than the
 *     specified minimum, add a MAXIMUM_MESSAGE_ID message to the
 *     {@link MessageList} for this request.</li>
 * <li>If a MINIMUM_ATTRIBUTE_NAME attribute has been configured on this
 *     component, and it is a Integer, check the component value against
 *     this limit.  If the component value is less than the
 *     specified minimum, add a MINIMUM_MESSAGE_ID message to the
 *     {@link MessageList} for this request.</li>
 * </ul>
 */

public final class IntegerRangeValidator extends ValidatorImpl {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The attribute name of an <code>Integer</code> representing
     * the maximum value to check for.</p>
     */
    public static final String MAXIMUM_ATTRIBUTE_NAME =
        "javax.faces.validator.IntegerRangeValidator.MAXIMUM";


    /**
     * <p>The attribute name of an <code>Integer</code> representing
     * the minimum value to check for.</p>
     */
    public static final String MINIMUM_ATTRIBUTE_NAME =
        "javax.faces.validator.IntegerRangeValidator.MINIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * one of the limit attributes is not of the correct type.</p>
     */
    public static final String LIMIT_MESSAGE_ID =
        "javax.faces.validator.StringRangeValidator.LIMIT";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the maximum value check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured maximum value.</p>
     */
    public static final String MAXIMUM_MESSAGE_ID =
        "javax.faces.validator.IntegerRangeValidator.MAXIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the minimum value check fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured minimum value.</p>
     */
    public static final String MINIMUM_MESSAGE_ID =
        "javax.faces.validator.IntegerRangeValidator.MINIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the current value of this component is not of the correct type.
     */
    public static final String TYPE_MESSAGE_ID =
        "javax.faces.validator.IntegerRangeValidator.TYPE";


    // ----------------------------------------------------- Static Initializer


    static { // FIXME - i18n !!!

        descriptors.put
            (MAXIMUM_ATTRIBUTE_NAME,
             new AttributeDescriptorImpl(MAXIMUM_ATTRIBUTE_NAME,
                                         Integer.class,
                                         "Maximum Value",
                                         "The maximum allowed value"));

        descriptors.put
            (MINIMUM_ATTRIBUTE_NAME,
             new AttributeDescriptorImpl(MINIMUM_ATTRIBUTE_NAME,
                                         Integer.class,
                                         "Minimum Value",
                                         "The minimum allowed value"));

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Perform the correctness checks implemented by this
     * <code>Validator</code> against the specified {@link UIComponent}.
     * Add {@link Message}s describing any correctness violations to the
     * {@link MessageList} associated with the specified {@link FacesContext}.
     * </p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     */
    public void validate(FacesContext context, UIComponent component) {

        Object value = component.getValue();
        if (value != null) {
            try {
                Integer converted = (Integer) value;
                checkMaximum(context, component, converted);
                checkMinimum(context, component, converted);
            } catch (ClassCastException e) {
                context.getMessageList().add(TYPE_MESSAGE_ID,
                                             component.getCompoundId());
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
                              Integer value) {

        Integer attribute = null;
        try {
            attribute = (Integer)
                component.getAttribute(MAXIMUM_ATTRIBUTE_NAME);
            if (attribute == null) {
                return;
            }
        } catch (ClassCastException e) {
            context.getMessageList().add(LIMIT_MESSAGE_ID,
                                         component.getCompoundId());
            return;
        }

        if (value.compareTo(attribute) > 0) {
            context.getMessageList().add(MAXIMUM_MESSAGE_ID,
                                         component.getCompoundId(),
                                         new Object[] { attribute });
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
                              Integer value) {

        Integer attribute = null;
        try {
            attribute = (Integer)
                component.getAttribute(MINIMUM_ATTRIBUTE_NAME);
            if (attribute == null) {
                return;
            }
        } catch (ClassCastException e) {
            context.getMessageList().add(LIMIT_MESSAGE_ID,
                                         component.getCompoundId());
            return;
        }

        if (value.compareTo(attribute) < 0) {
            context.getMessageList().add(MINIMUM_MESSAGE_ID,
                                         component.getCompoundId(),
                                         new Object[] { attribute });
        }

    }


}
