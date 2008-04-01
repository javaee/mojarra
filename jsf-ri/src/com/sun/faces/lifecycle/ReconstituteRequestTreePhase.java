/*
 * $Id: ReconstituteRequestTreePhase.java,v 1.1 2002/07/11 20:33:20 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ReconstituteRequestTreePhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import com.sun.faces.RIConstants;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: ReconstituteRequestTreePhase.java,v 1.1 2002/07/11 20:33:20 jvisvanathan Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#CREATE_REQUEST_TREE_PHASE
 *
 */

public class ReconstituteRequestTreePhase extends GenericPhaseImpl
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
private RenderKit renderKit = null;
private TreeFactory treeFactory = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Genericializers    
//

public ReconstituteRequestTreePhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId);
    
    RenderKitFactory factory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    Assert.assert_it(factory != null);
    renderKit = factory.getRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT);
    treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
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
    int rc = Phase.GOTO_NEXT;

    // PENDING(visvan) - will not deal with simultaneous requests
    // for the same session
    HttpSession session = facesContext.getHttpSession();

    // Reconstitute or create the request tree
    Tree requestTree = (Tree) session.getAttribute(RIConstants.FACES_TREE);
    if (requestTree == null) {
        HttpServletRequest request = (HttpServletRequest)
            facesContext.getServletRequest();
        String treeId = (String) 
               request.getAttribute("javax.servlet.include.path_info");
        if (treeId == null) {
            treeId = request.getPathInfo();
        }
        requestTree = treeFactory.getTree(facesContext.getServletContext(),
                                          treeId);
        requestTree.setRenderKit(renderKit);
    }   
    facesContext.setRequestTree(requestTree);
    session.removeAttribute(RIConstants.FACES_TREE);

    // Set up the request locale if needed
    Locale locale = (Locale) session.getAttribute(RIConstants.REQUEST_LOCALE);
    if (locale == null) {
        locale = facesContext.getServletRequest().getLocale();
    }
    facesContext.setLocale(locale);
    session.removeAttribute(RIConstants.REQUEST_LOCALE);
    return rc;
}

// The testcase for this class is TestReconstituteRequestTreePhase.java


} // end of class ReconstituteRequestTreePhase
