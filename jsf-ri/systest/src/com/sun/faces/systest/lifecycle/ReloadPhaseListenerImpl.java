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

package com.sun.faces.systest.lifecycle;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;


/**
 * <B>ReloadPhaseListener</B> is a class that looks for a Restore or Render
 * phase. If it finds that a phase has been entered other than a Restore or
 * Render, it sets a system property to false.
 * <p/>
 * This listener is used to determine whether a client refresh with no
 * request parameters or save state has occurred.
 *
 * @version $Id: ReloadPhaseListenerImpl.java,v 1.7 2006/03/29 22:38:51 rlubke Exp $
 */
public class ReloadPhaseListenerImpl implements PhaseListener {


    PhaseId phaseId = null;
    String pageRefresh;

    // ------------------------------------------------------------ Constructors


    public ReloadPhaseListenerImpl(PhaseId newPhaseId) {

        phaseId = newPhaseId;
        pageRefresh = "true";

    }

    // ---------------------------------------------- Methods From PhaseListener


    public void afterPhase(PhaseEvent event) {

        System.setProperty("PageRefreshPhases", pageRefresh);

    }


    public void beforePhase(PhaseEvent event) {

        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) {
            //reset System property to true when starting phase processing
            pageRefresh = "true";
            return;
        } else if (event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            //no other phases should be called
            return;
        }

        //phase other than Restore or Render is called
        pageRefresh = "false";

    }


    public PhaseId getPhaseId() {

        return phaseId;

    }

} // end of class ReloadPhaseListenerImpl

