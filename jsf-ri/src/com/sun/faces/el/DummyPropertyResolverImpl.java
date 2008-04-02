/*
 * $Id: DummyPropertyResolverImpl.java,v 1.7 2006/08/29 06:12:59 tony_robertson Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
@SuppressWarnings("deprecation")
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