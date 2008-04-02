/*
 * $Id: VariableResolverImpl.java,v 1.7 2003/05/06 19:21:50 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;


import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.el.VariableResolver;
import javax.faces.FactoryFinder;

import javax.servlet.http.HttpSession;

import com.sun.faces.context.FacesContextImpl;
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
        } else if ("tree".equals(name)) {
            return (context.getTree().getRoot());
        } else {
	    // do the scoped lookup thing
            Object value = null;

	    if (null == (value = ec.getRequestMap().get(name))) {
		if (null == (value = ec.getSessionMap().get(name))) {
		    if (null == (value = ec.getApplicationMap().get(name))) {
                         // if it's a managed bean try and create it
                         ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                         ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
                         value = application.getAppConfig().createAndMaybeStoreManagedBeans(context, name);
                    }
		}
	    }
            return (value);
        }

    }
}
