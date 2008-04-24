/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.systest.model;

import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

/**
 *
 * @author edburns
 */
public class RequestScopedManagedBeanWithPhaseListenerMethod {

    public void afterPhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) {
            FacesContext context = event.getFacesContext();

            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

            requestMap.put("afterRestoreView", "phaseListener for RestoreView was called");
        }
    }
    
    

}
