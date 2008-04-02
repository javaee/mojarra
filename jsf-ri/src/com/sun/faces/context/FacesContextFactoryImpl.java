/*
 * $Id: FacesContextFactoryImpl.java,v 1.6 2003/02/20 22:48:35 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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

import com.sun.faces.util.Util;

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
    public FacesContext getFacesContext(ServletContext sc,
					ServletRequest request,
					ServletResponse response,
					Lifecycle lifecycle)
        throws FacesException {

        try {
            ParameterCheck.nonNull(sc);
            ParameterCheck.nonNull(request);
            ParameterCheck.nonNull(response);
            ParameterCheck.nonNull(lifecycle);
        } catch (Exception e ) {
            throw new NullPointerException(Util.getExceptionMessage(Util.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }    
	
        return (new FacesContextImpl(sc, request,
                                     response, lifecycle));

    }


// The testcase for this class is TestFacesContextFactory.java 


} // end of class FacesContextFactoryImpl
