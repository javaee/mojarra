/*
 * $Id: RenderResponsePhase.java,v 1.3 2003/02/18 18:01:17 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RenderResponsePhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.lifecycle.ViewHandlerImpl; 

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
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
 * @version $Id: RenderResponsePhase.java,v 1.3 2003/02/18 18:01:17 craigmcc Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#UPDATE_MODEL_VALUES_PHASE
 *
 */

public class RenderResponsePhase extends GenericPhaseImpl
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

public RenderResponsePhase(Lifecycle newDriver, int newId)
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

public int execute(FacesContext facesContext) throws FacesException
{
    if (((FacesContextImpl)facesContext).getResponseComplete()) {
        return Phase.GOTO_EXIT;
    }
    int rc = Phase.GOTO_NEXT;
    Assert.assert_it(null != lifecycleDriver.getViewHandler());
    try { 
	lifecycleDriver.getViewHandler().renderView(facesContext); 
    } catch (IOException e) { 
	throw new FacesException(e.getMessage(), e);
    } catch (ServletException e) { 
	throw new FacesException(e.getMessage(), e);
    }
    return rc;
}



// The testcase for this class is TestRenderResponsePhase.java


} // end of class RenderResponsePhase
