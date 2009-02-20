/*
 * $Id: RenderResponsePhase.java,v 1.27 2007/12/17 21:46:09 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// RenderResponsePhase.java

package com.sun.faces.lifecycle;


import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.PhaseId;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.OnOffResponseWrapper;
import com.sun.faces.util.DebugUtil;
import javax.faces.event.PreRenderViewEvent;


/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RenderResponsePhase.java,v 1.27 2007/12/17 21:46:09 rlubke Exp $
 */

public class RenderResponsePhase extends Phase {


    // Log instance for this class
    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();


    // ---------------------------------------------------------- Public Methods


    public void execute(FacesContext facesContext) throws FacesException {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Entering RenderResponsePhase");
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("About to render view " +
                 facesContext.getViewRoot().getViewId());
        }
        // For requests intended to produce a partial response, we need prohibit
        // writing any content outside of the view itself (f:view).
        PartialViewContext partialViewContext = facesContext.getPartialViewContext();
        if (partialViewContext.isAjaxRequest()) {
            OnOffResponseWrapper onOffResponse = new OnOffResponseWrapper(facesContext);
            onOffResponse.setEnabled(false); 
        }
        
        try {
            // the before render event on the view root is a special case to keep door open for navigation
            facesContext.getApplication().publishEvent(PreRenderViewEvent.class, facesContext.getViewRoot());
            //render the view
            facesContext.getApplication().getViewHandler().
                 renderView(facesContext, facesContext.getViewRoot());

        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
        }

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "+=+=+=+=+=+= View structure printout for " + facesContext.getViewRoot().getViewId());
            DebugUtil.printTree(facesContext.getViewRoot(), LOGGER, Level.FINEST);
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Exiting RenderResponsePhase");
        }

    }


    public PhaseId getId() {

        return PhaseId.RENDER_RESPONSE;

    }


// The testcase for this class is TestRenderResponsePhase.java

} // end of class RenderResponsePhase
