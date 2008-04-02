/*
 * $Id: DummyPropertyResolverImpl.java,v 1.2 2005/05/18 17:33:43 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

import javax.faces.context.FacesContext;

/**
 * Default propertyResolver implementation that gets the ELContext from the 
 * argument FacesContext and calls setPropertyResolved(false) on it. This is
 * provided to ensure that the legacy property resolvers continue to work with
 * unfied EL API
 */

public class DummyPropertyResolverImpl extends PropertyResolver {
    
    public Object getValue(Object base, Object property)
        throws EvaluationException, PropertyNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getELContext().setPropertyResolved(false);
        return null;
    }


    public Object getValue(Object base, int index)
        throws EvaluationException, PropertyNotFoundException {
       FacesContext context = FacesContext.getCurrentInstance();
       context.getELContext().setPropertyResolved(false);
       return null;         
    }


    public void setValue(Object base, Object property, Object value)
        throws EvaluationException, PropertyNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getELContext().setPropertyResolved(false);
    }

    public void setValue(Object base, int index, Object value)
        throws EvaluationException, PropertyNotFoundException {
       FacesContext context = FacesContext.getCurrentInstance();
       context.getELContext().setPropertyResolved(false);
    }

    public boolean isReadOnly(Object base, Object property)
        throws EvaluationException, PropertyNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getELContext().setPropertyResolved(false);        
        return false;
    }

    public boolean isReadOnly(Object base, int index)
        throws EvaluationException, PropertyNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getELContext().setPropertyResolved(false);        
        return false;        
    }


    public Class getType(Object base, Object property)
        throws EvaluationException, PropertyNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getELContext().setPropertyResolved(false);        
        return null;         
    }

    public Class getType(Object base, int index)
        throws EvaluationException, PropertyNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getELContext().setPropertyResolved(false);        
        return null;
    }
    
}