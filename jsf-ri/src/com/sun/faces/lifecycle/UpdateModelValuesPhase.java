/*
 * $Id: UpdateModelValuesPhase.java,v 1.45 2007/04/25 04:07:01 rlubke Exp $
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

import com.sun.faces.util.FacesLogger;

import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;


/**
 * UpdateModelValuesPhase executes <code>processUpdates</code> on each
 * component in the tree so that it may have a chance to update its model value.
 */
public class UpdateModelValuesPhase extends Phase {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    // Log instance for this class
    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();

    // Attribute Instance Variables

    // Relationship Instance Variables


    //
    // Constructors and Genericializers    
    //

    public UpdateModelValuesPhase() {
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
        return PhaseId.UPDATE_MODEL_VALUES;
    }


    public void execute(FacesContext facesContext) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Entering UpdateModelValuesPhase");
        }
        UIComponent component = facesContext.getViewRoot();
        assert (null != component);
        String exceptionMessage = null;

        try {
            component.processUpdates(facesContext);
        } catch (IllegalStateException e) {
            exceptionMessage = e.getMessage();
        } catch (FacesException fe) {
            exceptionMessage = fe.getMessage();
        }

        // Just log the exception.  Any exception occurring from 
        // processUpdates should have been stored as a message
        // on FacesContext.
        if (exceptionMessage != null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(exceptionMessage);
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Exiting UpdateModelValuesPhase");
            }
        }
    }



// The testcase for this class is TestUpdateModelValuesPhase.java


} // end of class UpdateModelValuesPhase
