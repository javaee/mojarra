/*
 * $Id: MyPhaseListener.java,v 1.3 2004/02/05 16:25:05 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;


import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;


/**
 * <p>Debugging implementation of <code>PhaseListener</code>.</p>
 */

public class MyPhaseListener implements PhaseListener {


    public PhaseId getPhaseId() {
        return (PhaseId.ANY_PHASE);
    }


    public void afterPhase(PhaseEvent event) {
        System.out.println("afterPhase(" + event.getPhaseId() + ")");
    }


    public void beforePhase(PhaseEvent event) {
        System.out.println("beforePhase(" + event.getPhaseId() + ")");
    }


}
