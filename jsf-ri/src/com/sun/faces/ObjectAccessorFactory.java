/*
 * $Id: ObjectAccessorFactory.java,v 1.1 2002/01/12 01:41:16 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ObjectAccessorFactory.java

package com.sun.faces;

import javax.servlet.ServletRequest;

import javax.faces.FactoryFinder;
import javax.faces.FactoryConfigurationError;
import javax.faces.ObjectAccessor;
import javax.faces.FacesException;
import javax.faces.RenderContext;

/**
 *

 *  <B>ObjectAccessorFactory</B> Creates ObjectAccessor instances using
 *  the pluggable implementation scheme defined in the spec. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * There should be one instance of ObjectAccessorFactory per app. <P></P>

 * It is created and used like this: <P></P>

<CODE><PRE>
    RenderKit kit = null;
    ObjectAccessor context;
    ObjectAccessorFactory factory;
    Renderer renderer;
    
    try {
	factory = ObjectAccessorFactory.newInstance();
	context = factory.newObjectAccessor(renderContext);
	kit = context.getRenderKit();
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
    }

</PRE></CODE>

 *
 * @version $Id: ObjectAccessorFactory.java,v 1.1 2002/01/12 01:41:16 edburns Exp $
 * 
 * @see	javax.faces.ObjectAccessor
 *
 */

public abstract class ObjectAccessorFactory extends Object
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

protected ObjectAccessorFactory()
{
    super();
}

//
// Class methods
//

public static ObjectAccessorFactory newInstance() throws FactoryConfigurationError  {
    try {
	return (ObjectAccessorFactory) FactoryFinder.find(
	   /* The default property name according to the JSFaces spec */
	   "com.sun.faces.ObjectAccessorFactory",
	   /* The fallback implementation class name */
	   "com.sun.faces.ObjectAccessorFactoryImpl");
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

public abstract ObjectAccessor newObjectAccessor(RenderContext renderContext) throws FacesException;

} // end of class ObjectAccessorFactory
