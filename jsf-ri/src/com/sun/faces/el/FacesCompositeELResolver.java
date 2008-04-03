/*
 * $Id: FacesCompositeELResolver.java,v 1.8 2007/07/17 23:14:01 rlubke Exp $
 */
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
        
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return null;
        }                    
        setChainType();
        Object result = super.getValue(context, base, property);
        clearChainType();
        
        return result;
    }

    public Class<?> getType(ELContext context, Object base, Object property) 
        throws ELException {

        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return null;
        }
        setChainType();
        Class<?> result = super.getType(context, base, property);
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
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return false;
        }
        setChainType();
        boolean result = super.isReadOnly(context, base, property);
        clearChainType();
        return result;
    }

    
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        setChainType();
        Iterator<FeatureDescriptor> result = super.getFeatureDescriptors(context, base);
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

    public enum ELResolverChainType {
        JSP,
        Faces
    }

    private ELResolverChainType chainType;

    /**
     * <p>Guarantee that this instance knows of what chain it is a
     * member.</p>
     * @param chainType the {@link ELResolverChainType} 
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

