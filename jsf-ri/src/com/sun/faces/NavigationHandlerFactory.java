/*
 * $Id: NavigationHandlerFactory.java,v 1.1 2002/01/25 18:45:16 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// NavigationHandlerFactory.java

package com.sun.faces;

import javax.faces.FactoryFinder;
import javax.faces.FactoryConfigurationError;
import javax.faces.NavigationHandler;
import javax.faces.NavigationMap;

import javax.faces.FacesException;

/**
 *
 *  <B>NavigationHandlerFactory</B> Creates NavigationHandler instances using
 *  the pluggable implementation scheme defined in the spec. <P>
 *
 * <B>Lifetime And Scope</B> <P>
 * There should be one instance of NavigationHandlerFactory per app. <P></P>
 * It is created and used like this: <P></P>

<CODE><PRE>
   
    NavigationHandler context;
    NavigationHandlerFactory factory;
    try {
	factory = NavigationHandlerFactory.newInstance();
	context = factory.newNavigationHandler();
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
    }

</PRE></CODE>

 *
 * @version $Id: NavigationHandlerFactory.java,v 1.1 2002/01/25 18:45:16 visvan Exp $
 * 
 * @see	javax.faces.NavigationHandler
 *
 */

public abstract class NavigationHandlerFactory extends Object
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

    protected NavigationHandlerFactory()
    {
        super();
    }

    //
    // Class methods
    //

    public static NavigationHandlerFactory newInstance() throws FactoryConfigurationError  {
        try {
            return (NavigationHandlerFactory) FactoryFinder.find(
               /* The default property name according to the JSFaces spec */
               "com.sun.faces.NavigationHandlerFactory",
               /* The fallback implementation class name */
               "com.sun.faces.NavigationHandlerFactoryImpl");
        } catch (FactoryFinder.ConfigurationError e) {
            throw new FactoryConfigurationError(e.getException(),
                                                e.getMessage());
        }
    }


    //
    // Abstract Methods 
    //

    /**
      * @throws FacesException if any of these objects could not be
      *         created.
      */

    public abstract NavigationHandler newNavigationHandler(NavigationMap navMap) 
            throws FacesException;

} // end of class NavigationHandlerFactory
