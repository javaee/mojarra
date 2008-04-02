/*
 * $Id: MethodBinding.java,v 1.2 2003/10/25 22:08:49 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


import java.lang.reflect.InvocationTargetException;
import javax.faces.context.FacesContext;


/**
 * <p><strong>MethodBinding</strong> represents an object that can be used
 * to call an arbitrary public method, on an instance that is acquired by
 * evaluatng the leading portion of a method reference expression via a
 * {@link ValueBinding}.  An immutable {@link MethodBinding} for a particular
 * reference expression can be acquired by calling the
 * <code>getMethodBinding()</code> method of the
 * {@link javax.faces.application.Application} instance for this web
 * application.  Implementations of {@link MethodBinding} are suitable for
 * caching the bindings for frequently accessed expressions, without requiring
 * per-request reparsing.</p>
 */

public abstract class MethodBinding {


    /**
     * <p>Return the return value (if any) resulting from a call to the
     * method identified by this method reference expression, passing it
     * the specified parameters, relative to the specified {@link FacesContext}.
     * </p>
     *
     * @param context {@link FacesContext} for the current request
     * @param params Array of parameters to be passed to the called method,
     *  or <code>null</code> for no parameters
     *
     * @exception InvocationTargetException if an exception is thrown
     *  by the called method
     * @exception MethodNotFoundException if no suitable method can be found
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract Object invoke(FacesContext context, Object params[])
        throws InvocationTargetException;


    /**
     * <p>Return the Java class representing the return type from the
     * method identified by this method reference expression.  If the
     * identified method has a return type of <code>void</code>, return
     * <code>null</code> instead.</p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception MethodNotFoundException if no suitable method can be found
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract Class getType(FacesContext context);


}
