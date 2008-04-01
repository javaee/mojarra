/*
 * $Id: LengthValidator.java,v 1.4 2002/06/14 22:16:42 craigmcc Exp $
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
 * <p><strong>LengthValidator</strong> is a {@link Validator} that checks
 * the number of characters in the String representation of the value of the
 * associated component.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.  (If null values
 *     should not be allowed, a {@link RequiredValidator} can be configured
 *     to check for this case.)</li>
 * <li>Convert the value to a String, if necessary, by calling its
 *     <code>toString()</code> method.</li>
 * <li>If a MINIMUM_ATTRIBUTE_NAME attribute has been configured on this
 *     component, and it is an Integer, check the length of the converted
 *     String against this limit.  If the String length is less than the
 *     specified minimum, add a MINIMUM_MESSAGE_ID message to the
 *     {@link FacesContext} for this request.</li>
 * <li>If a MAXIMUM_ATTRIBUTE_NAME attribute has been configured on this
 *     component, and it is an Integer, check the length of the converted
 *     String against this limit.  If the String length is larger than the
 *     specified minimum, add a MAXIMUM_MESSAGE_ID message to the
 *     {@link FacesContext} for this request.</li>
 * </ul>
 */

public class LengthValidator extends ValidatorBase {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The attribute name of an <code>Integer</code> value representing
     * the maximum length to check for.</p>
     */
    public static final String MAXIMUM_ATTRIBUTE_NAME =
        "javax.faces.validator.LengthValidator.MAXIMUM";


    /**
     * <p>The attribute name of an <code>Integer</code> value representing
     * the minimum length to check for.</p>
     */
    public static final String MINIMUM_ATTRIBUTE_NAME =
        "javax.faces.validator.LengthValidator.MINIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * one of the limit attributes is not of the correct type.</p>
     */
    public static final String LIMIT_MESSAGE_ID =
        "javax.faces.validator.StringRangeValidator.LIMIT";


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


    // ----------------------------------------------------- Static Initializer


    static { // FIXME - i18n !!!

        descriptors.put
            (MAXIMUM_ATTRIBUTE_NAME,
             new AttributeDescriptorImpl(MAXIMUM_ATTRIBUTE_NAME,
                                         Integer.class,
                                         "Maximum Length",
                                         "The maximum number of characters " +
                                         "allowed for this value"));

        descriptors.put
            (MINIMUM_ATTRIBUTE_NAME,
             new AttributeDescriptorImpl(MINIMUM_ATTRIBUTE_NAME,
                                         Integer.class,
                                         "Minimum Length",
                                         "The minimum number of characters " +
                                         "allowed for this value"));

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
            String svalue = value.toString();
            checkMaximum(context, component, svalue);
            checkMinimum(context, component, svalue);
        }

    }


    // -------------------------------------------------------- Private Methods


    /**
     * <p>Check the specified value against the maximum length constraint
     * (if any).</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     * @param svalue String version of the component value to be checked
     */
    private void checkMaximum(FacesContext context, UIComponent component,
                              String svalue) {

        Integer attribute = null;
        try {
            attribute = (Integer)
                component.getAttribute(MAXIMUM_ATTRIBUTE_NAME);
            if (attribute == null) {
                return;
            }
        } catch (ClassCastException e) {
            context.addMessage(component,
                               getMessage(context, LIMIT_MESSAGE_ID));
            return;
        }
        if (svalue.length() > attribute.intValue()) {
            context.addMessage(component,
                               getMessage(context, MAXIMUM_MESSAGE_ID,
                                         new Object[] { attribute }));
        }

    }


    /**
     * <p>Check the specified value against the minimum length constraint
     * (if any).</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     * @param svalue String version of the component value to be checked
     */
    private void checkMinimum(FacesContext context, UIComponent component,
                              String svalue) {

        Integer attribute = null;
        try {
            attribute = (Integer)
                component.getAttribute(MINIMUM_ATTRIBUTE_NAME);
            if (attribute == null) {
                return;
            }
        } catch (ClassCastException e) {
            context.addMessage(component,
                               getMessage(context, LIMIT_MESSAGE_ID));
            return;
        }
        if (svalue.length() < attribute.intValue()) {
            context.addMessage(component,
                               getMessage(context, MINIMUM_MESSAGE_ID,
                                         new Object[] { attribute }));
        }

    }


}
