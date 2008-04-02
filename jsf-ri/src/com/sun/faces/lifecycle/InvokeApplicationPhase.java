/*
 * $Id: InvokeApplicationPhase.java,v 1.20 2006/03/29 22:38:34 rlubke Exp $
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

// InvokeApplicationPhase.java

package com.sun.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.Util;

/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: InvokeApplicationPhase.java,v 1.20 2006/03/29 22:38:34 rlubke Exp $
 */

public class InvokeApplicationPhase extends Phase {


    // Log instance for this class
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER
                                                  + Util.LIFECYCLE_LOGGER);

    // ------------------------------------------------------------ Constructors


    public InvokeApplicationPhase() {

        super();

    }

    // ---------------------------------------------------------- Public Methods


    public void execute(FacesContext facesContext) throws FacesException {

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Entering InvokeApplicationsPhase");
        }

        UIViewRoot root = facesContext.getViewRoot();
        assert (null != root);

        try {
            root.processApplication(facesContext);
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
            logger.fine("Exiting InvokeApplicationsPhase");
        }

    }


    public PhaseId getId() {

        return PhaseId.INVOKE_APPLICATION;

    }

// The testcase for this class is TestInvokeApplicationPhase.java

} // end of class InvokeApplicationPhase
