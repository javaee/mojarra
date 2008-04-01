/*
 * $Id: TestEventQueue.java,v 1.3 2002/04/11 22:52:41 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestEventQueue.java

package com.sun.faces;

import junit.framework.TestCase;
import java.io.IOException;
import javax.faces.EventQueue;
import javax.faces.AbstractFactory;
import javax.faces.Constants;
import java.util.EventObject;

/**
 *
 *  <B>TestEventQueue</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestEventQueue.java,v 1.3 2002/04/11 22:52:41 eburns Exp $
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
	    AbstractFactory abstractFactory = new AbstractFactory();
            eventQueue = abstractFactory.newEventQueue();
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
