/*
 * $Id: VariableResolverChainWrapper.java,v 1.2 2005/08/22 22:10:13 ofung Exp $
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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.beans.FeatureDescriptor;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import javax.faces.el.EvaluationException;

import javax.el.ELException;
import javax.el.PropertyNotWritableException;
import javax.el.PropertyNotFoundException;
import javax.el.ELContext;
import javax.el.ELResolver;

import com.sun.faces.el.ELConstants;
import com.sun.faces.util.Util;

public class VariableResolverChainWrapper extends ELResolver {

    private VariableResolver legacyVR = null;
    
    public VariableResolverChainWrapper( VariableResolver variableResolver) {
        this.legacyVR = variableResolver;
    }

    public Object getValue(ELContext context, Object base, Object property) 
        throws ELException {
        
        if (base != null) {
            return null;
        }
        if ( base == null && property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        Object result = null;
        context.setPropertyResolved(true);
        FacesContext facesContext = (FacesContext) 
            context.getContext(FacesContext.class);
        try {
            result = legacyVR.resolveVariable(facesContext, 
                (String)property);
        } catch (EvaluationException ex) {
            context.setPropertyResolved(false);
            throw new ELException(ex);
        }
        return result;
    }


    public Class getType(ELContext context, Object base, Object property) 
        throws ELException {
        
        Object result = null;
        result = getValue(context, base, property);
        if (result != null) {
            return result.getClass();
        }
        return null;
    }

    public void  setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
	if (null == base && null == property) {
	    throw new PropertyNotFoundException();
	}
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException {                      
        if (null == base && null == property) {
	    throw new PropertyNotFoundException();
	}
        return false;
    }

  
    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    public Class getCommonPropertyType(ELContext context, Object base) {
        if ( base == null ) {
            return String.class;
        }
        return null;
    }

}
