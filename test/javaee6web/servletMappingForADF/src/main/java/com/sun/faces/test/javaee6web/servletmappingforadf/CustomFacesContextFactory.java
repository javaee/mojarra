package com.sun.faces.test.javaee6web.servletmappingforadf;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

public class CustomFacesContextFactory extends FacesContextFactory {
     private FacesContextFactory delegate;

    public CustomFacesContextFactory(FacesContextFactory facesContextFactory) {
      delegate = facesContextFactory;
    }

    public FacesContext getFacesContext(Object context, Object request, Object response,
      Lifecycle lifecycle) throws FacesException {
        FacesContext result = delegate.getFacesContext(context, request, response, lifecycle);
        result.getExternalContext().getApplicationMap().put("testValue", "true");

        return result;
    }
}
