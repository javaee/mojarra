/*
 * $Id: TestDataValueChangeListener.java,v 1.4 2004/02/26 20:31:28 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.context.FacesContext;


/**
 * <p>Test {@link ValueChangeListener} implementation.</p>
 */

public class TestDataValueChangeListener implements ValueChangeListener {


    // ------------------------------------------------------------ Constructors


    public TestDataValueChangeListener() {
    }


    // ---------------------------------------------------------- Public Methods


    public void processValueChange(ValueChangeEvent event) {
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
