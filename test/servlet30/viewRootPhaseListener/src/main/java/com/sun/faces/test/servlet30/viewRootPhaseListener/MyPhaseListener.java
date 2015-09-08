package com.sun.faces.test.servlet30.viewRootPhaseListener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author frederick.kaempfer
 */
public class MyPhaseListener implements  PhaseListener{

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        throw new RuntimeException("Thrown from UIViewRoot PhaseListener");
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
    
    
    
}
