/*
 * $Id: EventQueueFactoryImpl.java,v 1.1 2001/11/21 00:42:42 visvan Exp $
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
 * @version $Id: EventQueueFactoryImpl.java,v 1.1 2001/11/21 00:42:42 visvan Exp $
 * 
 * @see	Blah
 * @see	Bloo
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
