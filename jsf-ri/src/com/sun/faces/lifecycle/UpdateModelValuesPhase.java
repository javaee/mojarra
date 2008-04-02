/*
 * $Id: UpdateModelValuesPhase.java,v 1.39 2005/06/23 20:29:34 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.lifecycle;

import com.sun.faces.util.MessageFactory;
import com.sun.faces.util.Util;
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
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.LIFECYCLE_LOGGER);

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
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Entering UpdateModelValuesPhase");
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

        if (null != exceptionMessage) {
            Object[] params = new Object[3];
            params[2] = exceptionMessage;
            facesContext.addMessage(component.getClientId(facesContext),
                    MessageFactory.getMessage(facesContext,
                    Util.MODEL_UPDATE_ERROR_MESSAGE_ID, params));
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(exceptionMessage);
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Exiting UpdateModelValuesPhase");
            }
        }
    }



// The testcase for this class is TestUpdateModelValuesPhase.java


} // end of class UpdateModelValuesPhase
