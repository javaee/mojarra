/*
 * $Id: DummyVariableResolverImpl.java,v 1.1 2005/05/05 20:51:21 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

/**
 * Default VariableResolver implementation that gets the ELContext from the 
 * argument FacesContext and calls setPropertyResolved(false) on it. This is
 * provided to ensure that the legacy variable resolvers continue to work with
 * unfied EL API
 */

public class DummyVariableResolverImpl extends VariableResolver{
   
    // Specified by javax.faces.el.VariableResolver.resolveVariable()
    public Object resolveVariable(FacesContext context, String name)
            throws EvaluationException {
        context.getELContext().setPropertyResolved(false);
        return null;
    }
}