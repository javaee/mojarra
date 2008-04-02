/*
 * $Id: PrintEventToRequestMapPhaseListener.java,v 1.3 2006/03/29 22:38:52 rlubke Exp $
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

package com.sun.faces.systest.model;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class PrintEventToRequestMapPhaseListener extends Object
      implements PhaseListener {


    protected PhaseListener otherListener = null;

    private String phaseIdString = null;

    // ---------------------------------------------- Methods From PhaseListener

    public void afterPhase(PhaseEvent event) {

        String attr = (String) event.getFacesContext().getExternalContext()
              .getRequestMap().get("afterPhaseEvent");
        if (null == attr) {
            attr = "";
        }
        event.getFacesContext().getExternalContext().getRequestMap().put(
              "afterPhaseEvent",
              attr + " afterPhase: " + event.getPhaseId());

    }


    public void beforePhase(PhaseEvent event) {

        String attr = (String) event.getFacesContext().getExternalContext()
              .getRequestMap().get("beforePhaseEvent");
        if (null == attr) {
            attr = "";
        }
        event.getFacesContext().getExternalContext().getRequestMap().put(
              "beforePhaseEvent",
              attr + " beforePhase: " + event.getPhaseId());

    }


    /**
     * <p>Look at our phaseIdString ivar.  If non-null, use this as the
     * phaseId for the substring search below.  If null, look in the
     * request map for an attribute called "phaseId".  If not found,
     * return <code>PhaseId.ANY_PHASE</code>.  If found, see if it is a
     * substring of any of the known phases, if so, return the
     * corresponding phase Id.  If not return
     * <code>PhaseId.ANY_PHASE</code>.</p>
     */

    public PhaseId getPhaseId() {

        String phaseId =
              (null != phaseIdString) ? phaseIdString : (String)
                    FacesContext.getCurrentInstance().getExternalContext()
                          .getRequestMap().get("phaseId");
        PhaseId result = PhaseId.ANY_PHASE;
        if (null == phaseId) {
            return result;
        }
        if (-1 != PhaseId.ANY_PHASE.toString().indexOf(phaseId)) {
            result = PhaseId.ANY_PHASE;
        } else
        if (-1 != PhaseId.APPLY_REQUEST_VALUES.toString().indexOf(phaseId)) {
            result = PhaseId.APPLY_REQUEST_VALUES;
        } else
        if (-1 != PhaseId.PROCESS_VALIDATIONS.toString().indexOf(phaseId)) {
            result = PhaseId.PROCESS_VALIDATIONS;
        } else
        if (-1 != PhaseId.UPDATE_MODEL_VALUES.toString().indexOf(phaseId)) {
            result = PhaseId.UPDATE_MODEL_VALUES;
        } else
        if (-1 != PhaseId.INVOKE_APPLICATION.toString().indexOf(phaseId)) {
            result = PhaseId.INVOKE_APPLICATION;
        }
        return result;

    }

    // ---------------------------------------------------------- Public Methods


    public PhaseListener getOtherListener() {

        return otherListener;

    }


    public void setOtherListener(PhaseListener newOtherListener) {

        otherListener = newOtherListener;

    }


    public void setPhaseIdString(String phaseIdString) {

        this.phaseIdString = phaseIdString;

    }


    public PhaseListener getInstance() {

        return this;

    }

}
