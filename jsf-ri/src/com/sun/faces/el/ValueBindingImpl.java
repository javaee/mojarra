/*
 * $Id: ValueBindingImpl.java,v 1.38 2005/05/05 20:51:24 edburns Exp $
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

import javax.el.ValueExpression;
import javax.el.ELException;

import com.sun.faces.el.impl.Node;

/**
 * @author Jacob Hookom
 */
public class ValueBindingImpl extends ValueBinding implements StateHolder, 
    Serializable {
   
    private ValueExpression valueExpression= null;
    private boolean tranzient;

    public ValueBindingImpl() {} // must have for StateHolder
    
    public ValueBindingImpl(ValueExpression valueExpression) {
        this.valueExpression = valueExpression;    
    }
    
    // PENDING (visvan) we don't need this constrcutor anymore. Remove
    // this once the  ValueBindingFactory is CVS removed
    public ValueBindingImpl(String ref, Node node) {
    }
   
    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#getExpressionString()
     */
    public String getExpressionString() {
        // PENDING (visvan) is there a way to get hold of ExpressionString
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#getType(javax.faces.context.FacesContext)
     */
    public Class getType(FacesContext context) throws EvaluationException,
            PropertyNotFoundException {
        Class result = null;
        try {
            result = valueExpression.getType(context.getELContext());
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#getValue(javax.faces.context.FacesContext)
     */
    public Object getValue(FacesContext context) throws EvaluationException,
            PropertyNotFoundException {
        Object result = null;
        try {
            result = valueExpression.getValue(context.getELContext());
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#isReadOnly(javax.faces.context.FacesContext)
     */
    public boolean isReadOnly(FacesContext context) throws EvaluationException,
            PropertyNotFoundException {
        boolean result = false;
        try {
            result = valueExpression.isReadOnly(context.getELContext());
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }
    
    

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#setValue(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void setValue(FacesContext context, Object value)
            throws EvaluationException, PropertyNotFoundException {
        try {
            valueExpression.setValue(context.getELContext(), value);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
    }
    
    public boolean isTransient() {
        return this.tranzient;
    }
    
    public void setTransient(boolean tranzient) {
        this.tranzient = tranzient;
    }
    
    public void restoreState(FacesContext context, Object obj) {
    }

    public Object saveState(FacesContext context){
        return null;
    }
}
