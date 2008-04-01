/*
 * $Id: ValidatorBase.java,v 1.2 2002/07/23 00:19:14 eburns Exp $
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

abstract class ValidatorBase extends Validator {


    // ------------------------------------------------------- Static Variables


    /**
     * <p>The set of {@link AttributeDescriptor}s for the attributes supported
     * by this {@link Validator}.</p>
     */
    protected static HashMap descriptors = new HashMap();


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
     * specified {@link FacesContext}.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     */
    public abstract void validate(FacesContext context, UIComponent component);


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

    protected int intValue(Object attributeValue) throws NumberFormatException {
        if (attributeValue instanceof Integer) {
            return ( ((Integer) attributeValue).intValue() );
        } else {
            return (Integer.parseInt(attributeValue.toString()));
        }
    }

    protected double doubleValue(Object attributeValue) throws NumberFormatException {
        if (attributeValue instanceof Double) {
            return ( ((Double) attributeValue).intValue() );
        } else {
            return (Double.parseDouble(attributeValue.toString()));
        }
    }

    
 


}
