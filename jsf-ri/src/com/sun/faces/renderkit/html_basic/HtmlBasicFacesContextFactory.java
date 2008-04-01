/*
 * $Id: HtmlBasicFacesContextFactory.java,v 1.1 2002/04/05 19:41:15 jvisvanathan Exp $
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
import javax.faces.FacesContextFactory;
import javax.faces.FacesContext;
import javax.faces.FacesException;


/**
 *
 *  <B>HtmlBasicFacesContextFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicFacesContextFactory.java,v 1.1 2002/04/05 19:41:15 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HtmlBasicFacesContextFactory extends FacesContextFactory
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
// Methods from FacesContextFactory
//

//
// Class methods
//

public FacesContext newFacesContext(ServletRequest request, 
        ServletResponse response, ServletContext sc ) throws FacesException {
    FacesContext result = new HtmlBasicFacesContext(request, response, sc);
    return result;
}

//
// General Methods
//

} // end of class HtmlBasicFacesContextFactory
