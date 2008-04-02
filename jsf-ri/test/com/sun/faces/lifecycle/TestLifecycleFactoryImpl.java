/*
 * $Id: TestLifecycleFactoryImpl.java,v 1.13 2005/10/19 19:51:34 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// TestLifecycleFactoryImpl.java

package com.sun.faces.lifecycle;

import com.sun.faces.cactus.ServletFacesTestCase;
import org.apache.cactus.ServletTestCase;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import java.util.Iterator;

/**
 * <B>TestLifecycleFactoryImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleFactoryImpl.java,v 1.13 2005/10/19 19:51:34 edburns Exp $
 */

public class TestLifecycleFactoryImpl extends ServletFacesTestCase {

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

    public TestLifecycleFactoryImpl() {
        super("TestLifecycleFactoryImpl");
    }


    public TestLifecycleFactoryImpl(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void testDefault() {
        LifecycleFactoryImpl factory = new LifecycleFactoryImpl();
        Lifecycle life = null, life2 = null;

        assertTrue(factory != null);

        // Make sure the default instance exists
        life = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        assertTrue(null != life);

        // Make sure multiple requests for the same name give the same
        // instance.
        life2 = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        assertTrue(life == life2);
    }


    public void testIdIterator() {
        LifecycleFactoryImpl factory = new LifecycleFactoryImpl();

        String
            l1 = "l1",
            l2 = "l2",
            l3 = "l3";
        LifecycleImpl
            life1 = new LifecycleImpl(),
            life2 = new LifecycleImpl(),
            life3 = new LifecycleImpl();
        int i = 0;
        Iterator iter = null;

        factory.addLifecycle(l1, life1);
        factory.addLifecycle(l2, life2);
        factory.addLifecycle(l3, life3);

        iter = factory.getLifecycleIds();
        while (iter.hasNext()) {
            iter.next();
            i++;
        }

        assertTrue(4 == i);
    }


    public void testIllegalArgumentException() {
        LifecycleFactoryImpl factory = new LifecycleFactoryImpl();
        Lifecycle life = null;
        assertTrue(factory != null);

        boolean exceptionThrown = false;
        // Try to get an IllegalArgumentException
        try {
            LifecycleImpl lifecycle = new LifecycleImpl();
            factory.addLifecycle("bogusId", lifecycle);
            factory.addLifecycle("bogusId", lifecycle);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        } catch (UnsupportedOperationException e) {
            assertTrue(false);
        }
        assertTrue(exceptionThrown);
    }


} // end of class TestLifecycleFactoryImpl
