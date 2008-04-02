/*
 * $Id: ApplyRequestValuesPhase.java,v 1.19 2005/05/16 20:16:19 rlubke Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ApplyRequestValuesPhase executes <code>processDecodes</code> on each
 * component in the tree so that it may update it's current value from the
 * information included in the current request (parameters, headers, c
 * cookies and so on.)
 */
public class ApplyRequestValuesPhase extends Phase {

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
    private static final Log log =
        LogFactory.getLog(ApplyRequestValuesPhase.class);

    // Relationship Instance Variables

    //
    // Constructors and Genericializers    
    //

    public ApplyRequestValuesPhase() {
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
        return PhaseId.APPLY_REQUEST_VALUES;
    }


    public void execute(FacesContext facesContext) throws FacesException {

        if (log.isDebugEnabled()) {
            log.debug("Entering ApplyRequestValuesPhase");
        }

        UIComponent component = facesContext.getViewRoot();
        assert (null != component);

        try {
            component.processDecodes(facesContext);
        } catch (RuntimeException re) {
            String exceptionMessage = re.getMessage();
            if (null != exceptionMessage) {
                if (log.isErrorEnabled()) {
                    log.error(exceptionMessage, re);
                }
            }
            throw new FacesException(exceptionMessage, re);
        }
        if (log.isDebugEnabled()) {
            log.debug("Exiting ApplyRequestValuesPhase");
        }
    }

    // The testcase for this class is TestApplyRequestValuesPhase.java

} // end of class ApplyRequestValuesPhase
