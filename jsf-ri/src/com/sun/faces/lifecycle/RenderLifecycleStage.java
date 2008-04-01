/*
 * $Id: RenderLifecycleStage.java,v 1.3 2002/03/18 21:24:00 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RenderLifecycleStage.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesContext;
import javax.faces.FacesException;
import javax.faces.ObjectManager;
import javax.faces.EventContext;
import javax.faces.TreeNavigator;
import javax.faces.LifecycleStage;
import javax.faces.RenderKit;
import javax.faces.FacesException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletResponse;

import com.sun.faces.util.Util;
import com.sun.faces.lifecycle.RenderWrapper;

import java.io.IOException;

/**
 *
 *  <B>RenderLifecycleStage</B> 
 *
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * LifecycleDriverImpl.
 *
 * @version $Id: RenderLifecycleStage.java,v 1.3 2002/03/18 21:24:00 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.LifecycleDriverImpl
 *
 */

public class RenderLifecycleStage extends GenericLifecycleStage
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

public RenderLifecycleStage(LifecycleDriverImpl newDriver, 
			    String newName)
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


*/

public boolean execute(FacesContext ctx, TreeNavigator root) throws FacesException
{
    boolean result = false;
    EventContext eventContext = ctx.getEventContext();
    ServletRequest request = eventContext.getRequest();
    HttpServletResponse response = (HttpServletResponse) eventContext.getResponse();
    ObjectManager objectManager = eventContext.getObjectManager();
    RenderWrapper renderWrapper = (RenderWrapper) objectManager.get(request, 
					    Constants.REF_RENDERWRAPPER);

    Assert.assert_it(null != renderWrapper);

    // Make sure the client does not cache the response.
    
    // PENDING(edburns): not sure if this is necessary in all cases.
    // We probably don't need to specify no cache if the request to
    // which we're forwarding is not a faces page.
    response.addHeader("Pragma:", "No-cache");
    response.addHeader("Cache-control:", "no-cache");
    response.addHeader("Expires:", "1");

    try {
	renderWrapper.commenceRendering(ctx, root);
	result = true;
    }
    catch (Throwable e) {
	e.printStackTrace();
	throw new FacesException(e.getMessage());
    }

    
    return result;
}


// The testcase for this class is TestLifecycleDriverImpl.java 


} // end of class RenderLifecycleStage
