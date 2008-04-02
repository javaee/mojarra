/*
 * $Id: VariableResolver.java,v 1.2 2003/03/13 01:12:15 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


import javax.faces.context.FacesContext;


/**
 * <p><strong>VariableResolver</strong> represents a pluggable mechanism
 * for resolving a top-level variable reference at evaluation time.</p>
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
     * @exception NullPointerException if <code>context</code>
     *  or <code>name</code> is <code>null</code>
     */
    public abstract Object resolveVariable(FacesContext context, String name);


}
