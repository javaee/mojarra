/*
 * $Id: TestFacesContextFactoryImpl.java,v 1.12 2005/08/22 22:11:13 ofung Exp $
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
 * @version $Id: TestFacesContextFactoryImpl.java,v 1.12 2005/08/22 22:11:13 ofung Exp $
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
