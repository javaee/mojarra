/*
 * $Id: TestActionListener.java,v 1.6 2003/09/24 22:41:11 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UIComponent;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;


/**
 * <p>Test {@link ActionListener} implementation.</p>
 */

public class TestActionListener implements ActionListener, StateHolder {

    public TestActionListener() {
	phaseId = PhaseId.ANY_PHASE;
    }

    public TestActionListener(String id, PhaseId phaseId) {
        this.id = id;
        this.phaseId = phaseId;
    }


    public TestActionListener(String id) {
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

    public void processAction(ActionEvent event) {
        trace(getId());
    }

    public boolean equals(Object otherObj) {
	if (!(otherObj instanceof TestActionListener)) {
	    return false;
	}
	TestActionListener other = (TestActionListener) otherObj;
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
