/*
 * $Id: TestListener.java,v 1.5 2004/01/22 22:19:44 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesListener;

public class TestListener implements FacesListener {


    public TestListener(String id,
                        String fromId, String toId) {
        this.id = id;
        this.fromId = fromId; // When an event with this id is received ...
        this.toId = toId;     // queue an additional event with this id
    }


    public TestListener(String id) {
        this.id = id;
    }

    public TestListener(String id, boolean abort) {
        this.id = id;
        this.abort = abort;
    }


    private boolean abort = false;
    private String fromId = null;
    private String id = null;
    private String toId = null;


    public String getId() {
        return (this.id);
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
        if (abort) {
            throw new AbortProcessingException();
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
