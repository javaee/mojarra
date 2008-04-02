/*
 * $Id: ValueBindingImpl.java,v 1.37 2004/11/09 04:23:11 jhook Exp $
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

import com.sun.faces.el.impl.JsfParser;
import com.sun.faces.el.impl.ELSupport;
import com.sun.faces.el.impl.Node;
import com.sun.faces.el.impl.ParseException;
import com.sun.faces.el.impl.ValueGetVisitor;
import com.sun.faces.el.impl.ValueReadOnlyVisitor;
import com.sun.faces.el.impl.ValueSetVisitor;
import com.sun.faces.el.impl.ValueTypeVisitor;


/**
 * @author Jacob Hookom
 */
public class ValueBindingImpl extends ValueBinding implements StateHolder, Serializable
{
    protected String ref;
    protected transient Node node;
    protected boolean tranzient;
    
    
    public ValueBindingImpl()
    {
        
    }
    
    public ValueBindingImpl(String ref)
    {
        this.ref = ref;
    }
    
    /**
     * 
     */
    public ValueBindingImpl(String ref, Node node)
    {
        this.ref = ref;
        this.node = node;
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
                throw new ReferenceSyntaxException(ELSupport.msg("el.error.factory.value",ref,pe.getMessage()));
            }
        }
        return this.node;
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#getExpressionString()
     */
    public String getExpressionString()
    {
        return this.ref;
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#getType(javax.faces.context.FacesContext)
     */
    public Class getType(FacesContext context) throws EvaluationException,
            PropertyNotFoundException
    {
        ValueTypeVisitor visitor = new ValueTypeVisitor(context);
        try
        {
            return (Class) this.getNode().jjtAccept(visitor, null);
        }
        catch (PropertyNotFoundException pe)
        {
            rethrow("el.error.value.type",pe);
        }
        catch (ReferenceSyntaxException rse)
        {
            rethrow("el.error.value.type",rse);
        }
        catch (EvaluationException ee)
        {
            rethrow("el.error.value.type",ee);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#getValue(javax.faces.context.FacesContext)
     */
    public Object getValue(FacesContext context) throws EvaluationException,
            PropertyNotFoundException
    {
        ValueGetVisitor visitor = new ValueGetVisitor(context);
        try
        {
            return this.getNode().jjtAccept(visitor, null);
        }
        catch (PropertyNotFoundException pe)
        {
            rethrow("el.error.value.get",pe);
        }
        catch (ReferenceSyntaxException rse)
        {
            rethrow("el.error.value.get",rse);
        }
        catch (EvaluationException ee)
        {
            rethrow("el.error.value.get",ee);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#isReadOnly(javax.faces.context.FacesContext)
     */
    public boolean isReadOnly(FacesContext context) throws EvaluationException,
            PropertyNotFoundException
    {
        ValueReadOnlyVisitor visitor = new ValueReadOnlyVisitor(context);
        try
        {
            return ((Boolean) this.getNode().jjtAccept(visitor, null)).booleanValue();
        }
        catch (PropertyNotFoundException pe)
        {
            rethrow("el.error.value.readOnly",pe);
        }
        catch (ReferenceSyntaxException rse)
        {
            rethrow("el.error.value.readOnly",rse);
        }
        catch (EvaluationException ee)
        {
            rethrow("el.error.value.readOnly",ee);
        }
        return true;
    }
    
    protected void rethrow(String key, ReferenceSyntaxException rse) throws ReferenceSyntaxException
    {
        throw new ReferenceSyntaxException(ELSupport.msg(key,this.ref,rse.getMessage()), (rse.getCause() != null) ? rse.getCause() : rse);
    }
    
    protected void rethrow(String key, EvaluationException ee) throws EvaluationException
    {
        throw new EvaluationException(ELSupport.msg(key,this.ref,ee.getMessage()), (ee.getCause() != null) ? ee.getCause() : ee);
    }
    
    protected void rethrow(String key, PropertyNotFoundException pe) throws PropertyNotFoundException
    {
        throw new PropertyNotFoundException(ELSupport.msg(key,this.ref,pe.getMessage()), (pe.getCause() != null) ? pe.getCause() : pe);
    }

    /* (non-Javadoc)
     * @see javax.faces.el.ValueBinding#setValue(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void setValue(FacesContext context, Object value)
            throws EvaluationException, PropertyNotFoundException
    {
        ValueSetVisitor visitor = new ValueSetVisitor(context, value);
        try
        {
            this.getNode().jjtAccept(visitor, null);
        }
        catch (PropertyNotFoundException pe)
        {
            rethrow("el.error.value.set",pe);
        }
        catch (ReferenceSyntaxException rse)
        {
            rethrow("el.error.value.set",rse);
        }
        catch (EvaluationException ee)
        {
            rethrow("el.error.value.set",ee);
        }
    }
    
    public boolean isTransient()
    {
        return this.tranzient;
    }
    public void restoreState(FacesContext context, Object obj)
    {
        this.ref = (String) obj;
    }
    public Object saveState(FacesContext context)
    {
        return this.ref;
    }
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
