/*
 * $Id: EventQueueImpl.java,v 1.3 2001/12/20 22:26:38 ofung Exp $
 */


/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
 * @version $Id: EventQueueImpl.java,v 1.3 2001/12/20 22:26:38 ofung Exp $
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
