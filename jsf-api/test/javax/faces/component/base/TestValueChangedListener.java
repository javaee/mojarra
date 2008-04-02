/*
 * $Id: TestValueChangedListener.java,v 1.2 2003/07/26 17:55:21 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import javax.faces.event.PhaseId;


/**
 * <p>Test {@link ValueChangedListener} implementation.</p>
 */

public class TestValueChangedListener implements ValueChangedListener {

    // ------------------------------------------------------------ Constructors


    public TestValueChangedListener(String id, PhaseId phaseId) {
        this.id = id;
        this.phaseId = phaseId;
    }


    public TestValueChangedListener(String id) {
        this(id, PhaseId.ANY_PHASE);
    }


    private String id = null;
    private PhaseId phaseId = null;


    // ----------------------------------------------------------- Pubic Methods


    public String getId() {
        return (this.id);
    }

    public PhaseId getPhaseId() {
        return (this.phaseId);
    }

    public void processValueChanged(ValueChangedEvent event) {
        trace(getId());
    }


    // ---------------------------------------------------- Static Trace Methods


    // Accumulated trace log
    private static StringBuffer trace = new StringBuffer();

    // Append to the current trace log (or clear if null)
    public static void trace(String text) {
        if (text == null) {
            trace.setLength(0);
        } else {
            trace.append('/');
            trace.append(text);
        }
    }

    // Retrieve the current trace log
    public static String trace() {
        return (trace.toString());
    }


}
