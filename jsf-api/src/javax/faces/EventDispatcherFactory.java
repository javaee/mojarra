/*
 * $Id: EventDispatcherFactory.java,v 1.2 2001/12/06 22:02:30 visvan Exp $
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

// EventDispatcherFactory.java

package javax.faces;

import java.util.EventObject;

/**
 *

 *  <B>EventDispatcherFactory</B> Creates EventDispatcher instances using
 *  the pluggable implementation scheme defined in the spec. <P>
 *
 * @version $Id: EventDispatcherFactory.java,v 1.2 2001/12/06 22:02:30 visvan Exp $
 * 
 *
 */

public abstract class EventDispatcherFactory extends Object
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

    protected EventDispatcherFactory()
    {
        super();
    }

    //
    // Class methods
    //

    public static EventDispatcherFactory newInstance() 
            throws FactoryConfigurationError  {
        try {
            return (EventDispatcherFactory) FactoryFinder.find(
               /* The default property name according to the JSFaces spec */
               "javax.faces.EventDispatcherFactory",
               /* The fallback implementation class name */
               "com.sun.faces.EventDispatcherFactoryImpl");
        } catch (FactoryFinder.ConfigurationError fe) {
            throw new FactoryConfigurationError(fe.getException(),
                                                fe.getMessage());
        }
    }

    //
    // Abstract Methods
    //

     /** 
      * Returns appropriate EventDispacther for the Event.
      * @param EventObject representing an event 
      * @return EventDispatcher to process the event. 
      * @throws FacesException if any of these objects could not be
      *         created.
      */

    public abstract EventDispatcher newEventDispatcher(EventObject e) 
            throws FacesException;

} // end of class EventDispatcherFactory
