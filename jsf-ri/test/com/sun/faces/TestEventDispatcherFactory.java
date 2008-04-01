/*
 * $Id: TestEventDispatcherFactory.java,v 1.2 2001/12/20 22:26:43 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestEventDispatcherFactory.java

package com.sun.faces;

import junit.framework.TestCase;
import java.io.IOException;
import javax.faces.EventDispatcherFactory;
import java.util.EventObject;
import javax.faces.CommandDispatcher;
import javax.faces.ValueChangeDispatcher;
import javax.faces.CommandEvent;
import javax.faces.ValueChangeEvent;
import javax.faces.EventDispatcher;
import javax.faces.FacesException;

/**
 *
 *  <B>TestEventDispatcherFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestEventDispatcherFactory.java,v 1.2 2001/12/20 22:26:43 ofung Exp $
 * 
 *
 */

public class TestEventDispatcherFactory extends TestCase
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
    EventDispatcherFactory edFactory = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public TestEventDispatcherFactory(String name) {
        super(name);
    }

    public void setUp() {
        // create renderContext
        try {
            edFactory = EventDispatcherFactory.newInstance();
            System.out.println("created EventDispatcherFactory: " +
                           edFactory);
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

    public void testEventDispatcherFactory() {
        boolean gotException = false;
        EventDispatcher d = null;
 
       // Test newEventDispatcher method
       System.out.println("Test newEventDispatcher method");
       try {
            edFactory.newEventDispatcher(null);
        } catch ( Exception e ) {
            gotException = true;
            System.out.println("Expected exception: " + e.getMessage());
        }
        assertTrue(gotException);
       
        gotException = false; 
        // passing commandEvent should return CommandDispatcher
        try {
            CommandEvent ce = new CommandEvent(null, "test", "test");
            d = edFactory.newEventDispatcher(ce);
        } catch (Exception e ) {
            System.out.println("Unexpected Exception " +e.getMessage());
            gotException = true;
        }
        assertTrue (gotException==false );

        gotException = false;
        // passing commandEvent should return CommandDispatcher
        try {
            ValueChangeEvent vce = new ValueChangeEvent(null,"test", "test", "test");
            d = edFactory.newEventDispatcher(vce);
            assertTrue ( d instanceof ValueChangeDispatcher);
        } catch (Exception e ) {
            System.out.println("Unexpected Exception " +e.getMessage());
            gotException = true;
        }
        assertTrue (gotException==false );

        gotException = false;
        // passing EventObject should return null
        try {
            EventObject eobj = new EventObject("test");
            d = edFactory.newEventDispatcher(eobj);
        } catch (Exception e ) {
            System.out.println("Unexpected Exception " +e.getMessage());
            gotException = true;
        }
        assertTrue (gotException==true );
    }

} // end of class TestEventDispatcherFactory
