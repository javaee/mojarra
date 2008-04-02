/*
 * $Id: VariableResolverImpl.java,v 1.26 2006/01/11 15:28:06 rlubke Exp $
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

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import javax.el.ELResolver;
import javax.el.ELException;

import com.sun.faces.util.MessageUtils;

/**
 * <p>
 * Concrete implementation of <code>VariableResolver</code>.
 * </p>
 */

public class VariableResolverImpl extends VariableResolver {

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
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
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
