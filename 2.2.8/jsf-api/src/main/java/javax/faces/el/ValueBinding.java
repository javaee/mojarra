/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
     * @throws EvaluationException if an exception is thrown while getting
     *  the value (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @throws PropertyNotFoundException if a specified property name
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
     * @throws EvaluationException if an exception is thrown while setting
     *  the value (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @throws PropertyNotFoundException if a specified property name
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
     * @throws EvaluationException if an exception is thrown while getting
     *  the description of the property (the thrown exception must be
     *  included as the <code>cause</code> property of this exception)
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @throws PropertyNotFoundException if a specified property name
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
     * @throws EvaluationException if an exception is thrown while getting
     *  the description of the property (the thrown exception must be
     *  included as the <code>cause</code> property of this exception)
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @throws PropertyNotFoundException if a specified property name
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
