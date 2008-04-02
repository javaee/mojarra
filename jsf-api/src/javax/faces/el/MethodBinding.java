/*
 * $Id: MethodBinding.java,v 1.8 2004/02/26 20:30:55 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


import javax.faces.context.FacesContext;


/**
 * <p><strong>MethodBinding</strong> is an object that can be used
 * to call an arbitrary public method, on an instance that is acquired by
 * evaluatng the leading portion of a method binding expression via a
 * {@link ValueBinding}.  An immutable {@link MethodBinding} for a particular
 * method binding expression can be acquired by calling the
 * <code>createMethodBinding()</code> method of the
 * {@link javax.faces.application.Application} instance for this web
 * application.</p>
 */

public abstract class MethodBinding {


    /**
     * <p>Return the return value (if any) resulting from a call to the
     * method identified by this method binding expression, passing it
     * the specified parameters, relative to the specified {@link FacesContext}.
     * </p>
     *
     * @param context {@link FacesContext} for the current request
     * @param params Array of parameters to be passed to the called method,
     *  or <code>null</code> for no parameters
     *
     * @exception EvaluationException if an exception is thrown
     *  by the called method (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @exception MethodNotFoundException if no suitable method can be found
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract Object invoke(FacesContext context, Object params[])
        throws EvaluationException, MethodNotFoundException;


    /**
     * <p>Return the Java class representing the return type from the
     * method identified by this method binding expression.  If the
     * identified method has a return type of <code>void</code>, return
     * <code>null</code> instead.</p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception MethodNotFoundException if no suitable method can be found
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract Class getType(FacesContext context)
        throws MethodNotFoundException;

    /**
     * <p>Return the (possibly <code>null</code>) expression String,
     * with leading and trailing delimiters, from which this
     * <code>MethodBinding</code> was built.  The default implementation
     * returns <code>null</code>.</p>
     *
     */
    public String getExpressionString() {
	return null;
    }



}
