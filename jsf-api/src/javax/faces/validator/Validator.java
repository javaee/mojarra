/*
 * $Id: Validator.java,v 1.4 2002/07/26 21:53:31 craigmcc Exp $
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


/**
 * <p>A <strong>Validator</strong> is a class that can perform validation
 * (correctness checks) on a {@link UIComponent}.  Zero or more
 * <code>Validator</code>s can be associated with each {@link UIComponent}
 * in the request tree, and are called during the <em>Process
 * Validations Phase</em>.</p>
 *
 * <p>Individual <code>Validator</code>s should examine the component that
 * they are passed, and add {@link Message} instances to the
 * {@link FacesContext} for the current request, documenting
 * any failures to conform to the required rules.  In general, such
 * messages should be associated with the {@link UIComponent} on which
 * the validation failure occurred.</p>
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

public interface Validator {


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
    public AttributeDescriptor getAttributeDescriptor(String name);


    /**
     * <p>Return an <code>Iterator</code> over the names of the supported
     * attributes for this <code>Validator</code>.  If no attributes are
     * supported, an empty <code>Iterator</code> is returned.</p>
     */
    public Iterator getAttributeNames();


    /**
     * <p>Perform the correctness checks implemented by this
     * <code>Validator</code> against the specified {@link UIComponent}.
     * Add {@link Message}s describing any correctness violations to the
     * specified {@link FacesContext}.  If any violations are found:</p>
     * <ul>
     * <li>Add zero or more {@link Message}s to the specified
     *     {@link FacesContext}, specifying this {@link UIComponent} as
     *     associated with the message, describing the nature of the
     *     violation(s) encountered.</li>
     * <li>Call <code>setValid(false)</code> on the {@link UIComponent}.</li>
     * </ul>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     *
     * @exception NullPointerException if any parameter is <code>null</code>
     */
    public void validate(FacesContext context, UIComponent component);


}
