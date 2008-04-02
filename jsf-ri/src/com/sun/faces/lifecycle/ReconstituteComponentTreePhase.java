/*
 * $Id: ReconstituteComponentTreePhase.java,v 1.8 2003/05/15 22:25:48 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ReconstituteComponentTreePhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.application.ApplicationFactory;
import javax.faces.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
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
import com.sun.faces.util.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: ReconstituteComponentTreePhase.java,v 1.8 2003/05/15 22:25:48 rkitain Exp $
 * 
 */

public class ReconstituteComponentTreePhase extends Phase {
//
// Protected Constants
//
    // Log instance for this class
    protected static Log log = LogFactory.getLog(ReconstituteComponentTreePhase.class);

//
// Class Variables
//

//
// Instance Variables
//
private TreeFactory treeFactory = null;
private ActionListener actionListener = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Genericializers    
//

public ReconstituteComponentTreePhase() {
    treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);

    ApplicationFactory aFactory = (ApplicationFactory)
        FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    if (aFactory != null) {
        actionListener = aFactory.getApplication().getActionListener();
    }
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


public int getId() {
    return Phase.RECONSTITUTE_COMPONENT_TREE;
}

/**

* PRECONDITION: the necessary factories have been installed in the
* ServletContext attr set. <P>

* POSTCONDITION: The facesContext has been initialized with a tree. 

*/

public void execute(FacesContext facesContext) throws FacesException
{
    if (null == facesContext) {
	throw new FacesException(Util.getExceptionMessage(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
    }

    // Create the requested component tree
    Tree requestTree = null;
    
    // look up saveStateInClient parameter to check whether to restore
    // state of tree from client or server. Default is server.
    String saveState = facesContext.getExternalContext().
        getInitParameter(RIConstants.SAVESTATE_INITPARAM);
    if ( saveState != null ) {
        Assert.assert_it (saveState.equalsIgnoreCase("true") || 
            saveState.equalsIgnoreCase("false"));
    }     
    if (saveState == null || saveState.equalsIgnoreCase("false")) {
        restoreTreeFromSession(facesContext);
    } else {
        restoreTreeFromPage(facesContext);           
    }
}    
        
public void restoreTreeFromPage(FacesContext facesContext) {
    Tree requestTree = null;
    Locale locale = null;
    long beginTime = 0;

    if (log.isTraceEnabled()) {
        beginTime = System.currentTimeMillis();
    }
   
    // reconstitute tree from page. 
    Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
    String treeId = (String)requestMap.get("javax.servlet.include.path_info");
    if (treeId == null) {
        treeId = facesContext.getExternalContext().getRequestPathInfo();
    }
    
    String treeRootString = (String)requestMap.get(RIConstants.FACES_TREE);
    if ( treeRootString == null ) {
        requestTree = treeFactory.getTree(facesContext, treeId);
    } else {    
        byte[] bytes  = Base64.decode(treeRootString.getBytes());
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(bytes));
            requestTree = (Tree) ois.readObject();
            locale = (Locale) ois.readObject();
            ois.close();
            if (log.isDebugEnabled()) {
                DebugUtil.printTree(requestTree.getRoot(), System.out);
            }
        } catch (java.io.OptionalDataException ode) {
            log.error(ode.getMessage(), ode);
        } catch (java.lang.ClassNotFoundException cnfe) {
            log.error(cnfe.getMessage(), cnfe);
        } catch (java.io.IOException iox) {
            log.error(iox.getMessage(), iox);
        }
    }
    facesContext.setTree(requestTree);
    if ( locale != null ) {
        facesContext.setLocale(locale);
    }
    processTree(facesContext);
    // PENDING(visvan): If we wanted to track time, here is where we'd do it
    if (log.isTraceEnabled()) {
        long endTime = System.currentTimeMillis();
        log.trace("Time to reconstitute tree " + (endTime-beginTime));
    }
}

protected void restoreTreeFromSession(FacesContext facesContext) {
    Tree requestTree = null;
    
    // PENDING(visvan) - will not deal with simultaneous requests
    // for the same session
    Map sessionMap = Util.getSessionMap(facesContext);

    // Reconstitute or create the request tree
    Map requestMap = facesContext.getExternalContext().getRequestMap();
    String treeId = (String) 
               requestMap.get("javax.servlet.include.path_info");
    if (treeId == null) {
        treeId = facesContext.getExternalContext().getRequestPathInfo();
    }
    requestTree = (Tree) sessionMap.get(RIConstants.FACES_TREE);
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
	
    facesContext.setTree(requestTree);
    sessionMap.remove(RIConstants.FACES_TREE);

    // Set up the request locale if needed
    Locale locale = (Locale)sessionMap.get(RIConstants.REQUEST_LOCALE);
    if (locale == null) {
        locale = facesContext.getExternalContext().getRequestLocale();
    }
    facesContext.setLocale(locale);
    sessionMap.remove(RIConstants.REQUEST_LOCALE);
    processTree(facesContext);
}

protected void processTree(FacesContext facesContext) {
    UIComponent root = facesContext.getTree().getRoot();
    try {
        root.processReconstitutes(facesContext);
    } catch (java.io.IOException iox) {
        log.error(iox.getMessage(), iox);
    }
    if (actionListener != null) {
        registerActionListeners(root);
    }
}

protected void registerActionListeners(UIComponent uic) {
    Iterator kids = uic.getFacetsAndChildren();
    while (kids.hasNext()) {
        registerActionListeners((UIComponent) kids.next());
    }
    if (uic instanceof UICommand) {
      
        // register actionlistener if it has not been registered already
        // PENDING (visvan) This could cause a problem because the components
        // are exposed to applications. Instead move this logic to the root
        // component.
        if ( (uic.getAttribute("com.sun.faces.ActionListener")) == null ) {
            ((UICommand)uic).addActionListener(actionListener);
            uic.setAttribute("com.sun.faces.ActionListener", Boolean.TRUE);
        }     
    }
}

// The testcase for this class is TestReconstituteComponentTreePhase.java


} // end of class ReconstituteComponentTreePhase
