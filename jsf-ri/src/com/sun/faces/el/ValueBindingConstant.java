/*
 * $Id: ValueBindingConstant.java,v 1.2 2004/11/09 04:23:10 jhook Exp $
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
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

import com.sun.faces.el.impl.ELSupport;

/**
 * @author Jacob Hookom
 */
public class ValueBindingConstant extends ValueBinding implements StateHolder, Serializable
{
    protected String ref;

    protected Object constant;

    protected boolean tranzient;
    
    public ValueBindingConstant()
    {
        
    }

    /**
     *  
     */
    public ValueBindingConstant(String ref, Object constant)
    {
        this.ref = ref;
        this.constant = constant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.ValueBinding#getValue(javax.faces.context.FacesContext)
     */
    public Object getValue(FacesContext context) throws EvaluationException, PropertyNotFoundException
    {
        return this.constant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.ValueBinding#setValue(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public void setValue(FacesContext context, Object value) throws EvaluationException, PropertyNotFoundException
    {
        throw new ReferenceSyntaxException(ELSupport.msg("el.error.value.set", this.ref, ELSupport
                .msg("el.error.value.set.invalid")));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.ValueBinding#isReadOnly(javax.faces.context.FacesContext)
     */
    public boolean isReadOnly(FacesContext context) throws EvaluationException, PropertyNotFoundException
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.ValueBinding#getType(javax.faces.context.FacesContext)
     */
    public Class getType(FacesContext context) throws EvaluationException, PropertyNotFoundException
    {
        if (this.constant != null)
        {
            return this.constant.getClass();
        }
        return null;
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
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state)
    {
        this.ref = (String) ((Object[]) state)[0];
        this.constant = ((Object[]) state)[1];
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context)
    {
        Object[] state = new Object[2];
        state[0] = this.ref;
        state[1] = this.constant;
        return state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#setTransient(boolean)
     */
    public void setTransient(boolean tranzient)
    {
        this.tranzient = tranzient;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof ValueBinding) {
            return this.getExpressionString().equals(((ValueBinding) obj).getExpressionString());
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
        return "ValueBinding["+this.getExpressionString()+"]";
    }
}