/*
 * $Id: ReconstituteRequestTreePhase.java,v 1.7 2002/10/07 20:39:50 jvisvanathan Exp $
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
import javax.faces.FactoryFinder;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Base64;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import com.sun.faces.util.DebugUtil;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: ReconstituteRequestTreePhase.java,v 1.7 2002/10/07 20:39:50 jvisvanathan Exp $
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
private TreeFactory treeFactory = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Genericializers    
//

public ReconstituteRequestTreePhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId);
    
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
        // PENDING (visvan) localize
	throw new FacesException("null FacesContext");
    }

    // Create the requested component tree
    int rc = Phase.GOTO_NEXT;
    Tree requestTree = null;
    
    // look up saveStateInClient parameter to check whether to restore
    // state of tree from client or server. Default is server.
    String saveState = facesContext.getServletContext().getInitParameter
            (RIConstants.SAVESTATE_INITPARAM);
    if ( saveState != null ) {
        Assert.assert_it (saveState.equalsIgnoreCase("true") || 
            saveState.equalsIgnoreCase("false"));
    }     
    if (saveState == null || saveState.equalsIgnoreCase("false")) {
        restoreTreeFromSession(facesContext);
    } else {
        restoreTreeFromPage(facesContext);           
    }
    return rc;
}    
        
public void restoreTreeFromPage(FacesContext facesContext) {
    Tree requestTree = null;
    //long beginTime = System.currentTimeMillis();
   
    // reconstitute tree from page. 
    HttpServletRequest request = (HttpServletRequest)
            facesContext.getServletRequest();
    String treeId = (String) 
               request.getAttribute("javax.servlet.include.path_info");
    if (treeId == null) {
        treeId = request.getPathInfo();
    }
    
    String treeRootString = request.getParameter(RIConstants.FACES_TREE);
    if ( treeRootString == null ) {
        requestTree = treeFactory.getTree(facesContext, treeId);
    } else {    
        byte[] bytes  = Base64.decode(treeRootString.getBytes());
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            requestTree = (Tree) ois.readObject();
            ois.close();
            // DebugUtil.printTree(root, System.out);
        } catch (java.io.OptionalDataException ode) {
            // PENDING (visvan) log error
            System.err.println(ode.getMessage());
        } catch (java.lang.ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        } catch (java.io.IOException iox) {
            System.err.println(iox.getMessage());
        }
    }
    facesContext.setRequestTree(requestTree);
    // PENDING(visvan): If we wanted to track time, here is where we'd do it
    // long endTime = System.currentTimeMillis();
    // System.out.println("Time to reconstitute tree " + (endTime-beginTime));
}

protected void restoreTreeFromSession(FacesContext facesContext) {
    Tree requestTree = null;
    
    // PENDING(visvan) - will not deal with simultaneous requests
    // for the same session
    HttpSession session = facesContext.getHttpSession();

    // Reconstitute or create the request tree
    HttpServletRequest request = (HttpServletRequest)
            facesContext.getServletRequest();
    String treeId = (String) 
               request.getAttribute("javax.servlet.include.path_info");
    if (treeId == null) {
        treeId = request.getPathInfo();
    }
    requestTree = (Tree) session.getAttribute(RIConstants.FACES_TREE);
    // If there is nothing in the session, 
    if (requestTree == null) {
	// create the tree from the pathInfo
        requestTree = treeFactory.getTree(facesContext, treeId);
    } 
    else {
	// There is something in the session.  Make sure its TreeId,
	// matches the treeId from the pathInfo.
	if ((null != treeId) && !treeId.equals(requestTree.getTreeId())) {
	    // If it doesn't match, use the pathInfo
	    requestTree = treeFactory.getTree(facesContext,treeId);
	}
	// If it does match, use the tree from the Session
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
}

// The testcase for this class is TestReconstituteRequestTreePhase.java


} // end of class ReconstituteRequestTreePhase
