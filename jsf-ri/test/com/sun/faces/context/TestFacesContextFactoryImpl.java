/*
 * $Id: TestFacesContextFactoryImpl.java,v 1.11 2004/02/26 20:34:20 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestFacesContextFactoryImpl.java

package com.sun.faces.context;

import com.sun.faces.ServletFacesTestCase;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

/**
 * <B>TestFacesContextFactoryImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextFactoryImpl.java,v 1.11 2004/02/26 20:34:20 eburns Exp $
 */

public class TestFacesContextFactoryImpl extends ServletFacesTestCase {

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

    public TestFacesContextFactoryImpl() {
        super("TestFacesContextFactory");
    }


    public TestFacesContextFactoryImpl(String name) {
        super(name);
    }
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

    public void testCreateMethods() {
        boolean gotException = false;
        FacesContext facesContext = null;
        FacesContextFactoryImpl facesContextFactory = null;

        // create FacesContextFactory.
        facesContextFactory = new FacesContextFactoryImpl();

        try {
            facesContext = facesContextFactory.getFacesContext(null, null, null,
                                                               null);
        } catch (FacesException fe) {
            gotException = true;
        } catch (NullPointerException ee) {
            gotException = true;
        }
        assertTrue(gotException);

        LifecycleFactory factory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        assertTrue(null != factory);
        Lifecycle lifecycle =
            factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        assertTrue(null != lifecycle);

        gotException = false;
        try {
            facesContext = facesContextFactory.getFacesContext(
                config.getServletContext(),
                request,
                response,
                lifecycle);
        } catch (FacesException fe) {
            gotException = true;
        }
        assertTrue(gotException == false);

    }

} // end of class TestFacesContextFactoryImpl
