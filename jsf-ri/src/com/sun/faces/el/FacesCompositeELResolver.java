/*
 * $Id: FacesCompositeELResolver.java,v 1.6 2006/09/01 01:22:40 tony_robertson Exp $
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

import com.sun.faces.RIConstants;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;

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
        
        Object result = null;
                               
        context.setPropertyResolved(false);                      
        if (FacesContext.getCurrentInstance() == null) {
            return null;
        }                    
        setChainType();
        result = super.getValue(context, base, property);
        clearChainType();
        
        return result;
    }

    public Class<?> getType(ELContext context, Object base, Object property) 
        throws ELException {
        Class result = null;
        
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return null;
        }
        setChainType();
        result = super.getType(context, base, property);
        clearChainType();

        return result;
    }

    
    public void setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return;
        }
        setChainType();
        super.setValue(context, base, property, val);
        clearChainType();
    }

    
    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException {
        boolean result = false;
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return false;
        }
        setChainType();
        result = super.isReadOnly(context, base, property);
        clearChainType();
        return result;
    }

    
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        Iterator<FeatureDescriptor> result = null;
        setChainType();
        result = super.getFeatureDescriptors(context, base);
        clearChainType();
        return result;
    }
    
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return null;
    }

    /**
     * <p><b>JSP</b> indicates this CompositeELResolver instance is the
     * JSP chain, specified in section 5.6.1 of the spec.</p>
     *
     * <p><b>Faces</b> indicates this CompositeELResolver instance is the
     * JSF chain, specified in section 5.6.2 of the spec.</p>
     */

    public enum ELResolverChainType { JSP, Faces
    }

    private ELResolverChainType chainType;

    /**
     * <p>Guarantee that this instance knows of what chain it is a
     * member.</p>
     */

    public FacesCompositeELResolver(ELResolverChainType chainType) {
        this.chainType = chainType;
    }

    /**
     * <p>Set a request scoped attribute indicating what kind of chain
     * the current expression is.</p>
     */

    private void setChainType() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        requestMap.put(RIConstants.EL_RESOLVER_CHAIN_TYPE_NAME, chainType);
    }

    /**
     * <p>Clear the request scoped attribute indicating what kind of
     * chain the current expression is.</p>
     */
    
    private void clearChainType() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        requestMap.remove(RIConstants.EL_RESOLVER_CHAIN_TYPE_NAME);
    }

}

