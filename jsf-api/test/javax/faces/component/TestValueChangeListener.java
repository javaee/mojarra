/*
 * $Id: TestValueChangeListener.java,v 1.3 2004/02/04 23:38:44 ofung Exp $
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

public class TestValueChangeListener implements ValueChangeListener, StateHolder {

    // ------------------------------------------------------------ Constructors

    /**
     *
     * Called from state system.
     */
    public TestValueChangeListener() {
    }


    public TestValueChangeListener(String id) {
        this.id = id;
    }

    private String id = null;


    // ----------------------------------------------------------- Pubic Methods


    public String getId() {
        return (this.id);
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
	return idsAreEqual;
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
