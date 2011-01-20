/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
        Object result;
        context.setPropertyResolved(true);
        FacesContext facesContext = (FacesContext) 
            context.getContext(FacesContext.class);
        ELContext jsfEL = facesContext.getELContext();
        jsfEL.setPropertyResolved(true);
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
        context.setPropertyResolved(jsfEL.isPropertyResolved());
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Class<?> getType(ELContext context, Object base, Object property) 
        throws ELException {
        if (base == null || property == null) {
            return null;
        }
        
        Class result;
        context.setPropertyResolved(true);
        FacesContext facesContext = (FacesContext)
                context.getContext(FacesContext.class);
        ELContext jsfEL = facesContext.getELContext();
        jsfEL.setPropertyResolved(true);
        if (base instanceof List || base.getClass().isArray()) {
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
        context.setPropertyResolved(jsfEL.isPropertyResolved());
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
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
        ELContext jsfEL = facesContext.getELContext();
        jsfEL.setPropertyResolved(true);
        if (base instanceof List || base.getClass().isArray()) {
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
        context.setPropertyResolved(jsfEL.isPropertyResolved());
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException {
        if (base == null || property == null) {
            return false;
        }      
        boolean result;
        context.setPropertyResolved(true);
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
        ELContext jsfEL = facesContext.getELContext();
        jsfEL.setPropertyResolved(true);
        if (base instanceof List || base.getClass().isArray()) {
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
        context.setPropertyResolved(jsfEL.isPropertyResolved());
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
