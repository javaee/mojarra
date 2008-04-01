/*
 * $Id: ApplyNewValuesLifecycleStage.java,v 1.2 2002/03/15 23:29:48 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ApplyNewValuesLifecycleStage.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesContext;
import javax.faces.RenderContext;
import javax.faces.EventContext;
import javax.faces.TreeNavigator;
import javax.faces.LifecycleStage;
import javax.faces.RenderKit;
import javax.faces.FacesException;

import javax.servlet.http.HttpServletRequest;

import com.sun.faces.util.Util;

/**
 *
 *  <B>ApplyNewValuesLifecycleStage</B> 
 *
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * LifecycleDriverImpl.
 *
 * @version $Id: ApplyNewValuesLifecycleStage.java,v 1.2 2002/03/15 23:29:48 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.LifecycleDriverImpl
 *
 */

public class ApplyNewValuesLifecycleStage extends GenericLifecycleStage
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

public ApplyNewValuesLifecycleStage(LifecycleDriverImpl newDriver, 
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
    RenderContext renderContext = ctx.getRenderContext();
    RenderKit renderKit = renderContext.getRenderKit();
    Assert.assert_it(renderKit != null);
    HttpServletRequest request = 
	(HttpServletRequest) renderContext.getRequest();

    // if we have a transaction token, see if it is valid
    if (Util.hasParameters(request)) {
	if (!Util.isTokenValid(request)) {
	    FacesException e = new FacesException("Token not valid.  Perhaps your session timed out?");
	    throw e;
	}
    }
    Util.resetToken(request);

    renderKit.applyNewValuesToTree(ctx, root);
    return true;
}

// The testcase for this class is TestLifecycleDriverImpl.java 


} // end of class ApplyNewValuesLifecycleStage
