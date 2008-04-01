/*
 * $Id: CreateRequestTreePhase.java,v 1.5 2002/06/21 00:31:21 eburns Exp $
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
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.RIConstants;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: CreateRequestTreePhase.java,v 1.5 2002/06/21 00:31:21 eburns Exp $
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
    String initialRequestParam = null,
    renderKitId = facesContext.getServletRequest().getParameter("renderKit");

    String pathInfo = 
            ((HttpServletRequest) facesContext.getServletRequest()).getPathInfo();
    
    String treeId = getTreeIdFromPathInfo(pathInfo);
    TreeFactory treeFactory = null;
    RenderKitFactory renderKitFactory = null;
    RenderKit renderKit = null;
    int rc = Phase.GOTO_NEXT;

    treeFactory = (TreeFactory)
	FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(null != treeFactory);
    
    // PENDING(edburns): deal with possibly null treeId
    facesContext.setRequestTree(treeFactory.getTree(servletContext,treeId));
    
    if (renderKitId != null) {
	renderKitFactory = (RenderKitFactory)
	    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	Assert.assert_it(null != renderKitFactory);
	renderKit = renderKitFactory.getRenderKit(renderKitId);
	if (renderKit != null) {
	    facesContext.getRequestTree().setRenderKit(renderKit);
	}
    }
    facesContext.setLocale(facesContext.getServletRequest().getLocale());

    // If the request contained a param of the form
    // RIConstants.INITIAL_REQUEST_NAME=RIConstants.INITIAL_REQUEST_VALUE
    // Go straight to render
    if (null != (initialRequestParam = (String)
		 facesContext.getServletRequest().getParameter(RIConstants.INITIAL_REQUEST_NAME))
	&&
	initialRequestParam.equals(RIConstants.INITIAL_REQUEST_VALUE)) {
        rc = Phase.GOTO_RENDER;
    } 
    return rc;
}

protected String getTreeIdFromPathInfo(String path_info) {
            
    String treeId = null;
    String path = null;
    ParameterCheck.nonNull(path_info);
   
    // If it is an initial request, then treeId is after /faces like
    // /Faces_Basic.xul. No parsing is required.
    if (!(path_info.startsWith(RIConstants.FORM_PREFIX) || 
            path_info.startsWith(RIConstants.COMMAND_PREFIX))) {
        return path_info;
    }   

    // if it is postback case, then pathInfo will be of the form
    // /form/formName/treeid or /command/commandName/treeid.
    // We cannot count on locating treeId between the two slashes
    // because it could be nested like /jsp/examples/welcome.xul.
    String submitStr = null;
    if ( path_info.startsWith(RIConstants.FORM_PREFIX )) {
        submitStr = RIConstants.FORM_PREFIX;
    } else if ( path_info.startsWith(RIConstants.COMMAND_PREFIX )) {
        submitStr = RIConstants.COMMAND_PREFIX;  
    }    
   
    path = path_info.substring(submitStr.length());
    if ( path != null ) {
        int slash = path.indexOf('/');
        if (slash >= 0) {
            treeId = path.substring(slash, path.length());
        }    
    }
    return treeId;
    }    
// The testcase for this class is TestCreateRequestTreePhase.java


} // end of class CreateRequestTreePhase
