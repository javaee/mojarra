/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.config;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SimplePhaseListener implements PhaseListener {

    private static final String HANDLED_BEFORE_AFTER = "Handled Before After";

    private boolean handledBefore = false;
    private boolean handledAfter = false;


    public SimplePhaseListener() {
    }


    public void afterPhase(PhaseEvent event) {
        handledAfter = true;
        if (handledBefore && handledAfter) {
            System.setProperty(HANDLED_BEFORE_AFTER, HANDLED_BEFORE_AFTER);
        }
    }


    public void beforePhase(PhaseEvent event) {
        handledBefore = true;
    }


    public PhaseId getPhaseId() {
        return PhaseId.APPLY_REQUEST_VALUES;
    }
}
