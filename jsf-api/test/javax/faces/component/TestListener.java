/*
 * $Id: TestListener.java,v 1.1 2003/09/25 07:46:05 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;


public class TestListener implements FacesListener {


    public TestListener(String id, PhaseId phaseId) {
        this.id = id;
        this.phaseId = phaseId;
    }


    public TestListener(String id) {
        this(id, PhaseId.ANY_PHASE);
    }


    private String id = null;
    private PhaseId phaseId = null;


    public String getId() {
        return (this.id);
    }

    public PhaseId getPhaseId() {
        return (this.phaseId);
    }

    public void processTest(TestEvent event) {
        trace(getId());
    }


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
