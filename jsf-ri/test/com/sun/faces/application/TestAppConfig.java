/*
 * $Id: TestAppConfig.java,v 1.1 2003/05/01 06:20:44 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestAppConfig.java

package com.sun.faces.application;

import com.sun.faces.ServletFacesTestCase;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;


/**
 *
 *  <B>TestAppConfig</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestAppConfig.java,v 1.1 2003/05/01 06:20:44 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestAppConfig extends ServletFacesTestCase
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

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public TestAppConfig(String name)
{
    super(name);
}

//
// Class methods
//

//
// General Methods
//

    public void tearDown() {
    }

    public void testApplicationHasAppConfig() {
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	assertTrue(null != aFactory);
        ApplicationImpl application = 
	    (ApplicationImpl)aFactory.getApplication();
	assertTrue(null != application);
	AppConfig appConfig = application.getAppConfig();
	assertTrue(null != appConfig);
	//assertTrue(null != appConfig.getConfigBase());
    }
	
	

} // end of class TestAppConfig
