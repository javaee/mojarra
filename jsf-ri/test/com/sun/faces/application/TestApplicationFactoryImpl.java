/*
 * $Id: TestApplicationFactoryImpl.java,v 1.11 2006/03/29 23:04:38 rlubke Exp $
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

// TestApplicationFactoryImpl.java

package com.sun.faces.application;

import com.sun.faces.cactus.JspFacesTestCase;

import com.sun.faces.config.ConfigureListener;
import javax.faces.application.Application;

/**
 * <B>TestApplicationFactoryImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestApplicationFactoryImpl.java,v 1.11 2006/03/29 23:04:38 rlubke Exp $
 */

public class TestApplicationFactoryImpl extends JspFacesTestCase {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    private ApplicationFactoryImpl applicationFactory = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestApplicationFactoryImpl() {
        super("TestApplicationFactoryImpl");
    }


    public TestApplicationFactoryImpl(String name) {
        super(name);
    }
//
// Class methods
//

//
// General Methods
//

    public void testFactory() {
        applicationFactory = new ApplicationFactoryImpl();
	com.sun.faces.config.StoreServletContext storeSC = 
	    new com.sun.faces.config.StoreServletContext();
	storeSC.setServletContext(config.getServletContext());
	ApplicationAssociate.clearInstance(storeSC.getServletContextWrapper());


        // 1. Verify "getApplication" returns the same Application instance
        //    if called multiple times.
        //  
        Application application1 = applicationFactory.getApplication();
        Application application2 = applicationFactory.getApplication();
        assertTrue(application1 == application2);

        // 2. Verify "setApplication" adds instances.. /
        //    and "getApplication" returns the same instance
        //
	ApplicationAssociate.clearInstance(storeSC.getServletContextWrapper());
        Application application3 = new ApplicationImpl();
        applicationFactory.setApplication(application3);
        Application application4 = applicationFactory.getApplication();
        assertTrue(application3 == application4);
    }


    public void testSpecCompliance() {
        applicationFactory = new ApplicationFactoryImpl();
	com.sun.faces.config.StoreServletContext storeSC = 
	    new com.sun.faces.config.StoreServletContext();
	storeSC.setServletContext(config.getServletContext());
	ApplicationAssociate.clearInstance(storeSC.getServletContextWrapper());

        assertTrue(null != applicationFactory.getApplication());
    }


    public void testExceptions() {
        applicationFactory = new ApplicationFactoryImpl();

        // 1. Verify NullPointer exception which occurs when attempting
        //    to add a null Application
        //
        boolean thrown = false;
        try {
            applicationFactory.setApplication(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }


} // end of class TestApplicationFactoryImpl
