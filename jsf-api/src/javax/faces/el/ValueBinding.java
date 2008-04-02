/*
 * $Id: ValueBinding.java,v 1.5 2004/01/26 20:48:58 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


import javax.faces.context.FacesContext;


/**
 * <p><strong>ValueBinding</strong> is an object that can be used
 * to access the property represented by an action or value binding
 * expression.  An immutable {@link ValueBinding} for a particular value binding
 * can be acquired by calling the <code>createValueBinding()</code> method of
 * the {@link javax.faces.application.Application} instance for this web
 * application.  Implementations of {@link ValueBinding} are suitable for
 * caching the bindings for frequently accessed expressions, without requiring
 * per-request reparsing.</p>
 */

public abstract class ValueBinding {


    /**
     * <p>Return the value of the property represented by this
     * {@link ValueBinding}, relative to the specified {@link FacesContext}.
     * </p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception EvaluationException if an exception is thrown while getting
     *  the value (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @exception PropertyNotFoundException if a specified property name
     *  does not exist, or is not readable
     */
    public abstract Object getValue(FacesContext context)
        throws EvaluationException, PropertyNotFoundException;


    /**
     * <p>Set the value of the property represented by this
     * {@link ValueBinding}, relative to the specified {@link FacesContext}.
     * </p>
     *
     * @param context {@link FacesContext} for the current request
     * @param value The new value to be set
     *
     * @exception EvaluationException if an exception is thrown while setting
     *  the value (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @exception PropertyNotFoundException if a specified property name
     *  does not exist, or is not writeable
     */
    public abstract void setValue(FacesContext context, Object value)
        throws EvaluationException, PropertyNotFoundException;


    /**
     * <p>Return <code>true</code> if the specified property of the specified
     * property is known to be immutable; otherwise, return
     * <code>false</code>.</p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception EvaluationException if an exception is thrown while getting
     *  the description of the property (the thrown exception must be
     *  included as the <code>cause</code> property of this exception)
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @exception PropertyNotFoundException if a specified property name
     *  does not exist
     */
    public abstract boolean isReadOnly(FacesContext context)
        throws EvaluationException, PropertyNotFoundException;


    /**
     * <p>Return the type of the property represented by this
     * {@link ValueBinding}, relative to the specified {@link FacesContext}.
     * </p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception EvaluationException if an exception is thrown while getting
     *  the description of the property (the thrown exception must be
     *  included as the <code>cause</code> property of this exception)
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @exception PropertyNotFoundException if a specified property name
     *  does not exist
     */
    public abstract Class getType(FacesContext context)
        throws EvaluationException, PropertyNotFoundException;


    /**
     * <p>Return the (possibly <code>null</code>) expression String,
     * with leading and trailing delimiters, from which this
     * <code>ValueBinding</code> was built.  The default implementation
     * returns <code>null</code>.</p>
     *
     */
    public String getExpressionString() {
	return null;
    }




}
