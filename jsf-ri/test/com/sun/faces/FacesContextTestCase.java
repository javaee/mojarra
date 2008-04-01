/*
 * $Id: FacesContextTestCase.java,v 1.1 2002/05/30 18:32:55 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesContextTestCase.java

package com.sun.faces;

import org.apache.cactus.ServletTestCase;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;

/**
 *
 *  <B>FacesContextTestCase</B> is a base class that creates a
 *  FacesContextFactory instance in its ctor, and a FacesContext
 *  instance in its setUp();
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FacesContextTestCase.java,v 1.1 2002/05/30 18:32:55 eburns Exp $
 * 
 * @see	com.sun.faces.context.FacesContextFactoryImpl
 * @see	com.sun.faces.context.FacesContextImpl
 *
 */

public abstract class FacesContextTestCase extends ServletTestCase
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

protected FacesContextFactory facesContextFactory = null;

protected FacesContext facesContext = null;

//
// Constructors and Initializers    
//

    public FacesContextTestCase() {
	super("FacesContextTestCase");
	init();
    }

    public FacesContextTestCase(String name) {
	super(name);
	init();
    }

public void init()
{
    System.setProperty(FactoryFinder.FACES_CONTEXT_FACTORY,
		       "com.sun.faces.context.FacesContextFactoryImpl");
    facesContextFactory = (FacesContextFactory) 
	FactoryFinder.createFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    assertTrue(null != facesContextFactory);
}

//
// Class methods
//

//
// General Methods
//

public void setUp()
{
    facesContext = 
	facesContextFactory.createFacesContext(config.getServletContext(),
					       request, response);
    assertTrue(null != facesContext);
}

} // end of class FacesContextTestCase
