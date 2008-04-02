/*
 * $Id: MethodBindingImpl.java,v 1.7 2004/11/09 04:23:09 jhook Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.io.Serializable;
import java.lang.reflect.Array;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

import com.sun.faces.el.impl.JsfParser;
import com.sun.faces.el.impl.ELSupport;
import com.sun.faces.el.impl.MethodInvokeVisitor;
import com.sun.faces.el.impl.MethodReturnTypeVisitor;
import com.sun.faces.el.impl.Node;
import com.sun.faces.el.impl.ParseException;

/**
 * @author Jacob Hookom
 */
public class MethodBindingImpl extends MethodBinding implements StateHolder,
        Serializable
{
    protected String ref;

    protected transient Node node;

    protected boolean tranzient;

    protected Class[] paramTypes;

    public MethodBindingImpl()
    {
    }

    public MethodBindingImpl(String ref, Class[] paramTypes)
    {
        this.ref = ref;
        this.paramTypes = paramTypes;
    }

    /**
     *  
     */
    public MethodBindingImpl(String ref, Node node, Class[] paramTypes)
    {
        this.ref = ref;
        this.node = node;
        this.paramTypes = paramTypes;
    }

    protected Node getNode() throws ReferenceSyntaxException
    {
        if (this.node == null)
        {
            try
            {
                this.node = JsfParser.parse(this.ref);
            }
            catch (ParseException pe)
            {
                throw new ReferenceSyntaxException(ELSupport.msg(
                        "el.error.factory.method", ref, pe.getMessage()));
            }
        }
        return this.node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.MethodBinding#invoke(javax.faces.context.FacesContext,
     *      java.lang.Object[])
     */
    public Object invoke(FacesContext context, Object[] params)
            throws EvaluationException, MethodNotFoundException
    {
        MethodInvokeVisitor visitor = new MethodInvokeVisitor(context,
                this.paramTypes, params);
        try
        {
            return this.getNode().jjtAccept(visitor, null);
        }
        catch (MethodNotFoundException mnfe)
        {
            rethrow("el.error.method.invoke", mnfe);
        }
        catch (EvaluationException ee)
        {
            rethrow("el.error.method.invoke", ee);
        }
        return null;
    }

    protected void rethrow(String key, EvaluationException ee)
            throws EvaluationException
    {
        throw new EvaluationException(ELSupport.msg(key, this.ref, ee
                .getMessage()), (ee.getCause() != null) ? ee.getCause() : ee);
    }

    protected void rethrow(String key, MethodNotFoundException mnfe)
            throws MethodNotFoundException
    {
        throw new MethodNotFoundException(ELSupport.msg(key, this.ref, mnfe
                .getMessage()), (mnfe.getCause() != null) ? mnfe.getCause()
                : mnfe);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.MethodBinding#getType(javax.faces.context.FacesContext)
     */
    public Class getType(FacesContext context) throws MethodNotFoundException
    {
        MethodReturnTypeVisitor visitor = new MethodReturnTypeVisitor(context,
                this.paramTypes);
        try
        {
            return (Class) this.getNode().jjtAccept(visitor, null);
        }
        catch (MethodNotFoundException mnfe)
        {
            rethrow("el.error.method.type", mnfe);
        }
        catch (EvaluationException ee)
        {
            rethrow("el.error.method.type", ee);
        }
        return null;
    }

    public boolean isTransient()
    {
        return this.tranzient;
    }

    public void restoreState(FacesContext context, Object obj)
    {
        Object[] values = (Object[]) obj;
        this.ref = (String) values[0];
        this.paramTypes = (Class[]) values[1];
    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[]
        {
                this.ref, this.paramTypes
        };
        return values;
    }

    public void setTransient(boolean tranzient)
    {
        this.tranzient = tranzient;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.MethodBinding#getExpressionString()
     */
    public String getExpressionString()
    {
        return this.ref;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof MethodBinding)
        {
            MethodBinding omb = (MethodBinding) obj;
            return this.getExpressionString().equals(omb.getExpressionString());
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return this.getExpressionString().hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "MethodBinding[" + this.getExpressionString() + "]";
    }
}
