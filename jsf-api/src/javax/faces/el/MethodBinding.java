/*
 * $Id: MethodBinding.java,v 1.12 2005/12/05 16:42:53 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
 *
 *
 * @deprecated This has been replaced by {@link javax.el.MethodExpression}.
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
     * @throws EvaluationException if an exception is thrown
     *  by the called method (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @throws MethodNotFoundException if no suitable method can be found
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract Object invoke(FacesContext context, Object params[])
        throws EvaluationException, MethodNotFoundException;


    /**
     * <p>Return the Java class representing the return type from the
     * method identified by this method binding expression.  </p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @throws MethodNotFoundException if no suitable method can be found
     * @throws NullPointerException if <code>context</code>
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
