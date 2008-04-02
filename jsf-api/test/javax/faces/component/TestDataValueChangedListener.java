/*
 * $Id: TestDataValueChangedListener.java,v 1.1 2003/10/22 23:23:52 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;


/**
 * <p>Test {@link ValueChangedListener} implementation.</p>
 */

public class TestDataValueChangedListener implements ValueChangedListener {


    // ------------------------------------------------------------ Constructors


    public TestDataValueChangedListener() {
    }


    // ---------------------------------------------------------- Public Methods


    public PhaseId getPhaseId() {
        return (PhaseId.ANY_PHASE);
    }


    public void processValueChanged(ValueChangedEvent event) {
        trace(event.getComponent().getClientId
              (FacesContext.getCurrentInstance()));
        Object oldValue = event.getOldValue();
        if (oldValue == null) {
            oldValue = "<<NULL>>";
        }
        trace(oldValue.toString());
        Object newValue = event.getNewValue();
        if (newValue == null) {
            newValue = "<<NULL>>";
        }
        trace(newValue.toString());
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
