/*
 * $Id: NavigationHandlerFactoryImpl.java,v 1.3 2002/04/15 20:11:02 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// NavigationHandlerFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import java.util.Map;

import javax.faces.NavigationHandler;
import javax.faces.NavigationMap;
import javax.faces.Constants;

import javax.faces.FacesException;
import javax.faces.FacesFactory;

/**
 *
 *  <B>NavigationHandlerFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: NavigationHandlerFactoryImpl.java,v 1.3 2002/04/15 20:11:02 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class NavigationHandlerFactoryImpl implements FacesFactory
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

    public NavigationHandlerFactoryImpl()
    {
        super();
    }

    //
    // Methods from NavigationHandlerFactory
    //

    //
    // Class methods
    //

   //
    // Methods from FacesFactory
    //

    public Object newInstance(String facesName, ServletRequest req,
                          ServletResponse res) throws FacesException
    {
        throw new FacesException("Can't create NavigationHandler from Request and Response");
    }

    public Object newInstance(String facesName, ServletContext ctx) throws FacesException
    {
        throw new FacesException("Can't create NavigationHandler from ServletContext");
    }

    public Object newInstance(String facesName) throws FacesException
    {
        throw new FacesException("Can't create NavigationHandler");   
    }

    public Object newInstance(String facesName, Map args) throws FacesException
    {
        return new NavigationHandlerImpl(((NavigationMap)args.get(Constants.REF_NAVIGATIONMAP)));
    }
    
    //
    // General Methods
    //

} // end of class NavigationHandlerFactoryImpl
