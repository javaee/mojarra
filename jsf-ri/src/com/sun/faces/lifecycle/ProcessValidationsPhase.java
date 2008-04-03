/*
 * $Id: ProcessValidationsPhase.java,v 1.28 2007/04/25 04:07:01 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.faces.util.FacesLogger;

/**
 * ProcessValidationsPhase executes <code>processValidators</code> on each
 * component in the tree.
 */
public class ProcessValidationsPhase extends Phase {

//
// Protected Constants
//
    
// Log instance for this class
   private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();

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
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Entering ProcessValidationsPhase");
        }
        UIComponent component = facesContext.getViewRoot();
        assert (null != component);

        try {
            component.processValidators(facesContext);
        } catch (RuntimeException re) {
            String exceptionMessage = re.getMessage();
            if (null != exceptionMessage) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, exceptionMessage, re);
                }
            }
	    throw new FacesException(exceptionMessage, re);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Exiting ProcessValidationsPhase");
        }
    }


// The testcase for this class is TestProcessValidationsPhase.java


} // end of class ProcessValidationsPhase
