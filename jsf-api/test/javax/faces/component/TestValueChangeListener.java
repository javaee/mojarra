/*
 * $Id: TestValueChangeListener.java,v 1.5 2005/08/22 22:08:17 ofung Exp $
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
