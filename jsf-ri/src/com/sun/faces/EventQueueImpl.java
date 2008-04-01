
/*
 * $Id: EventQueueImpl.java,v 1.2 2001/12/06 22:59:16 visvan Exp $
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

// EventQueueImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.EventQueue;
import java.util.Vector;
import java.util.Iterator;
import java.util.EventObject;

/**
 *
 *  <B>EventQueueImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: EventQueueImpl.java,v 1.2 2001/12/06 22:59:16 visvan Exp $
 * @author Jayashri Visvanathan
 * 
 * @see	javax.faces.EventQueue
 *
 */


public class EventQueueImpl extends EventQueue {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private Vector eventQueue;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public EventQueueImpl() {
        eventQueue = new Vector();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    // Methods from EventQueue.
    //

    public void add(EventObject e) {
        ParameterCheck.nonNull(e);
        eventQueue.add(e);
    }

    public void clear() {
        eventQueue.removeAllElements();
    }

    public boolean isEmpty() {
        return eventQueue.isEmpty();
    }

    public void remove(EventObject e) {
        ParameterCheck.nonNull(e);
        eventQueue.remove(e);
    }

    public EventObject getNext() {
        EventObject e = null;
        if ( eventQueue.size() > 0 ) {
             e = (EventObject) eventQueue.elementAt(0);
        }
        return e;
    }

    public EventObject peekNext() {
        EventObject e = null;
        if ( eventQueue.size() > 1 ) {
             e = (EventObject) eventQueue.elementAt(1);
        }
        return e;
    }

    public Iterator iterator() {
        return eventQueue.iterator();
    }

} // end of class EventQueueImpl
