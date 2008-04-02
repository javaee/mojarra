/*
 * $Id: TestCommandActionListener.java,v 1.1 2003/10/27 04:10:05 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;


/**
 * <p>Test implementation of {@link ActionListener}.</p>
 */

public class TestCommandActionListener implements ActionListener {

    protected String actionListenerId = null;
 
    public TestCommandActionListener(String actionListenerId) {
	this.actionListenerId = actionListenerId;
    }
    
    public void processAction(ActionEvent event) {
        trace(actionListenerId);
    }


    public PhaseId getPhaseId() {
        return (PhaseId.APPLY_REQUEST_VALUES);
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
