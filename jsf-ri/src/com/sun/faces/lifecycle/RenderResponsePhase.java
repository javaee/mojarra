/*
 * $Id: RenderResponsePhase.java,v 1.16 2005/06/23 20:29:33 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RenderResponsePhase.java

package com.sun.faces.lifecycle;


import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import java.util.logging.Logger;
import java.util.logging.Level;
import com.sun.faces.util.Util;


/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RenderResponsePhase.java,v 1.16 2005/06/23 20:29:33 jayashri Exp $
 */

public class RenderResponsePhase extends Phase {

//
// Protected Constants
//
// Log instance for this class
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.LIFECYCLE_LOGGER);

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


    public void execute(FacesContext facesContext) throws FacesException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Entering RenderResponsePhase");
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("About to render view " +
                    facesContext.getViewRoot().getViewId());
        }
        try {
            facesContext.getApplication().getViewHandler().
                renderView(facesContext, facesContext.getViewRoot());
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Exiting RenderResponsePhase");
        }
    }



// The testcase for this class is TestRenderResponsePhase.java


} // end of class RenderResponsePhase
