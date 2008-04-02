/*
 * $Id: VariableResolverImpl.java,v 1.23 2005/05/18 17:33:46 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.util.Arrays;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.el.ELConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.ELException;

import com.sun.faces.util.Util;

/**
 * <p>
 * Concrete implementation of <code>VariableResolver</code>.
 * </p>
 */

public class VariableResolverImpl extends VariableResolver {
    
    private static final Log log = LogFactory
            .getLog(VariableResolverImpl.class);
    
    private ELResolver elResolver = null;

    public VariableResolverImpl(ELResolver resolver ) {
        this.elResolver = resolver;
    }
    
    //
    // Relationship Instance Variables
    // 

    // Specified by javax.faces.el.VariableResolver.resolveVariable()
    public Object resolveVariable(FacesContext context, String name)
            throws EvaluationException {
        Object result = null;
        if (context == null || name == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " context " + context + " name " + name;
            throw new NullPointerException(message);
        }
        
        try {
            result = elResolver.getValue(context.getELContext(), null, name);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }
}
