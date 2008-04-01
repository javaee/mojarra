/*
 * $Id: FacesContextTestCaseJsp.java,v 1.1 2002/06/07 00:01:12 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesContextTestCaseJsp.java

package com.sun.faces;

import org.apache.cactus.JspTestCase;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import javax.servlet.jsp.PageContext;

/**
 *
 *  <B>FacesContextTestCaseJsp</B> is a base class that creates a
 *  FacesContextFactory instance in its ctor, and a FacesContext
 *  instance in its setUp();
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FacesContextTestCaseJsp.java,v 1.1 2002/06/07 00:01:12 eburns Exp $
 * 
 * @see	com.sun.faces.context.FacesContextFactoryImpl
 * @see	com.sun.faces.context.FacesContextImpl
 *
 */

public abstract class FacesContextTestCaseJsp extends JspTestCase
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

    public FacesContextTestCaseJsp() { super("FacesContextTestCaseJsp"); }

    public FacesContextTestCaseJsp(String name) { super(name); }

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
    pageContext.setAttribute(FacesContext.FACES_CONTEXT_ATTR, facesContext,
			     PageContext.REQUEST_SCOPE);
    assertTrue(null != facesContext);
    TestBean testBean = new TestBean();
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
}

public void tearDown()
{
    Util.releaseServletContextFromFaces(config.getServletContext());
    (facesContext.getHttpSession()).removeAttribute("TestBean");
}

} // end of class FacesContextTestCaseJsp
