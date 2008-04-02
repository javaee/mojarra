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


package com.sun.faces.config;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SimplePhaseListener implements PhaseListener {


    private static final String HANDLED_BEFORE_AFTER = "Handled Before After";
    private boolean handledAfter = false;

    private boolean handledBefore = false;


    // ------------------------------------------------------------ Constructors


    public SimplePhaseListener() {
    }


    // ---------------------------------------------- Methods From PhaseListener


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
