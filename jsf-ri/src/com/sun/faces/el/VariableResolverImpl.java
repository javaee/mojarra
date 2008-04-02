/*
 * $Id: VariableResolverImpl.java,v 1.3 2003/03/27 21:21:16 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;


import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.el.VariableResolver;
import javax.faces.el.PropertyResolver;
import javax.servlet.http.HttpSession;
import com.sun.faces.context.EvaluationContext;
import com.sun.faces.context.FacesContextImpl;


/**
 * <p>Concrete implementation of <code>VariableResolver</code>.</p>
 */

public class VariableResolverImpl extends VariableResolver {

    //
    // Relationship Instance Variables
    // 

    protected PropertyResolver propertyResolver = null;


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
		    value = ec.getApplicationMap().get(name);
		}
	    }
            return (value);
        }

    }

    //
    // Helper Methods
    // 

    public void setPropertyResolver(PropertyResolver pResolver) {
	propertyResolver = pResolver;
    }

    public PropertyResolver getPropertyResolver() {
	return propertyResolver;
    }


}
