/*
 * $Id: FacesContextTestCaseJsp.java,v 1.4 2002/06/07 23:29:15 eburns Exp $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * @version $Id: FacesContextTestCaseJsp.java,v 1.4 2002/06/07 23:29:15 eburns Exp $
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

public HttpServletRequest getRequest() 
{
    return request;
}

public HttpServletResponse getResponse()
{
    return response;
}

public void setUp()
{
    Util.verifyFactoriesAndInitDefaultRenderKit(config.getServletContext());
    
    facesContextFactory = (FacesContextFactory) 
	FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    assertTrue(null != facesContextFactory);
    
    facesContext = 
	facesContextFactory.createFacesContext(config.getServletContext(),
					       getRequest(), getResponse());
    pageContext.setAttribute(FacesContext.FACES_CONTEXT_ATTR, facesContext,
			     PageContext.REQUEST_SCOPE);
    assertTrue(null != facesContext);
    TestBean testBean = new TestBean();
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    System.setProperty(RIConstants.DISABLE_RENDERERS, 
		       RIConstants.DISABLE_RENDERERS);
}

public void tearDown()
{
    Util.releaseFactoriesAndDefaultRenderKit(config.getServletContext());
    (facesContext.getHttpSession()).removeAttribute("TestBean");
}

} // end of class FacesContextTestCaseJsp
