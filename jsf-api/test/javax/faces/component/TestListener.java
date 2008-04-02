/*
 * $Id: TestListener.java,v 1.3 2003/09/29 22:49:36 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;


public class TestListener implements FacesListener {


    public TestListener(String id, PhaseId phaseId,
                        String fromId, String toId) {
        this.id = id;
        this.phaseId = phaseId;
        this.fromId = fromId; // When an event with this id is received ...
        this.toId = toId;     // queue an additional event with this id
    }


    public TestListener(String id, PhaseId phaseId) {
        this.id = id;
        this.phaseId = phaseId;
    }


    public TestListener(String id) {
        this(id, PhaseId.ANY_PHASE);
    }


    private String fromId = null;
    private String id = null;
    private PhaseId phaseId = null;
    private String toId = null;


    public String getId() {
        return (this.id);
    }

    public PhaseId getPhaseId() {
        return (this.phaseId);
    }

    public void processTest(TestEvent event) {
        if (getId() != null) {
            trace(getId());
        }
        if (event.getId() != null) {
            trace(event.getId());
            if (event.getId().equals(fromId)) {
                event.getComponent().queueEvent
                    (new TestEvent(event.getComponent(), toId));
            }
        }
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
