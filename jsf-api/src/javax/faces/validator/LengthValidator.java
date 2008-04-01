/*
 * $Id: LengthValidator.java,v 1.1 2002/06/03 19:27:27 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageList;


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
 * <li>If a MINIMUM_LENGTH_ATTRIBUTE attribute has been configured on this
 *     component, and it is a legal integer, check the length of the converted
 *     String against this limit.  If the String length is less than the
 *     specified minimum, add a MINIMUM_MESSAGE_ID message to the
 *     {@link MessageList} for this request.</li>
 * <li>If a MAXIMUM_LENGTH_ATTRIBUTE attribute has been configured on this
 *     component, and it is a legal integer, check the length of the converted
 *     String against this limit.  If the String length is larger than the
 *     specified minimum, add a MAXIMUM_MESSAGE_ID message to the
 *     {@link MessageList} for this request.</li>
 * </ul>
 */

public final class LengthValidator extends Validator {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The attribute name of an <code>Integer</code> value representing
     * the maximum length to check for.</p>
     */
    public static final String MAXIMUM_ATTRIBUTE =
        "javax.faces.component.LengthValidator.MAXIMUM";


    /**
     * <p>The attribute name of an <code>Integer</code> value representing
     * the minimum length to check for.</p>
     */
    public static final String MINIMUM_ATTRIBUTE =
        "javax.faces.component.LengthValidator.MINIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the minimum length check fails.
     */
    public static final String MAXIMUM_MESSAGE_ID =
        "javax.faces.component.LengthValidator.MAXIMUM";


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the minimum length check fails.
     */
    public static final String MINIMUM_MESSAGE_ID =
        "javax.faces.component.LengthValidator.MINIMUM";


    // ------------------------------------------------------- Static Variables


    /**
     * <p>The set of {@link AttributeDescriptor}s for the attributes supported
     * by this {@link Validator}.</p>
     */
    private static HashMap descriptors = new HashMap();

    static { // FIXME - i18n !!!
        descriptors.put
            (MAXIMUM_ATTRIBUTE,
             new AttributeDescriptorImpl(MAXIMUM_ATTRIBUTE,
                                         Integer.class,
                                         "Maximum Length",
                                         "The maximum number of characters " +
                                         "allowed for this value"));
        descriptors.put
            (MINIMUM_ATTRIBUTE,
             new AttributeDescriptorImpl(MINIMUM_ATTRIBUTE,
                                         Integer.class,
                                         "Minimum Length",
                                         "The minimum number of characters " +
                                         "allowed for this value"));
    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return an {@link AttributeDescriptor} for the specified attribute
     * name that is supported by this <code>Validator</code>.</p>
     *
     * @param name The requested attribute name
     *
     * @exception IllegalArgumentException if this attribute is not
     *  supported by this <code>Validator</code>.
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public AttributeDescriptor getAttributeDescriptor(String name) {

        if (name == null) {
            throw new NullPointerException();
        }
        AttributeDescriptor descriptor =
            (AttributeDescriptor) descriptors.get(name);
        if (descriptor != null) {
            return (descriptor);
        } else {
            throw new IllegalArgumentException(name);
        }

    }


    /**
     * <p>Return an <code>Iterator</code> over the names of the supported
     * attributes for this <code>Validator</code>.  If no attributes are
     * supported, an empty <code>Iterator</code> is returned.</p>
     */
    public Iterator getAttributeNames() {

        return (descriptors.keySet().iterator());

    }


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
            attribute = (Integer) component.getAttribute(MAXIMUM_ATTRIBUTE);
            if (attribute == null) {
                return;
            }
        } catch (ClassCastException e) {
            return; // FIXME - throw IllegalArgumentException instead?
        }
        if (svalue.length() > attribute.intValue()) {
            context.getMessageList().add(MAXIMUM_MESSAGE_ID,
                                         component.getCompoundId(),
                                         new Object[] { attribute });
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
            attribute = (Integer) component.getAttribute(MINIMUM_ATTRIBUTE);
            if (attribute == null) {
                return;
            }
        } catch (ClassCastException e) {
            return; // FIXME - throw IllegalArgumentException instead?
        }
        if (svalue.length() < attribute.intValue()) {
            context.getMessageList().add(MINIMUM_MESSAGE_ID,
                                         component.getCompoundId(),
                                         new Object[] { attribute });
        }

    }


}
