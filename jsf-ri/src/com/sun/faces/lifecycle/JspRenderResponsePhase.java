/*
 * $Id: JspRenderResponsePhase.java,v 1.3 2002/06/09 01:43:08 eburns Exp $
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
import javax.faces.tree.Tree;
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
 * @version $Id: JspRenderResponsePhase.java,v 1.3 2002/06/09 01:43:08 eburns Exp $
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

This implementation just replaces the file extension on treeId with
".jsp".

*/

protected String fixTreeId(String treeId) throws FacesException
{
    ParameterCheck.nonNull(treeId);

    String result = null;
    int i = 0;

    if (-1 == (i = treeId.indexOf("."))) {
	throw new FacesException("invalid treeId");
    }

    result = treeId.substring(0, i) + ".jsp";

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
    Tree tree = facesContext.getResponseTree();
    try {
	requestURI = fixTreeId(tree.getTreeId());
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
