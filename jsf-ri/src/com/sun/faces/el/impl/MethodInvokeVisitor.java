/*
 * $Id: MethodInvokeVisitor.java,v 1.2 2004/11/09 04:23:32 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package com.sun.faces.el.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;

/**
 * @author Jacob Hookom
 */
public class MethodInvokeVisitor extends MethodAbstractVisitor
{
    protected Object[] params;
    
    /**
     * @param context
     * @param paramTypes
     */
    public MethodInvokeVisitor(FacesContext context, Class[] paramTypes, Object[] params)
    {
        super(context, paramTypes);
        this.params = params;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.MethodAbstractVisitor#handleMethod(java.lang.Object, java.lang.reflect.Method)
     */
    public Object handleMethod(Object base, Method method)
            throws EvaluationException, MethodNotFoundException
    {
        try
        {
            return method.invoke(base, this.params);
        }
        catch (IllegalAccessException iae)
        {
            throw new MethodNotFoundException(ELSupport.msg("el.error.method.access"), iae);
        }
        catch (InvocationTargetException ite)
        {
            throw new EvaluationException(ite.getCause());
        }
    }
}
