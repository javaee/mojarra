/*
 * $Id: EventQueueFactoryImpl.java,v 1.3 2001/12/20 22:26:38 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// EventQueueFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.EventQueueFactory;
import javax.faces.EventQueue;

/**
 *
 *  <B>EventQueueFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: EventQueueFactoryImpl.java,v 1.3 2001/12/20 22:26:38 ofung Exp $
 * @author Jayashri Visvanathan
 * 
 * @see	javax.faces.EventQueueFactory
 * @see	javax.faces.EventQueue
 *
 */

public class EventQueueFactoryImpl extends EventQueueFactory
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

    public EventQueueFactoryImpl()
    {
        super();
    }

    //
    // Methods from EventQueueFactory
    //

    //
    // Class methods
    //

    public EventQueue newEventQueue() throws FacesException {
        EventQueue result = new EventQueueImpl();
        return result;
    }

    //
    // General Methods
    //

} // end of class EventQueueFactoryImpl
