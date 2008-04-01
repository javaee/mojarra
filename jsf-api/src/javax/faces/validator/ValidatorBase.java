/*
 * $Id: ValidatorBase.java,v 1.6 2002/08/29 05:39:14 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import javax.faces.FactoryFinder;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageResources;
import javax.faces.context.MessageResourcesFactory;


/**
 * <p>Internal abstract implementation of {@link Validator} used by classes
 * within this package.  This class is <strong>NOT</strong> part of the public
 * API of JavaServer Faces.</p>
 */

abstract class ValidatorBase implements Validator {


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Perform the correctness checks implemented by this
     * <code>Validator</code> against the specified {@link UIComponent}.
     * Add {@link Message}s describing any correctness violations to the
     * specified {@link FacesContext}.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     *
     * @return <code>true</code> if all validations performed by this
     *  method passed successfully, or <code>false</code> if one or more
     *  validations performed by this method failed
     */
    public abstract boolean validate(FacesContext context,
                                     UIComponent component);


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Return a {@link Message} for the specified parameters.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     */
    protected Message getMessage(FacesContext context, String messageId) {

        return (getMessageResources().getMessage(context, messageId));

    }


    /**
     * <p>Return a {@link Message} for the specified parameters.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     * @param params Substitution parameters for this message
     */
    protected Message getMessage(FacesContext context, String messageId,
                                 Object params[]) {

        return (getMessageResources().getMessage(context, messageId, params));

    }


    private MessageResources resources = null;

    /**
     * <p>Return the {@link MessageResources} instance for the message
     * resources defined by the JavaServer Faces Specification.
     */
    protected synchronized MessageResources getMessageResources() {

        if (resources == null) {
            MessageResourcesFactory factory = (MessageResourcesFactory)
                FactoryFinder.getFactory
                (FactoryFinder.MESSAGE_RESOURCES_FACTORY);
            resources = factory.getMessageResources
                (MessageResourcesFactory.FACES_API_MESSAGES);
        }
        return (resources);

    }


    /**
     * <p>Return the specified attribute value, converted to a
     * <code>double</code>.</p>
     *
     * @param attributeValue The attribute value to be converted
     *
     * @exception NumberFormatException if conversion is not possible
     */
    protected double doubleValue(Object attributeValue)
        throws NumberFormatException {

        if (attributeValue instanceof Float) {
            return ( ((Float) attributeValue).doubleValue() );
        } else if (attributeValue instanceof Double) {
            return ( ((Double) attributeValue).doubleValue() );
        } else if (attributeValue instanceof Byte) {
            return ( ((Byte) attributeValue).doubleValue() );
        } else if (attributeValue instanceof Short) { 
            return ( ((Short) attributeValue).doubleValue() );
        } else if (attributeValue instanceof Integer) {
            return ( ((Integer) attributeValue).doubleValue() );
        } else if (attributeValue instanceof Long) {
            return ( ((Long) attributeValue).doubleValue() );
        } else {
            return (Double.parseDouble(attributeValue.toString()));
        }

    }


    /**
     * <p>Return the specified attribute value, converted to a
     * <code>long</code>.</p>
     *
     * @param attributeValue The attribute value to be converted
     *
     * @exception NumberFormatException if conversion is not possible
     */
    protected long longValue(Object attributeValue)
        throws NumberFormatException {

        if (attributeValue instanceof Byte) {
            return ( ((Byte) attributeValue).longValue() );
        } else if (attributeValue instanceof Short) { 
            return ( ((Short) attributeValue).longValue() );
        } else if (attributeValue instanceof Integer) {
            return ( ((Integer) attributeValue).longValue() );
        } else if (attributeValue instanceof Long) {
            return ( ((Long) attributeValue).longValue() );
        } else if (attributeValue instanceof Double) {
            return ( ((Double) attributeValue).longValue() );
        } else if (attributeValue instanceof Float) {
            return ( ((Float) attributeValue).longValue() );
        } else {
            return (Long.parseLong(attributeValue.toString()));
        }

    }


    /**
     * <p>Return the specified attribute value, converted to a
     * <code>String</code>.</p>
     *
     * @param attributeValue The attribute value to be converted
     */
    protected String stringValue(Object attributeValue) {

        if (attributeValue == null) {
            return (null);
        } else if (attributeValue instanceof String) {
            return ((String) attributeValue);
        } else {
            return (attributeValue.toString());
        }

    }


}
