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
