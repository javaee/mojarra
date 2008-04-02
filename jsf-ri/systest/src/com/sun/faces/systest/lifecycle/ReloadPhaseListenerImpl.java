/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.lifecycle;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;



/**
 *
 * <B>ReloadPhaseListener</B> is a class that looks for a Restore or Render
 * phase. If it finds that a phase has been entered other than a Restore or
 * Render, it sets a system property to false.
 *
 * This listener is used to determine whether a client refresh with no
 * request parameters or save state has occurred.
 *
 * @version $Id: ReloadPhaseListenerImpl.java,v 1.2 2004/02/04 23:42:39 ofung Exp $
 *
 */
public class ReloadPhaseListenerImpl implements PhaseListener {
    PhaseId phaseId = null;
    String pageRefresh;

    public ReloadPhaseListenerImpl(PhaseId newPhaseId) { 
        phaseId = newPhaseId;
        pageRefresh = "true";
    }

    public void afterPhase(PhaseEvent event) {
        System.setProperty("PageRefreshPhases", pageRefresh);
    }

    public void beforePhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) {
            //reset System property to true when starting phase processing
            pageRefresh = "true";
            return;
        }
        else if (event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
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

