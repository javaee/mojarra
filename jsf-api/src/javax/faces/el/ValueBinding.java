/*
 * $Id: ValueBinding.java,v 1.13 2005/08/22 22:08:04 ofung Exp $
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
 * <p><strong>ValueBinding</strong> is an object that can be used
 * to access the property represented by an action or value binding
 * expression.  An immutable {@link ValueBinding} for a particular value binding
 * can be acquired by calling the <code>createValueBinding()</code> method of
 * the {@link javax.faces.application.Application} instance for this web
 * application.</p>
 *
 * @deprecated This has been replaced by {@link javax.el.ValueExpression}.
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
     * including the delimiters, from which this
     * <code>ValueBinding</code> was built.</p>
     *
     */
    public String getExpressionString() {
	return null;
    }




}
