/*
 * $Id: MethodReturnTypeVisitor.java,v 1.2 2004/11/09 04:23:33 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package com.sun.faces.el.impl;

import java.lang.reflect.Method;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;

/**
 * @author Jacob Hookom
 */
public class MethodReturnTypeVisitor extends MethodAbstractVisitor
{       
    /**
     * @param context
     * @param paramTypes
     */
    public MethodReturnTypeVisitor(FacesContext context, Class[] paramTypes)
    {
        super(context, paramTypes);
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.MethodAbstractVisitor#handleMethod(java.lang.Object, java.lang.reflect.Method)
     */
    public Object handleMethod(Object base, Method method)
            throws EvaluationException, MethodNotFoundException
    {
        Class type = method.getReturnType();
        return (type == Void.TYPE) ? Void.class : type;
    }
}
