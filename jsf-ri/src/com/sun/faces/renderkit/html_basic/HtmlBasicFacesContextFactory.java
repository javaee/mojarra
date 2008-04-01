/*
 * $Id: HtmlBasicFacesContextFactory.java,v 1.2 2002/04/11 22:52:41 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicFacesContextFactory.java

package com.sun.faces.renderkit.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import javax.faces.FacesFactory;
import javax.faces.FacesContext;
import javax.faces.FacesException;

import java.util.Map;

/**
 *
 *  <B>HtmlBasicFacesContextFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicFacesContextFactory.java,v 1.2 2002/04/11 22:52:41 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HtmlBasicFacesContextFactory extends Object implements FacesFactory
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

public HtmlBasicFacesContextFactory()
{
    super();
}

//
// Methods from FacesFactory
//

public Object newInstance(String facesName, ServletRequest req, 
			  ServletResponse res) throws FacesException
{
    ServletContext sc = 
	((HttpServletRequest)req).getSession().getServletContext();
    FacesContext result = new HtmlBasicFacesContext(req, res, sc);
    return result;
}

public Object newInstance(String facesName, ServletContext ctx) throws FacesException
{
    throw new FacesException("Can't create FacesContext from ServletContext");
}

public Object newInstance(String facesName) throws FacesException
{
    throw new FacesException("Can't create FacesContext from nothing");
}

public Object newInstance(String facesName, Map args) throws FacesException
{
    throw new FacesException("Can't create FacesContext from map");
}

//
// General Methods
//

} // end of class HtmlBasicFacesContextFactory
