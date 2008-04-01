/*
 * $Id: CreateRequestTreePhase.java,v 1.1 2002/06/01 00:58:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// CreateRequestTreePhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.PhaseListener;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContext;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: CreateRequestTreePhase.java,v 1.1 2002/06/01 00:58:21 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#CREATE_REQUEST_TREE_PHASE
 *
 */

public class CreateRequestTreePhase extends GenericPhaseImpl
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

public CreateRequestTreePhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId);
}

//
// Class methods
//

//
// General Methods
//

// 
// Methods from Phase
//


/**

* PRECONDITION: the necessary factories have been installed in the
* ServletContext attr set. <P>

* POSTCONDITION: The facesContext has been initialized with a tree. 

*/

public int execute(FacesContext facesContext) throws FacesException
{
    if (null == facesContext) {
	throw new FacesException("null FacesContext");
    }

    // Create the requested component tree
    ServletContext servletContext = facesContext.getServletContext();
    String treeId = facesContext.getServletRequest().getParameter("tree"),
	renderKitId = facesContext.getServletRequest().getParameter("renderKit");

    TreeFactory treeFactory = null;
    RenderKitFactory renderKitFactory = null;
    RenderKit renderKit = null;

    treeFactory = (TreeFactory)
	servletContext.getAttribute(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(null != treeFactory);
    
    // PENDING(edburns): deal with possibly null treeId
    facesContext.setRequestTree(treeFactory.createTree(servletContext,treeId));
    
    if (renderKitId != null) {
	renderKitFactory = (RenderKitFactory)
	    servletContext.getAttribute(FactoryFinder.RENDER_KIT_FACTORY);
	Assert.assert_it(null != renderKitFactory);
	renderKit = renderKitFactory.createRenderKit(renderKitId);
	if (renderKit != null) {
	    facesContext.getRequestTree().setRenderKit(renderKit);
	}
    }
    facesContext.setLocale(facesContext.getServletRequest().getLocale());

    return Phase.GOTO_NEXT;
}


// The testcase for this class is TestCreateRequestTreePhase.java


} // end of class CreateRequestTreePhase
