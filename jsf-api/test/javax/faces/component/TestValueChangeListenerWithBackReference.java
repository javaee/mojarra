/*
 * $Id: TestValueChangeListenerWithBackReference.java,v 1.1 2003/10/27 04:10:08 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.TestUtil;

/**
 * <p>Test {@link ValueChangeListener} implementation.</p>
 */

public class TestValueChangeListenerWithBackReference extends TestValueChangeListener implements StateHolder {

    // ------------------------------------------------------------ Constructors

    /**
     *
     * Called from state system.
     */
    public TestValueChangeListenerWithBackReference() {
    }


    public TestValueChangeListenerWithBackReference(String id, 
						     PhaseId phaseId, 
						     UIComponent yourComponent) {
	super(id, phaseId);
	this.yourComponent = yourComponent;
    }


    public TestValueChangeListenerWithBackReference(String id) {
        super(id, PhaseId.ANY_PHASE);
    }

    private UIComponent yourComponent = null;


    // this needs to be named differently because other test methods
    // rely on the standard equal method.
    public boolean isEqual(Object otherObj) {
	if (!(otherObj instanceof TestValueChangeListenerWithBackReference)) {
	    return false;
	}
	TestValueChangeListenerWithBackReference other = 
	    (TestValueChangeListenerWithBackReference) otherObj;
	
	boolean 
	    superIsEqual = super.isEqual(otherObj),
	    yourComponentsIdsAreEqual = false;
	if (null == yourComponent && null == other.yourComponent) {
	    yourComponentsIdsAreEqual = true;
	}
	else if (null != yourComponent && null != other.yourComponent) {
	    yourComponentsIdsAreEqual = 
		TestUtil.equalsWithNulls(yourComponent.getId(),
					 other.yourComponent.getId());
	}
	
	boolean result = superIsEqual && yourComponentsIdsAreEqual;
	return result;
    }

    //
    // methods from StateHolder
    //

    public void setComponent(UIComponent yourComponent) {
	this.yourComponent = yourComponent;
    }



}
