/*
 * $Id: FacesContextFactoryImpl.java,v 1.1 2002/05/28 18:20:39 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import org.mozilla.util.ParameterCheck;

public class FacesContextFactoryImpl extends FacesContextFactory
{
    
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

    public FacesContextFactoryImpl()
    {
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
    public FacesContext createFacesContext(ServletContext sc,
                                           ServletRequest request,
                                           ServletResponse response)
        throws FacesException {

        return (createFacesContext(sc, request, response,
                                   LifecycleFactory.DEFAULT_LIFECYCLE));

    }


    public FacesContext createFacesContext(ServletContext sc,
                                           ServletRequest request,
                                           ServletResponse response,
                                           String lifecycleId)
        throws FacesException {

        try {
            ParameterCheck.nonNull(sc);
            ParameterCheck.nonNull(request);
            ParameterCheck.nonNull(response);
            ParameterCheck.nonNull(lifecycleId);
        } catch (Exception e ) {
            throw new FacesException("Cannot create FacesContext." + 
                "One or more input paramters might be null");
        }    
        return (new FacesContextImpl(sc, request,
                                     response, lifecycleId));
    }


// The testcase for this class is TestFacesContextFactory.java 


} // end of class FacesContextFactoryImpl
