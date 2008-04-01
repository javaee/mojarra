/*
 * $Id: TestEventQueue.java,v 1.1 2001/12/06 22:59:17 visvan Exp $
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

// TestEventQueue.java

package com.sun.faces;

import junit.framework.TestCase;
import java.io.IOException;
import javax.faces.EventQueue;
import javax.faces.EventQueueFactory;
import java.util.EventObject;

/**
 *
 *  <B>TestEventQueue</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestEventQueue.java,v 1.1 2001/12/06 22:59:17 visvan Exp $
 * 
 *
 */

public class TestEventQueue extends TestCase
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
    EventQueueFactory eqFactory = null;
    EventQueue eventQueue = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public TestEventQueue(String name) {
        super(name);
    }

    public void setUp() {
        // create renderContext
        try {
            eqFactory = EventQueueFactory.newInstance();
            System.out.println("created EventQueueFactory: " +
                           eqFactory);
            eventQueue = eqFactory.newEventQueue();
            System.out.println("Created eventQueue " +
                           eventQueue);
        }
        catch (Exception e) {
            System.out.println("Exception getting factory!!! " + e.getMessage());
        }
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public void testEventQueue() {
        boolean gotException = false;
        
       // Test add method
       System.out.println("Test add method");
       try {
            eventQueue.add(null);
        } catch ( Exception e ) {
            gotException = true;
            System.out.println("Expected exception: " + e.getMessage());
        }
        assertTrue(gotException);

        EventObject eobj =  new EventObject("test");
        eventQueue.add(eobj); 
       
        // Test clear method
        System.out.println("Test clear method");
        eventQueue.clear();
        // make sure event queue is cleared
        eobj = eventQueue.getNext();
        assertTrue(eobj == null );
       
       // test isEmpty
        System.out.println("Test isEmpty method");
       boolean isEmpty = eventQueue.isEmpty();
       assertTrue(isEmpty);
       // add events. isEmpty should return false
       EventObject ce =  new EventObject("test");
       eventQueue.add(ce);
       eventQueue.add(ce);
       isEmpty = eventQueue.isEmpty();
       assertTrue(isEmpty == false );

      // test getNext
       System.out.println("Test getNext method");
      eobj = eventQueue.getNext();
      assertTrue(eobj != null );
     
      // test peekNext
      System.out.println("Test peekNext method");
      eobj = eventQueue.peekNext();
      assertTrue(eobj != null );

    }

} // end of class TestRenderKit
