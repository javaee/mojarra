/*
 * $Id: Validator.java,v 1.1 2002/06/03 19:27:27 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.util.Iterator;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageList;


/**
 * <p>A <strong>Validator</strong> is a class that can perform validation
 * (correctness checks) on a {@link UIComponent}.  Zero or more
 * <code>Validator</code>s can be associated with each {@link UIComponent}
 * in the request tree, and are called during the <em>Process
 * Validations Phase</em>.</p>
 *
 * <p>Individual <code>Validator</code>s should examine the component that
 * they are passed, and add {@link Message} instances to the
 * {@link MessageList} associated with the specified {@link FacesContext}
 * for any failures to conform to the required rules.  In general, such
 * messages should be configured with the <code>compoundId</code> of the
 * specified component as the <code>reference</code> property of the message.
 * </p>
 *
 * <p>For maximum generality, <code>Validator</code> instances should be
 * configurable based on attribute values associated with the
 * {@link UIComponent} being validated.  For example, a range check
 * validator might support configuration of the minimum and maximum values
 * to be used.  Each <code>Validator</code> should document the attributes
 * it cares about via the <code>getAttributeNames()</code> and
 * <code>getAttributeDescriptor()</code> methods, so that tools can construct
 * robust user interfaces for configuring them.</p>
 */

public abstract class Validator {


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
    public abstract AttributeDescriptor getAttributeDescriptor(String name);


    /**
     * <p>Return an <code>Iterator</code> over the names of the supported
     * attributes for this <code>Validator</code>.  If no attributes are
     * supported, an empty <code>Iterator</code> is returned.</p>
     */
    public abstract Iterator getAttributeNames();


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
