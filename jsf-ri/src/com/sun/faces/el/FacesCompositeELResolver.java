/*
 * $Id: FacesCompositeELResolver.java,v 1.1 2005/05/05 20:51:22 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.util.ArrayList;
import java.util.Iterator;

import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.CompositeELResolver;
import javax.el.ELException;

import javax.faces.context.FacesContext;

/**
 * Maintains an ordered composite list of child <code>ELResolver for JSF</code>.
 *
 */
public class FacesCompositeELResolver extends CompositeELResolver {
    
    public void add(ELResolver elResolver) {                                                                             
        super.add(elResolver);
    }

    public Object getValue(ELContext context, Object base, Object property) 
        throws ELException {
                               
        context.setPropertyResolved(false);                      
        if (FacesContext.getCurrentInstance() == null) {
            return null;
        }                       
        return super.getValue(context, base, property);
    }

    public Class getType(ELContext context, Object base, Object property) 
        throws ELException {
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return null;
        }
        return super.getType(context, base, property);
    }

    
    public void setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return;
        }
        super.setValue(context, base, property, val);
    }

    
    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException {
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return false;
        }
        return super.isReadOnly(context, base, property);
    }

    
    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        return super.getFeatureDescriptors(context, base);
    }
    
    public Class getCommonPropertyType(ELContext context, Object base) {
        return null;
    }

}

