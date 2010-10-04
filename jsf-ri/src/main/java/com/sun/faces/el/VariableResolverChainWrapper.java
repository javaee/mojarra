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
import javax.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import java.beans.FeatureDescriptor;
import java.util.*;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.RequestStateManager;

public class VariableResolverChainWrapper extends ELResolver {
    
    @SuppressWarnings("deprecation")
    private VariableResolver legacyVR = null;

    @SuppressWarnings("deprecation")
    public VariableResolverChainWrapper(VariableResolver variableResolver) {
        this.legacyVR = variableResolver;
    }

    public void setWrapped(VariableResolver newVR) {
        this.legacyVR = newVR;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Object getValue(ELContext context, Object base, Object property)
        throws ELException {

        // Don't call into the chain unless it's been decorated.
        if (legacyVR instanceof ChainAwareVariableResolver) {
            return null;
        }

        if (base != null) {
            return null;
        }
        if ( base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }
        context.setPropertyResolved(true);
        Object result = null;
        
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
        String propString = property.toString();
        Map<String,Object> stateMap = RequestStateManager.getStateMap(facesContext);
        try {
        // If we are already in the midst of an expression evaluation
        // that touched this resolver...
            //noinspection unchecked
            List<String> varNames = (List<String>) stateMap.get(RequestStateManager.REENTRANT_GUARD);
            if (varNames != null
                 && !varNames.isEmpty()
                 && varNames.contains(propString)) {
                // take no action and return.
                context.setPropertyResolved(false);
                return null;
            }
            // Make sure subsequent calls don't take action.
            if (varNames == null) {
                varNames = new ArrayList<String>();
                stateMap.put(RequestStateManager.REENTRANT_GUARD, varNames);
            }
            varNames.add(propString);
            
            result = legacyVR.resolveVariable(facesContext,
                                              propString);
        } catch (EvaluationException ex) {
            context.setPropertyResolved(false);
            throw new ELException(ex);
        } finally {
            // Make sure to remove the guard after the call returns
            //noinspection unchecked
            List<String> varNames = (List<String>) stateMap.get(RequestStateManager.REENTRANT_GUARD);
            if (varNames != null && !varNames.isEmpty()) {
                varNames.remove(propString);
            }
            // Make sure that the ELContext "resolved" indicator is set 
            // in accordance wth the result of the resolution.
            context.setPropertyResolved(result != null);
        }
        return result;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property)
        throws ELException {

        // Don't call into the chain unless it's been decorated.
        if (legacyVR instanceof ChainAwareVariableResolver) {
            return null;
        }

        Object result = getValue(context, base, property);
        context.setPropertyResolved(result != null);
        if (result != null) {
            return result.getClass();
        }
        return null;
    }

    @Override
    public void  setValue(ELContext context, Object base, Object property,
                          Object val) throws ELException {
        // Don't call into the chain unless it's been decorated.
        if (legacyVR instanceof ChainAwareVariableResolver) {
            return;
        }

        if (null == base && null == property) {
            throw new PropertyNotFoundException();
        }
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws ELException {

        // Don't call into the chain unless it's been decorated.
        if (legacyVR instanceof ChainAwareVariableResolver) {
            return false;
        }

        if (null == base && null == property) {
        throw new PropertyNotFoundException();
    }
        return false;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, 
                                                             Object base) {
        return null;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {

        // Don't call into the chain unless it's been decorated.
        if (legacyVR instanceof ChainAwareVariableResolver) {
            return null;
        }
        
        if ( base == null ) {
            return String.class;
        }
        return null;
    }

}
