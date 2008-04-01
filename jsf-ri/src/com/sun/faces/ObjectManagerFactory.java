/*
 * $Id: ObjectManagerFactory.java,v 1.1 2002/01/10 22:20:09 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ObjectManagerFactory.java

package com.sun.faces;

import javax.servlet.ServletRequest;

import javax.faces.FactoryFinder;
import javax.faces.FactoryConfigurationError;
import javax.faces.ObjectManager;
import javax.faces.FacesException;

/**
 *

 *  <B>ObjectManagerFactory</B> Creates ObjectManager instances using
 *  the pluggable implementation scheme defined in the spec. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * There should be one instance of ObjectManagerFactory per app. <P></P>

 * It is created and used like this: <P></P>

<CODE><PRE>
    RenderKit kit = null;
    ObjectManager context;
    ObjectManagerFactory factory;
    Renderer renderer;
    
    try {
	factory = ObjectManagerFactory.newInstance();
	context = factory.newObjectManager(servletRequest);
	kit = context.getRenderKit();
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
    }

</PRE></CODE>

 *
 * @version $Id: ObjectManagerFactory.java,v 1.1 2002/01/10 22:20:09 edburns Exp $
 * 
 * @see	javax.faces.ObjectManager
 *
 */

public abstract class ObjectManagerFactory extends Object
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

protected ObjectManagerFactory()
{
    super();
}

//
// Class methods
//

public static ObjectManagerFactory newInstance() throws FactoryConfigurationError  {
    try {
	return (ObjectManagerFactory) FactoryFinder.find(
	   /* The default property name according to the JSFaces spec */
	   "javax.faces.ObjectManagerFactory",
	   /* The fallback implementation class name */
	   "com.sun.faces.ObjectManagerFactoryImpl");
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

public abstract ObjectManager newObjectManager() throws FacesException;

} // end of class ObjectManagerFactory
