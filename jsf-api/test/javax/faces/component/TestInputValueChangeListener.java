/*
 * $Id: TestInputValueChangeListener.java,v 1.1 2003/10/27 04:10:07 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;


/**
 * <p>Test implementation of {@link ValueChangeListener}.</p>
 */

public class TestInputValueChangeListener implements ValueChangeListener {

    protected String valueChangeListenerId = null;
 
    public TestInputValueChangeListener(String valueChangeListenerId) {
	this.valueChangeListenerId = valueChangeListenerId;
    }
    
    public void processValueChange(ValueChangeEvent event) {
        trace(valueChangeListenerId);
    }


    public PhaseId getPhaseId() {
        return (PhaseId.ANY_PHASE);
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
