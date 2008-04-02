/*
 * $Id: MethodBindingConstant.java,v 1.2 2004/11/09 04:23:09 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.faces.el;

import java.io.Serializable;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ValueBinding;

/**
 * @author jhook
 */
public class MethodBindingConstant extends MethodBinding implements
        StateHolder, Serializable
{
    protected String ref;
    protected String value;
    protected boolean tranzient;
    
    /**
     *  
     */
    public MethodBindingConstant()
    {
        super();
    }
    
    public MethodBindingConstant(String ref, String value)
    {
        this.ref = ref;
        this.value = value;
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
        return this.value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.MethodBinding#getType(javax.faces.context.FacesContext)
     */
    public Class getType(FacesContext context) throws MethodNotFoundException
    {
        return String.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context)
    {
        String[] state = new String[2];
        state[0] = this.ref;
        state[1] = this.value;
        return state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state)
    {
        this.ref = ((String[]) state)[0];
        this.value = ((String[]) state)[1];
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#isTransient()
     */
    public boolean isTransient()
    {
        return this.tranzient;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#setTransient(boolean)
     */
    public void setTransient(boolean newTransientValue)
    {
        this.tranzient = newTransientValue;
    }
    
    /* (non-Javadoc)
     * @see javax.faces.el.MethodBinding#getExpressionString()
     */
    public String getExpressionString()
    {
        return this.ref;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof MethodBinding) {
            return this.getExpressionString().equals(((MethodBinding) obj).getExpressionString());
        }
        return false;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return this.getExpressionString().hashCode();
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "MethodBinding["+this.getExpressionString()+"]";
    }
}