/*
 * $Id: EventQueueFactory.java,v 1.1 2001/11/21 00:32:32 visvan Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// EventQueueFactory.java

package javax.faces;


/**
 *
 *
 *  <B>EventQueueFactory</B> Creates EventQueue instances using
 *  the pluggable implementation scheme defined in the spec. <P>
 *
 * <B>Lifetime And Scope</B> <P>
 *
 *
 * @version $Id: EventQueueFactory.java,v 1.1 2001/11/21 00:32:32 visvan Exp $
 * 
 * @see	javax.faces.EventQueue
 *
 */

public abstract class EventQueueFactory extends Object
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

protected EventQueueFactory()
{
    super();
}

//
// Class methods
//

public static EventQueueFactory newInstance() throws FactoryConfigurationError  {
    try {
	return (EventQueueFactory) FactoryFinder.find(
	   /* The default property name according to the JSFaces spec */
	   "javax.faces.EventQueueFactory",
	   /* The fallback implementation class name */
	   "com.sun.faces.EventQueueFactoryImpl");
    } catch (FactoryFinder.ConfigurationError e) {
	throw new FactoryConfigurationError(e.getException(),
					    e.getMessage());
    }
}


//
// Abstract Methods 
//

 /**
  * Internal constructor which initializes a <code>EventQueue</code>.
  * @throws FacesException if any of these objects could not be
  *         created.
  */

public abstract EventQueue newEventQueue() throws FacesException;

} // end of class EventQueueFactory
