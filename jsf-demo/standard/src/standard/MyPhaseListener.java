/*
 * $Id: MyPhaseListener.java,v 1.2 2003/10/17 03:53:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
