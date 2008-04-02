/*
 * $Id: TestActionListener.java,v 1.7 2004/02/04 23:38:42 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;


/**
 * <p>Test {@link ActionListener} implementation.</p>
 */

public class TestActionListener implements ActionListener, StateHolder {

    public TestActionListener() {
    }

    public TestActionListener(String id) {
        this.id = id;
    }

    private String id = null;


    // ----------------------------------------------------------- Pubic Methods


    public String getId() {
        return (this.id);
    }

    public void processAction(ActionEvent event) {
        trace(getId() + "@" + event.getPhaseId().toString());
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
	return idsAreEqual;
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

    public Object saveState(FacesContext context) {
	return id;
    }

    public void restoreState(FacesContext context, Object state) {
	id = (String) state;
    }

    public boolean isTransient() { return false;
    }

    public void setTransient(boolean newT) {}

}
