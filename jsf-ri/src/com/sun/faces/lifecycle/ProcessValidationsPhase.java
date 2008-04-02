/*
 * $Id: ProcessValidationsPhase.java,v 1.20 2004/02/26 20:32:48 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.lifecycle;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

/**
 * ProcessValidationsPhase executes <code>processValidators</code> on each
 * component in the tree.
 */
public class ProcessValidationsPhase extends Phase {

//
// Protected Constants
//
    
// Log instance for this class
    protected static Log log = LogFactory.getLog(ProcessValidationsPhase.class);

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
        if (log.isDebugEnabled()) {
            log.debug("Entering ProcessValidationsPhase");
        }
        UIComponent component = facesContext.getViewRoot();
        Util.doAssert(null != component);

        try {
            component.processValidators(facesContext);
        } catch (RuntimeException re) {
            String exceptionMessage = re.getMessage();
            if (null != exceptionMessage) {
                if (log.isErrorEnabled()) {
                    log.error(exceptionMessage, re);
                }
                throw new FacesException(exceptionMessage, re);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Exiting ProcessValidationsPhase");
        }
    }


// The testcase for this class is TestProcessValidationsPhase.java


} // end of class ProcessValidationsPhase
