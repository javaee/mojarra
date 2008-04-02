/*
 * $Id: VariableResolverImpl.java,v 1.21 2004/11/09 04:23:11 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.util.Arrays;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.el.impl.ELConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

/**
 * <p>
 * Concrete implementation of <code>VariableResolver</code>.
 * </p>
 */

public class VariableResolverImpl extends VariableResolver implements ELConstants
{
    private static final Log log = LogFactory
            .getLog(VariableResolverImpl.class);

    //
    // Relationship Instance Variables
    // 

    // Specified by javax.faces.el.VariableResolver.resolveVariable()
    public Object resolveVariable(FacesContext context, String name)
            throws EvaluationException
    {

        ExternalContext extCtx = context.getExternalContext();

        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, name);

        if (index < 0)
        {
            Object value = extCtx.getRequestParameterMap().get(name);
            if (value == null)
            {
                value = extCtx.getRequestMap().get(name);
                if (value == null)
                {
                    value = extCtx.getSessionMap().get(name);
                    if (value == null)
                    {
                        value = extCtx.getApplicationMap().get(name);
                        if (value == null)
                        {
                            // if it's a managed bean try and create it
                            ApplicationAssociate associate = ApplicationAssociate
                                    .getInstance(context.getExternalContext());

                            if (null != associate)
                            {
                                value = associate
                                        .createAndMaybeStoreManagedBeans(
                                                context, name);
                            }
                        }
                    }
                }
            }
            return value;
        }
        else
        {
            switch (index)
            {
                case APPLICATION:
                    return extCtx.getContext();
                case APPLICATION_SCOPE:
                    return extCtx.getApplicationMap();
                case COOKIE:
                    return extCtx.getRequestCookieMap();
                case FACES_CONTEXT:
                    return context;
                case HEADER:
                    return extCtx.getRequestHeaderMap();
                case HEADER_VALUES:
                    return extCtx.getRequestHeaderValuesMap();
                case INIT_PARAM:
                    return extCtx.getInitParameterMap();
                case PARAM:
                    return extCtx.getRequestParameterMap();
                case PARAM_VALUES:
                    return extCtx.getRequestParameterValuesMap();
                case REQUEST:
                    return extCtx.getRequest();
                case REQUEST_SCOPE:
                    return extCtx.getRequestMap();
                case SESSION:
                    return extCtx.getSession(true);
                case SESSION_SCOPE:
                    return extCtx.getSessionMap();
                case VIEW:
                    return context.getViewRoot();
                default:
                    return null;
            }
        }
    }
}