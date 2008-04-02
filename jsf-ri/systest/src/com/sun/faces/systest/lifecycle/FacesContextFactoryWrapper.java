/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.lifecycle;

import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FacesException;

public class FacesContextFactoryWrapper extends FacesContextFactory {
    
    private FacesContextFactory oldFactory = null;
    
    public FacesContextFactoryWrapper(FacesContextFactory yourOldFactory) {
	oldFactory = yourOldFactory;
    }
    
    public FacesContext getFacesContext(Object context, Object request,
					Object response, 
					Lifecycle lifecycle) throws FacesException {
	return oldFactory.getFacesContext(context, request, response, 
					  lifecycle);
    }

    public String toString() {
	return "FacesContextFactoryWrapper";
    }

}
