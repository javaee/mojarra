/*
 * $Id: VariableResolverChainWrapper.java,v 1.8 2006/05/17 17:31:29 rlubke Exp $
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
import javax.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;

import com.sun.faces.util.MessageUtils;

public class VariableResolverChainWrapper extends ELResolver {
    
    @SuppressWarnings("Deprecation")
    private VariableResolver legacyVR = null;

    @SuppressWarnings("Deprecation")
    public VariableResolverChainWrapper(VariableResolver variableResolver) {
        this.legacyVR = variableResolver;
    }

    /**
     * <p>Private request scoped attribute to protect against infinite
     * loops on expressions that touch a custom legacy VariableResolver
     * that delegates to its parent VariableResolver.</p>
     */
    
    private static final String REENTRANT_GUARD = "com.sun.faces.LegacyVariableResolver";

    @Override
    @SuppressWarnings("Deprecation")
    public Object getValue(ELContext context, Object base, Object property)
        throws ELException {

        if (base != null) {
            return null;
        }
        if ( base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        Object result = null;
        
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        try {
	    // If we are already in the midst of an expression evaluation
	    // that touched this resolver...
            if (null != requestMap.get(REENTRANT_GUARD)) {
		// take no action and return.
                context.setPropertyResolved(false);
                return null;
            }
	    // Make sure subsequent calls don't take action.
            requestMap.put(REENTRANT_GUARD, REENTRANT_GUARD);
            
            result = legacyVR.resolveVariable(facesContext,
                                              (String)property);
            
            context.setPropertyResolved(result != null);           
        } catch (EvaluationException ex) {
            context.setPropertyResolved(false);
            throw new ELException(ex);
        }
	finally {
	    // Make sure to remove the guard after the call returns
            requestMap.remove(REENTRANT_GUARD);
	}
        return result;
    }

    @Override
    public Class getType(ELContext context, Object base, Object property)
        throws ELException {

        Object result = null;
        result = getValue(context, base, property);
        context.setPropertyResolved(result != null);
        if (result != null) {
            return result.getClass();
        }
        return null;
    }

    @Override
    public void  setValue(ELContext context, Object base, Object property,
                          Object val) throws ELException {
    if (null == base && null == property) {
        throw new PropertyNotFoundException();
    }
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws ELException {
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
    public Class getCommonPropertyType(ELContext context, Object base) {
        if ( base == null ) {
            return String.class;
        }
        return null;
    }

}
