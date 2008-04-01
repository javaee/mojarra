/*
 * $Id: EventContextFactory.java,v 1.1 2002/01/11 20:05:59 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// EventContextFactory.java

package com.sun.faces;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.faces.FactoryFinder;
import javax.faces.FactoryConfigurationError;
import javax.faces.EventContext;
import javax.faces.RenderContext;
import javax.faces.FacesException;

/**
 *

 *  <B>EventContextFactory</B> Creates EventContext instances using
 *  the pluggable implementation scheme defined in the spec. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * There should be one instance of EventContextFactory per app. <P></P>

 * It is created and used like this: <P></P>

<CODE><PRE>
    RenderKit kit = null;
    EventContext context;
    EventContextFactory factory;
    Renderer renderer;
    
    try {
	factory = EventContextFactory.newInstance();
	context = factory.newEventContext(servletRequest, servletResponse);
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
    }

</PRE></CODE>

 *
 * @version $Id: EventContextFactory.java,v 1.1 2002/01/11 20:05:59 edburns Exp $
 * 
 * @see	javax.faces.EventContext
 *
 */

public abstract class EventContextFactory extends Object
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

protected EventContextFactory()
{
    super();
}

//
// Class methods
//

public static EventContextFactory newInstance() throws FactoryConfigurationError  {
    try {
	return (EventContextFactory) FactoryFinder.find(
	   /* The default property name according to the JSFaces spec */
	   "com.sun.faces.EventContextFactory",
	   /* The fallback implementation class name */
	   "com.sun.faces.EventContextFactoryImpl");
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

public abstract EventContext newEventContext(RenderContext renderContext,
					     ServletRequest request,
					     ServletResponse response) throws FacesException;

} // end of class EventContextFactory
