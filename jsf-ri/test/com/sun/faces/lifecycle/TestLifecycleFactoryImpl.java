/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
