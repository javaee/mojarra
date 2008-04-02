/*
 * $Id: ProcessValidationsPhase.java,v 1.24 2005/06/23 20:29:33 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.faces.util.Util;

/**
 * ProcessValidationsPhase executes <code>processValidators</code> on each
 * component in the tree.
 */
public class ProcessValidationsPhase extends Phase {

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

    public ProcessValidationsPhase() {
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
        return PhaseId.PROCESS_VALIDATIONS;
    }


    public void execute(FacesContext facesContext) throws FacesException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Entering ProcessValidationsPhase");
        }
        UIComponent component = facesContext.getViewRoot();
        assert (null != component);

        try {
            component.processValidators(facesContext);
        } catch (RuntimeException re) {
            String exceptionMessage = re.getMessage();
            if (null != exceptionMessage) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, exceptionMessage, re);
                }
            }
	    throw new FacesException(exceptionMessage, re);
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Exiting ProcessValidationsPhase");
        }
    }


// The testcase for this class is TestProcessValidationsPhase.java


} // end of class ProcessValidationsPhase
