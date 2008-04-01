/*
 * $Id: TestFacesContextFactoryImpl.java,v 1.2 2002/06/03 20:08:18 eburns Exp $
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
import javax.faces.context.FacesContext;
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
 * @version $Id: TestFacesContextFactoryImpl.java,v 1.2 2002/06/03 20:08:18 eburns Exp $
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
        facesContext = facesContextFactory.createFacesContext(null,null, null);
    } catch ( FacesException fe) {
        gotException = true;
    }
    assertTrue(gotException);
    
    gotException = false;
    ServletContext sc = (request.getSession()).getServletContext();
    try {
        facesContext = facesContextFactory.createFacesContext(sc, request, response);
    } catch (FacesException fe) {
        gotException = true;
    }    
    assertTrue(gotException == false );    
    
    gotException = false;
    try {
        facesContext = facesContextFactory.createFacesContext(sc, request, response,
                LifecycleFactory.DEFAULT_LIFECYCLE);
    } catch (FacesException fe) {
        gotException = true;
    }    
    assertTrue(gotException == false ); 
   
}

} // end of class TestFacesContextFactoryImpl
