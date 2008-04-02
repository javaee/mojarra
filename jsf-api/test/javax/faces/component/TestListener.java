/*
 * $Id: TestListener.java,v 1.4 2003/12/17 15:11:11 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


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
