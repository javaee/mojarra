/*
 * $Id: FacesContextTestCase.java,v 1.2 2002/06/01 00:58:22 eburns Exp $
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

import com.sun.faces.util.Util;

/**
 *
 *  <B>FacesContextTestCase</B> is a base class that creates a
 *  FacesContextFactory instance in its ctor, and a FacesContext
 *  instance in its setUp();
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FacesContextTestCase.java,v 1.2 2002/06/01 00:58:22 eburns Exp $
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

    public FacesContextTestCase() { super("FacesContextTestCase"); }

    public FacesContextTestCase(String name) { super(name); }

//
// Class methods
//

//
// General Methods
//

public void setUp()
{
    Util.initServletContextForFaces(config.getServletContext());
    
    facesContextFactory = (FacesContextFactory) config.getServletContext().
	getAttribute(FactoryFinder.FACES_CONTEXT_FACTORY);
    assertTrue(null != facesContextFactory);
    
    facesContext = 
	facesContextFactory.createFacesContext(config.getServletContext(),
					       request, response);
    assertTrue(null != facesContext);
}

public void tearDown()
{
    Util.releaseServletContextFromFaces(config.getServletContext());
}

} // end of class FacesContextTestCase
