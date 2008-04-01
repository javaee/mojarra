/*
 * $Id: JspRenderResponsePhase.java,v 1.1 2002/06/07 00:01:02 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// JspRenderResponsePhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;

import java.util.Iterator;
import java.io.IOException;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: JspRenderResponsePhase.java,v 1.1 2002/06/07 00:01:02 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#UPDATE_MODEL_VALUES_PHASE
 *
 */

public class JspRenderResponsePhase extends GenericPhaseImpl
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
// Constructors and Genericializers    
//

public JspRenderResponsePhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId);
}

//
// Class methods
//

//
// General Methods
//

 /**
  * Strips off the first part of the path. <P>
  * PRECONDITION: requestURI is at least a filename.
  */
protected String fixURI(String requestURI) 
{
    String result = requestURI;
    int i = requestURI.indexOf("/");
    if (-1 != i && requestURI.length() > i) {
	requestURI = requestURI.substring(i + 1);
	i = requestURI.indexOf("/");
	if (-1 != i && requestURI.length() > i) {
	    result = requestURI.substring(i);
	}
    } else {
	// This uri doesn't have a leading slash.  Make sure it does.
	result = "/" + requestURI;
    }
    return result;
}


//
// Methods from Phase
//

public int execute(FacesContext facesContext) throws FacesException
{
    int rc = Phase.GOTO_NEXT;
    String requestURI = null;
    HttpServletRequest request = (HttpServletRequest) 
	facesContext.getServletRequest();
    RequestDispatcher requestDispatcher = null;
    try {
	requestURI = fixURI(request.getRequestURI());
	requestDispatcher = request.getRequestDispatcher(requestURI);
	requestDispatcher.forward(request, facesContext.getServletResponse());
    }
    catch (IOException e) {
	throw new FacesException("Can't forward", e);
    }
    catch (ServletException e) {
	throw new FacesException("Can't forward", e);
    }
    return rc;
}



// The testcase for this class is TestRenderResponsePhase.java


} // end of class JspRenderResponsePhase
