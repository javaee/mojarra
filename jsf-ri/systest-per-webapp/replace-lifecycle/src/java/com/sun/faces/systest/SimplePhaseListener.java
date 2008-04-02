/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.systest;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SimplePhaseListener implements PhaseListener {

    public SimplePhaseListener() {
    }


    public void afterPhase(PhaseEvent event) {
	event.getFacesContext().getExternalContext().getRequestMap().put("afterPhase",
									 "afterPhase");
    }


    public void beforePhase(PhaseEvent event) {
	event.getFacesContext().getExternalContext().getRequestMap().put("beforePhase",
									 "beforePhase");
    }


    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
