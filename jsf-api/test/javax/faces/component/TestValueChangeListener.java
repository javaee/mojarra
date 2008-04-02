/*
 * $Id: TestValueChangeListener.java,v 1.1 2003/10/27 04:10:08 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;

/**
 * <p>Test {@link ValueChangeListener} implementation.</p>
 */

public class TestValueChangeListener implements ValueChangeListener, StateHolder {

    // ------------------------------------------------------------ Constructors

    /**
     *
     * Called from state system.
     */
    public TestValueChangeListener() {
    }


    public TestValueChangeListener(String id, PhaseId phaseId) {
        this.id = id;
        this.phaseId = phaseId;
    }


    public TestValueChangeListener(String id) {
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

    public void processValueChange(ValueChangeEvent event) {
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
	if (!(otherObj instanceof TestValueChangeListener)) {
	    return false;
	}
	TestValueChangeListener other = (TestValueChangeListener) otherObj;
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

    public Object saveState(FacesContext context) {
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
