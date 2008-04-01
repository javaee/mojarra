/*
 * $Id: TestFacesContextFactoryImpl.java,v 1.4 2002/08/02 01:17:39 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestFacesContextFactoryImpl.java

package com.sun.faces.context;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;
import org.apache.cactus.ServletTestCase;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.context.FacesContext;
import javax.faces.FactoryFinder;
import com.sun.faces.context.FacesContextFactoryImpl;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.tree.Tree;
import javax.faces.FacesException;

/**
 *
 *  <B>TestFacesContextFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextFactoryImpl.java,v 1.4 2002/08/02 01:17:39 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestFacesContextFactoryImpl extends ServletTestCase
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

    public TestFacesContextFactoryImpl() {super("TestFacesContextFactory");}
    public TestFacesContextFactoryImpl(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

public void testCreateMethods() 
{
    boolean gotException = false;
    FacesContext facesContext = null;
    FacesContextFactoryImpl facesContextFactory = null;

    // create FacesContextFactory.
    facesContextFactory = new FacesContextFactoryImpl();
    
    try {
        facesContext = facesContextFactory.getFacesContext(null,null, null, 
							   null);
    } catch ( FacesException fe) {
        gotException = true;
    } catch ( NullPointerException ee) {
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
        facesContext = facesContextFactory.getFacesContext(config.getServletContext(), 
							   request, 
							   response, 
							   lifecycle);
    } catch (FacesException fe) {
        gotException = true;
    }    
    assertTrue(gotException == false ); 
   
}

} // end of class TestFacesContextFactoryImpl
