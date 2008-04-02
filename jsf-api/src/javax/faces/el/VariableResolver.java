/*
 * $Id: VariableResolver.java,v 1.7 2005/08/22 22:08:04 ofung Exp $
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
 * <p><strong>VariableResolver</strong> represents a pluggable mechanism
 * for resolving a top-level variable reference at evaluation time.</p>
 *
 * @deprecated This has been replaced by {@link javax.el.ELResolver}
 * when operating with a <code>null</code> <code>base</code> argument.
 */

public abstract class VariableResolver {


    /**
     * <p>Resolve the specified variable name, and return the corresponding
     * object, if any; otherwise, return <code>null</code>.</p>
     *
     * @param context {@link FacesContext} against which to resolve
     *  this variable name
     * @param name Name of the variable to be resolved
     *
     * @exception EvaluationException if an exception is thrown while resolving
     *  the variable name (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @exception NullPointerException if <code>context</code>
     *  or <code>name</code> is <code>null</code>
     */
    public abstract Object resolveVariable(FacesContext context, String name)
        throws EvaluationException;


}
