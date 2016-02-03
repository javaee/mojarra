/*
 * $Id: PrintEventToRequestMapPhaseListener.java,v 1.3 2007/04/27 22:02:17 ofung Exp $
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

package com.sun.faces.systest.model;

import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseEvent;

import javax.faces.context.FacesContext;

public class PrintEventToRequestMapPhaseListener extends Object implements PhaseListener {

    public void afterPhase(PhaseEvent event) {
	String attr = (String) event.getFacesContext().getExternalContext().getRequestMap().get("afterPhaseEvent");
	if (null == attr) {
	    attr = "";
	}
	event.getFacesContext().getExternalContext().getRequestMap().put("afterPhaseEvent", attr + " afterPhase: " + event.getPhaseId());
    }
    
    
    public void beforePhase(PhaseEvent event) {
	String attr = (String) event.getFacesContext().getExternalContext().getRequestMap().get("beforePhaseEvent");
	if (null == attr) {
	    attr = "";
	}
	event.getFacesContext().getExternalContext().getRequestMap().put("beforePhaseEvent", attr + " beforePhase: " + event.getPhaseId());
    }

    private String phaseIdString = null;

    public void setPhaseIdString(String phaseIdString) {
	this.phaseIdString = phaseIdString;
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
	    FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("phaseId");
	PhaseId result = PhaseId.ANY_PHASE;
	if (null == phaseId) {
	    return result;
	}
	if (-1 != PhaseId.ANY_PHASE.toString().indexOf(phaseId)) {
	    result = PhaseId.ANY_PHASE;
	}
	else if(-1 !=PhaseId.APPLY_REQUEST_VALUES.toString().indexOf(phaseId)){
	    result = PhaseId.APPLY_REQUEST_VALUES;
	}
	else if(-1 != PhaseId.PROCESS_VALIDATIONS.toString().indexOf(phaseId)){
	    result = PhaseId.PROCESS_VALIDATIONS;
	}
	else if(-1 != PhaseId.UPDATE_MODEL_VALUES.toString().indexOf(phaseId)){
	    result = PhaseId.UPDATE_MODEL_VALUES;
	}
	else if(-1 != PhaseId.INVOKE_APPLICATION.toString().indexOf(phaseId)){
	    result = PhaseId.INVOKE_APPLICATION;
	}
	return result;
    }

    public PhaseListener getInstance() {
	return this;
    }

    protected PhaseListener otherListener = null;
    public PhaseListener getOtherListener() {
	return otherListener;
    }

    public void setOtherListener(PhaseListener newOtherListener) {
	otherListener = newOtherListener;
    }


}
