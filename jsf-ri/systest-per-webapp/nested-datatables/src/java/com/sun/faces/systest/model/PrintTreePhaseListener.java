/*
 * $Id: PrintTreePhaseListener.java,v 1.1 2004/06/16 20:24:25 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseEvent;

import com.sun.faces.util.DebugUtil;
import javax.faces.context.FacesContext;

public class PrintTreePhaseListener extends Object implements PhaseListener {
    
    public void afterPhase(PhaseEvent event) {
	DebugUtil.printTree(FacesContext.getCurrentInstance().getViewRoot(),
			    System.out);
    }


    public void beforePhase(PhaseEvent event) {
	
    }

    public PhaseId getPhaseId() {
	return PhaseId.ANY_PHASE;
    }

}
