/*
 * $Id: RenderResponsePhase.java,v 1.12 2004/02/04 23:41:38 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RenderResponsePhase.java

package com.sun.faces.lifecycle;


import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RenderResponsePhase.java,v 1.12 2004/02/04 23:41:38 ofung Exp $
 *
 */

public class RenderResponsePhase extends Phase {
//
// Protected Constants
//
// Log instance for this class
protected static Log log = LogFactory.getLog(RenderResponsePhase.class);

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

public RenderResponsePhase() {
    super();
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

public PhaseId getId() {
    return PhaseId.RENDER_RESPONSE;
}

public void execute(FacesContext facesContext) throws FacesException
{
    if (log.isDebugEnabled()) {
        log.debug("Entering RenderResponsePhase");
    }
    if (log.isDebugEnabled()) {
        log.debug("About to render view " + facesContext.getViewRoot().getViewId());
    }
    try { 
	facesContext.getApplication().getViewHandler().
                renderView(facesContext, facesContext.getViewRoot()); 
    } catch (IOException e) { 
	throw new FacesException(e.getMessage(), e);
    }
    if (log.isDebugEnabled()) {
        log.debug("Exiting RenderResponsePhase");
    }
}



// The testcase for this class is TestRenderResponsePhase.java


} // end of class RenderResponsePhase
