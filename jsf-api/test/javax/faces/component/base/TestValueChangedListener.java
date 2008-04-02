/*
 * $Id: TestValueChangedListener.java,v 1.3 2003/07/28 22:22:28 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

/**
 * <p>Test {@link ValueChangedListener} implementation.</p>
 */

public class TestValueChangedListener implements ValueChangedListener, StateHolder {

    // ------------------------------------------------------------ Constructors

    /**
     *
     * Called from state system.
     */
    public TestValueChangedListener() {
    }


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

    // this needs to be named differently because other test methods
    // rely on the standard equal method.
    public boolean isEqual(Object otherObj) {
	if (!(otherObj instanceof TestValueChangedListener)) {
	    return false;
	}
	TestValueChangedListener other = (TestValueChangedListener) otherObj;
	if ((null != id && null == other.id) ||
	    (null == id && null != other.id)) {
	    return false;
	}
	boolean idsAreEqual = true;
	if (null != id) {
	    idsAreEqual = id.equals(other.id);
	}
	boolean result = 
	    idsAreEqual && other.phaseId == this.phaseId;
	return result;
    }

    //
    // methods from StateHolder
    //

    public static final String SEP = "[sep]";

    public Object getState(FacesContext context) {
	return id + SEP + phaseId.getOrdinal();
    }

    public void restoreState(FacesContext context, Object state) {
	String stateStr = (String) state;
	int i = stateStr.indexOf(SEP);
	id = stateStr.substring(0,i);
	phaseId = (PhaseId)
	    PhaseId.VALUES.get(Integer.
			       valueOf(stateStr.
				       substring(i+SEP.length())).intValue());
    }

    public boolean isTransient() { return false;
    }

    public void setTransient(boolean newT) {}



}
