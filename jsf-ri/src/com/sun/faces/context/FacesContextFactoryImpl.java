/*
 * $Id: FacesContextFactoryImpl.java,v 1.12 2004/02/26 20:32:37 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
                Util.getExceptionMessage(
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
