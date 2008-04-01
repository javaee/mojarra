/*
 * $Id: SaveStateLifecycleStage.java,v 1.1 2002/03/13 18:04:23 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SaveStateLifecycleStage.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesContext;
import javax.faces.RenderContext;
import javax.faces.EventContext;
import javax.faces.TreeNavigator;
import javax.faces.LifecycleStage;
import javax.faces.FacesException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 *
 *  <B>SaveStateLifecycleStage</B> 
 *
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * LifecycleDriverImpl.
 *
 * @version $Id: SaveStateLifecycleStage.java,v 1.1 2002/03/13 18:04:23 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.LifecycleDriverImpl
 *
 */

public class SaveStateLifecycleStage extends GenericLifecycleStage
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

protected LifecycleDriverImpl lifecycleDriver = null;

//
// Constructors and Initializers    
//

public SaveStateLifecycleStage(LifecycleDriverImpl newDriver, String newName)
{
    super(newDriver, newName);
}

//
// Class methods
//

//
// General Methods
//

// 
// Methods from LifecycleStage
//

/**

* PENDING(edburns): Since we don't have save state in page, we just save
* this tree into the session.

*/

public boolean execute(FacesContext ctx, TreeNavigator root) throws FacesException
{
    RenderContext renderContext = ctx.getRenderContext();
    HttpServletRequest request =(HttpServletRequest)renderContext.getRequest();
    HttpSession session = request.getSession();
    String requestURI = request.getRequestURI();
    
    session.setAttribute(requestURI, root);
    return true;
}


// Delete this text and replace the below text with the name of the file
// containing the testcase covering this class.  If this text is here
// someone touching this file is poor software engineer.

// The testcase for this class is TestSaveStateLifecycleStage.java 


} // end of class SaveStateLifecycleStage
