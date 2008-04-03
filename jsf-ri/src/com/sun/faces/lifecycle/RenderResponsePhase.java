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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.TypedCollections;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.RequestStateManager;


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
        try {
            //Setup message display LOGGER.
            if (LOGGER.isLoggable(Level.INFO)) {
                Iterator<String> clientIdIter = facesContext.getClientIdsWithMessages();

                //If Messages are queued
                if (clientIdIter.hasNext()) {
                    Set<String> clientIds = new HashSet<String>();

                    //Copy client ids to set of clientIds pending display.
                    while (clientIdIter.hasNext()) {
                        clientIds.add(clientIdIter.next());
                    }
                    RequestStateManager.set(facesContext,
                                            RequestStateManager.CLIENT_ID_MESSAGES_NOT_DISPLAYED,
                                            clientIds);
                }
            }

            //render the view
            facesContext.getApplication().getViewHandler().
                 renderView(facesContext, facesContext.getViewRoot());

            //display results of message display LOGGER
            if (LOGGER.isLoggable(Level.INFO) &&
                 RequestStateManager.containsKey(facesContext,
                                                 RequestStateManager.CLIENT_ID_MESSAGES_NOT_DISPLAYED)) {

                //remove so Set does not get modified when displaying messages.
                Set<String> clientIds = TypedCollections.dynamicallyCastSet(
                     (Set) RequestStateManager.remove(facesContext,
                                                      RequestStateManager.CLIENT_ID_MESSAGES_NOT_DISPLAYED),
                     String.class);
                if (!clientIds.isEmpty()) {

                    //Display each message possibly not displayed.
                    StringBuilder builder = new StringBuilder();
                    for (String clientId : clientIds) {
                        Iterator<FacesMessage> messages = facesContext.getMessages(clientId);
                        while (messages.hasNext()) {
                            FacesMessage message = messages.next();
                            builder.append("\n");
                            builder.append("sourceId=").append(clientId);
                            builder.append("[severity=(").append(message.getSeverity());
                            builder.append("), summary=(").append(message.getSummary());
                            builder.append("), detail=(").append(message.getDetail()).append(")]");
                        }
                    }
                    LOGGER.log(Level.INFO, "jsf.non_displayed_message", builder.toString());
                }
            }
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
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
