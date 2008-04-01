/*
 * $Id: ValidatorImpl.java,v 1.1 2002/06/03 22:24:30 craigmcc Exp $
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


/**
 * <p>Internal abstract implementation of {@link Validator} used by classes
 * within this package.  This class is <strong>NOT</strong> part of the public
 * API of JavaServer Faces.</p>
 */

abstract class ValidatorImpl extends Validator {


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
     * {@link MessageList} associated with the specified {@link FacesContext}.
     * </p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     */
    public abstract void validate(FacesContext context, UIComponent component);


}
