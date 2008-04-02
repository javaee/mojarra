/*
 * $Id: TestListener.java,v 1.9 2005/08/22 22:08:17 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
