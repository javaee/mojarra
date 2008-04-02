/*
 * $Id: RenderResponsePhase.java,v 1.5 2003/03/12 19:51:06 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RenderResponsePhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.lifecycle.ViewHandlerImpl; 

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.tree.Tree;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import javax.servlet.http.HttpServletRequest;

import java.util.Iterator;
import java.io.IOException;
import javax.servlet.ServletException;
import com.sun.faces.RIConstants;
import com.sun.faces.context.FacesContextImpl;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RenderResponsePhase.java,v 1.5 2003/03/12 19:51:06 rkitain Exp $
 *
 */

public class RenderResponsePhase extends Phase {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
private Lifecycle lifecycleDriver = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Genericializers    
//

public RenderResponsePhase(Lifecycle newDriver) {
    lifecycleDriver = newDriver;
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
    return Phase.RENDER_RESPONSE;
}

public void execute(FacesContext facesContext) throws FacesException
{
    Assert.assert_it(null != lifecycleDriver.getViewHandler());
    try { 
	lifecycleDriver.getViewHandler().renderView(facesContext); 
    } catch (IOException e) { 
	throw new FacesException(e.getMessage(), e);
    } catch (ServletException e) { 
	throw new FacesException(e.getMessage(), e);
    }
}



// The testcase for this class is TestRenderResponsePhase.java


} // end of class RenderResponsePhase
