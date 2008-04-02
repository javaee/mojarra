/*
 * $Id: VariableResolverImpl.java,v 1.13 2003/09/18 15:02:07 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;


import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.el.VariableResolver;
import javax.faces.FactoryFinder;

import com.sun.faces.application.ApplicationImpl;




/**
 * <p>Concrete implementation of <code>VariableResolver</code>.</p>
 */

public class VariableResolverImpl extends VariableResolver {

    //
    // Relationship Instance Variables
    // 

    // Specified by javax.faces.el.VariableResolver.resolveVariable()
    public Object resolveVariable(FacesContext context, String name) {

	ExternalContext ec = context.getExternalContext();

        if ("applicationScope".equals(name)) {
            return (ec.getApplicationMap());
        } else if ("cookie".equals(name)) {
            return (ec.getRequestCookieMap());
        } else if ("facesContext".equals(name)) {
            return (context);
        } else if ("header".equals(name)) {
            return (ec.getRequestHeaderMap());
        } else if ("headerValues".equals(name)) {
            return (ec.getRequestHeaderValuesMap());
        } else if ("initParam".equals(name)) {
            return (ec.getInitParameterMap());
        } else if ("param".equals(name)) {
            return (ec.getRequestParameterMap());
        } else if ("paramValues".equals(name)) {
            return (ec.getRequestParameterValuesMap());
        } else if ("requestScope".equals(name)) {
            return (ec.getRequestMap());
        } else if ("sessionScope".equals(name)) {
            return (ec.getSessionMap());
        } else if ("view".equals(name)) {
            return (context.getViewRoot());
        } else {
	    // do the scoped lookup thing
            Object value = null;

	    if (null == (value = ec.getRequestMap().get(name))) {
            if (null == (value = ec.getSessionMap().get(name))) {
                if (null == (value = ec.getApplicationMap().get(name))) {
                    // if it's a managed bean try and create it
                    ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder.getFactory(
                            FactoryFinder.APPLICATION_FACTORY);
                    Application application = aFactory.getApplication();
                    if (application instanceof ApplicationImpl) {
                        value = ((ApplicationImpl) application).createAndMaybeStoreManagedBeans(context, name);
                    }
                }
            }
        }
            return (value);
        }

    }
}
