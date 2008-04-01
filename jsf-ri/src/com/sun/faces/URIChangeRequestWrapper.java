/*
 * $Id: URIChangeRequestWrapper.java,v 1.2 2002/03/13 18:04:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// URIChangeRequestWrapper.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.NoSuchElementException;

/**
 *

 *  The sole purpose of <B>URIChangeRequestWrapper</B> is to wrap an
 *  HttpServletRequest and change its request URI, and block any
 *  params. This is necessary to allow navigation to work.  <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * A instance of this class is created just before we forward the
 * request to the request dispatcher in the navigation logic.
 * No local reference
 * is kept to this instance and we rely on automatic garbage collection. <P>

 *
 * @version $Id: URIChangeRequestWrapper.java,v 1.2 2002/03/13 18:04:21 eburns Exp $
 * 
 * @see	com.sun.faces.CommandDispatcherImpl#dispatch
 *
 */

public class URIChangeRequestWrapper extends ParamBlockingRequestWrapper
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

private String requestURI;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

/**
 
* called from CommandDispatcherImpl.

*/

public URIChangeRequestWrapper(HttpServletRequest toWrap, String newRequestURI)
{
    super(toWrap);
    requestURI = newRequestURI;
}


//
// Class methods
//

//
// Methods from ServletRequest 
//

public String getRequestURI() {
    return requestURI;
}




} // end of class URIChangeRequestWrapper


