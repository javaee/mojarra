/*
 * $Id: FacesContextFactoryImpl.java,v 1.14 2005/08/22 22:10:11 ofung Exp $
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

package com.sun.faces.context;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FacesContextFactoryImpl extends FacesContextFactory {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public FacesContextFactoryImpl() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from FacesContextFactory
    //
    public synchronized FacesContext getFacesContext(Object sc,
                                                     Object request,
                                                     Object response,
                                                     Lifecycle lifecycle)
        throws FacesException {

        try {
            Util.parameterNonNull(sc);
            Util.parameterNonNull(request);
            Util.parameterNonNull(response);
            Util.parameterNonNull(lifecycle);
        } catch (Exception e) {
            throw new NullPointerException(
                Util.getExceptionMessageString(
                    Util.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }

        ServletContext ctx = (ServletContext) sc;

        // if this is the very first FacesContext instance we're being
        // asked to create.
        if (null ==
            ctx.getAttribute(RIConstants.ONE_TIME_INITIALIZATION_ATTR)) {
            // initialize our Factories
            Util.verifyFactoriesAndInitDefaultRenderKit(ctx);
        }
        return (new FacesContextImpl(new ExternalContextImpl(
            (ServletContext) sc,
            (ServletRequest) request, (ServletResponse) response), lifecycle));

    }


// The testcase for this class is TestSerlvetFacesContextFactory.java 


} // end of class FacesContextFactoryImpl
