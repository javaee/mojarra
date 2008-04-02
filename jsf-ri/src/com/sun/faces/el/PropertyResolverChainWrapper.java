/*
 * $Id: PropertyResolverChainWrapper.java,v 1.13 2006/09/01 01:22:47 tony_robertson Exp $
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

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyResolver;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.List;

public class PropertyResolverChainWrapper extends ELResolver {

    @SuppressWarnings("deprecation")
    private PropertyResolver legacyPR = null;
    
    @SuppressWarnings("deprecation")
    public PropertyResolverChainWrapper(PropertyResolver propertyResolver) {
        this.legacyPR = propertyResolver;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Object getValue(ELContext context, Object base, Object property) 
        throws ELException {
        if (base == null || property == null) {
            return null;
        }        
        Object result = null;
        context.setPropertyResolved(true);
        FacesContext facesContext = (FacesContext) 
            context.getContext(FacesContext.class);
        if (base instanceof List || base.getClass().isArray()) {
            Object indexObj = facesContext.getApplication().getExpressionFactory().
                    coerceToType(property, Integer.class);
            int index = ((Integer)indexObj).intValue();
            try {
                result = legacyPR.getValue(base, index);                
            } catch (EvaluationException ex) {
                context.setPropertyResolved(false);
                throw new ELException(ex);
            }
        } else {
            try {
                result = legacyPR.getValue(base, property);               
            } catch (EvaluationException ex) {
                context.setPropertyResolved(false);
                throw new ELException(ex);
            }    
        }
        
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Class<?> getType(ELContext context, Object base, Object property) 
        throws ELException {
        if (base == null || property == null) {
            return null;
        }
        
        Class result = null;
        context.setPropertyResolved(true);
        if (base instanceof List || base.getClass().isArray()) {
            FacesContext facesContext = (FacesContext) 
                context.getContext(FacesContext.class);
            Object indexObj = facesContext.getApplication().getExpressionFactory().
                    coerceToType(property, Integer.class);
            int index = ((Integer)indexObj).intValue();
            try {
                result = legacyPR.getType(base, index);               
            } catch (EvaluationException ex) {
                context.setPropertyResolved(false);
                throw new ELException(ex);
            }
        } else {
            try {
                result = legacyPR.getType(base, property);                
            } catch (EvaluationException ex) {
                context.setPropertyResolved(false);
                throw new ELException(ex);
            }    
        }
        
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void  setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
        if (base == null || property == null) {
            return;
        }       

        context.setPropertyResolved(true);

        if (base instanceof List || base.getClass().isArray()) {
            FacesContext facesContext = (FacesContext) 
            context.getContext(FacesContext.class);
            Object indexObj = facesContext.getApplication().getExpressionFactory().
                    coerceToType(property, Integer.class);
            int index = ((Integer)indexObj).intValue();            
            try {
                legacyPR.setValue(base, index, val);
            } catch (EvaluationException ex) {
                context.setPropertyResolved(false);
                throw new ELException(ex);
            }
        } else {
            try {
                legacyPR.setValue(base, property, val);
            } catch (EvaluationException ex) {
                context.setPropertyResolved(false);
                throw new ELException(ex);
            }    
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException {
        if (base == null || property == null) {
            return false;
        }      
        boolean result = false;
        context.setPropertyResolved(true);
        if (base instanceof List || base.getClass().isArray()) {
            FacesContext facesContext = (FacesContext) 
            context.getContext(FacesContext.class);
            Object indexObj = facesContext.getApplication().getExpressionFactory().
                    coerceToType(property, Integer.class);
            int index = ((Integer)indexObj).intValue();
            try {
                result = legacyPR.isReadOnly(base, index);                
            } catch (EvaluationException ex) {
                context.setPropertyResolved(false);
                throw new ELException(ex);
            }
        } else {
            try {
                result = legacyPR.isReadOnly(base, property);                
            } catch (EvaluationException ex) {
                context.setPropertyResolved(false);
                throw new ELException(ex);
            }    
        }
        
        return result;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, 
                                                             Object base) {
        return null;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if ( base == null ) {
            return Object.class;
        }
        return null;
    }

}
