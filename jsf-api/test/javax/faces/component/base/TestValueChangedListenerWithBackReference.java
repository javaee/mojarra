/*
 * $Id: TestValueChangedListenerWithBackReference.java,v 1.1 2003/09/18 01:21:15 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.component.StateHolderWithBackReference;
import javax.faces.component.UIComponent;
import javax.faces.TestUtil;

/**
 * <p>Test {@link ValueChangedListener} implementation.</p>
 */

public class TestValueChangedListenerWithBackReference extends TestValueChangedListener implements StateHolderWithBackReference {

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

    public void restoreState(FacesContext context, Object state, 
			     UIComponent toAttachTo) {
	restoreState(context, state);
	
	if (null != toAttachTo) {
	    yourComponent = (UIComponent) toAttachTo;
	}
    }
}
