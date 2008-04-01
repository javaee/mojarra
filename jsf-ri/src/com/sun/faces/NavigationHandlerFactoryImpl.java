/*
 * $Id: NavigationHandlerFactoryImpl.java,v 1.2 2002/04/05 19:41:13 jvisvanathan Exp $
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

import javax.faces.NavigationHandler;
import javax.faces.NavigationMap;

import javax.faces.FacesException;


/**
 *
 *  <B>NavigationHandlerFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: NavigationHandlerFactoryImpl.java,v 1.2 2002/04/05 19:41:13 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class NavigationHandlerFactoryImpl extends NavigationHandlerFactory
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

    public NavigationHandler newNavigationHandler( NavigationMap navMap ) 
            throws FacesException {

        NavigationHandler result = new NavigationHandlerImpl( navMap);
        return result;
    }

    //
    // General Methods
    //

} // end of class NavigationHandlerFactoryImpl
