/*
 * $Id: RenderResponsePhase.java,v 1.24 2007/04/25 04:07:01 rlubke Exp $
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

// RenderResponsePhase.java

package com.sun.faces.lifecycle;


import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.RIConstants;
import com.sun.faces.util.TypedCollections;
import com.sun.faces.util.FacesLogger;


/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RenderResponsePhase.java,v 1.24 2007/04/25 04:07:01 rlubke Exp $
 */

public class RenderResponsePhase extends Phase {


    // Log instance for this class
    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();


    public RenderResponsePhase() {
        super();
    }


    public PhaseId getId() {
        return PhaseId.RENDER_RESPONSE;
    }


    public void execute(FacesContext facesContext) throws FacesException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Entering RenderResponsePhase");
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("About to render view " +
                 facesContext.getViewRoot().getViewId());
        }
        try {
            Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

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
                    requestMap.put(RIConstants.CLIENT_ID_MESSAGES_NOT_DISPLAYED, clientIds);
                }
            }

            //render the view
            facesContext.getApplication().getViewHandler().
                 renderView(facesContext, facesContext.getViewRoot());

            //display results of message display LOGGER
            if (LOGGER.isLoggable(Level.INFO) &&
                 requestMap.containsKey(RIConstants.CLIENT_ID_MESSAGES_NOT_DISPLAYED)) {

                //remove so Set does not get modified when displaying messages.
                Set<String> clientIds = TypedCollections.dynamicallyCastSet(
                     (Set) requestMap.remove(RIConstants.CLIENT_ID_MESSAGES_NOT_DISPLAYED), String.class);
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

// The testcase for this class is TestRenderResponsePhase.java


} // end of class RenderResponsePhase
