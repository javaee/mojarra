/*
 * $Id: MethodBindingImpl.java,v 1.8 2005/05/05 20:51:23 edburns Exp $
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

import javax.el.MethodExpression;
import javax.el.ELException;
import javax.el.MethodInfo;

import com.sun.faces.el.impl.Node;
/**
 * @author Jacob Hookom
 */
public class MethodBindingImpl extends MethodBinding implements StateHolder,
        Serializable {
            
    private MethodExpression methodExpression = null;
    private boolean tranzient = false;

    public MethodBindingImpl() {} // must have for StateHolder
    
    public MethodBindingImpl(MethodExpression methodExpression) {
        this.methodExpression = methodExpression;
    }

    // PENDING (visvan) we don't need this constrcutor anymore. Remove
    // this once the  MethodBindingFactory is CVS removed
    public MethodBindingImpl(String ref, Node node, Class[] paramTypes) {
        
    }
 
    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.MethodBinding#invoke(javax.faces.context.FacesContext,
     *      java.lang.Object[])
     */
    public Object invoke(FacesContext context, Object[] params)
            throws EvaluationException, MethodNotFoundException {
        Object result = null;
        try {
            result = methodExpression.invoke(context.getELContext(), params);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.MethodBinding#getType(javax.faces.context.FacesContext)
     */
    public Class getType(FacesContext context) throws MethodNotFoundException {
        Class result = null;
        try {
            MethodInfo methodInfo 
                = methodExpression.getMethodInfo(context.getELContext());
            result = methodInfo.getReturnType();
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }

    public String getExpressionString() {
        // PENDING (visvan) is there a way to get hold of ExpressionString
	return null;
    }
    
    public boolean isTransient() {
        return this.tranzient;
    }

    public void restoreState(FacesContext context, Object obj) {
    }

    public Object saveState(FacesContext context){
        return null;
    }

    public void setTransient(boolean tranzient) {
        this.tranzient = tranzient;
    }
}
