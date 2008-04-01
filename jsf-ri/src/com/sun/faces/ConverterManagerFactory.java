/*
 * $Id: ConverterManagerFactory.java,v 1.2 2002/04/11 22:52:39 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ConverterManagerFactory.java

package com.sun.faces;

import javax.servlet.ServletContext;

import javax.faces.FactoryFinder;
import javax.faces.FactoryConfigurationError;
import javax.faces.ConverterManager;
import javax.faces.FacesException;

/**
 *

 *  <B>ConverterManagerFactory</B> Creates ConverterManager instances using
 *  the pluggable implementation scheme defined in the spec. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * There should be one instance of ConverterManagerFactory per app. <P></P>

 * It is created and used like this: <P></P>

<CODE><PRE>
    ConverterManagerFactory factory;
    try {
	factory = ConverterManagerFactory.newInstance();
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
    }

</PRE></CODE>

 *
 * @version $Id: ConverterManagerFactory.java,v 1.2 2002/04/11 22:52:39 eburns Exp $
 * 
 * @see	javax.faces.ConverterManager
 *
 */

public abstract class ConverterManagerFactory extends Object
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

    protected ConverterManagerFactory()
    {
        super();
    }

    //
    // Class methods
    //

    public static ConverterManagerFactory newInstance() 
            throws FactoryConfigurationError  {
            return (ConverterManagerFactory) FactoryFinder.find(
               /* The default property name according to the JSFaces spec */
               "com.sun.faces.ConverterManagerFactory",
               /* The fallback implementation class name */
               "com.sun.faces.ConverterManagerFactoryImpl");
    }


    //
    // Abstract Methods 
    //

    /**
      * @throws FacesException if any of these objects could not be
      *         created.
      */

    public abstract ConverterManager newConverterManager(ServletContext servletContext) 
            throws FacesException;

} // end of class ConverterManagerFactory
