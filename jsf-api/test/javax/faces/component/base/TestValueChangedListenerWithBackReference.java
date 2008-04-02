/*
 * $Id: TestValueChangedListenerWithBackReference.java,v 1.2 2003/09/22 19:03:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.TestUtil;

/**
 * <p>Test {@link ValueChangedListener} implementation.</p>
 */

public class TestValueChangedListenerWithBackReference extends TestValueChangedListener implements StateHolder {

    // ------------------------------------------------------------ Constructors

    /**
     *
     * Called from state system.
     */
    public TestValueChangedListenerWithBackReference() {
    }


    public TestValueChangedListenerWithBackReference(String id, 
						     PhaseId phaseId, 
						     UIComponent yourComponent) {
	super(id, phaseId);
	this.yourComponent = yourComponent;
    }


    public TestValueChangedListenerWithBackReference(String id) {
        super(id, PhaseId.ANY_PHASE);
    }

    private UIComponent yourComponent = null;


    // this needs to be named differently because other test methods
    // rely on the standard equal method.
    public boolean isEqual(Object otherObj) {
	if (!(otherObj instanceof TestValueChangedListenerWithBackReference)) {
	    return false;
	}
	TestValueChangedListenerWithBackReference other = 
	    (TestValueChangedListenerWithBackReference) otherObj;
	
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
