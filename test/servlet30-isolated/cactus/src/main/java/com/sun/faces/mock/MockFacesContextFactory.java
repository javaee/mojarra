/*
 * $Id: MockFacesContextFactory.java,v 1.1 2005/10/18 17:47:55 edburns Exp $
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

package com.sun.faces.mock;

import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MockFacesContextFactory extends FacesContextFactory {
    public MockFacesContextFactory(FacesContextFactory oldImpl) {
	System.setProperty(FactoryFinder.FACES_CONTEXT_FACTORY, 
			   this.getClass().getName());
    }
    public MockFacesContextFactory() {}
    
    public FacesContext getFacesContext(Object context, Object request,
					Object response, 
					Lifecycle lifecycle) throws FacesException {
	MockFacesContext result = new MockFacesContext();
        
        ExternalContext externalContext =
                new MockExternalContext((ServletContext) context, 
                (ServletRequest) request, (ServletResponse) response);
        result.setExternalContext(externalContext);
        ApplicationFactory applicationFactory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = (MockApplication) applicationFactory.getApplication();
        result.setApplication(application);
        
	ELContext elContext = new MockELContext(new MockELResolver());
	elContext.putContext(FacesContext.class, result);
        result.setELContext(elContext);
        
        return result;
    }
}

