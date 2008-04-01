/*
 * $Id: TestEventDispatcherFactory.java,v 1.1 2001/12/06 22:59:17 visvan Exp $
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
 * @version $Id: TestEventDispatcherFactory.java,v 1.1 2001/12/06 22:59:17 visvan Exp $
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
