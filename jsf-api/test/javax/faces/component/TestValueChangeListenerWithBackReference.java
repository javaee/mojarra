/*
 * $Id: TestValueChangeListenerWithBackReference.java,v 1.5 2005/08/22 22:08:17 ofung Exp $
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


import javax.faces.context.FacesContext;
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
						     UIComponent yourComponent) {
	super(id);
	this.yourComponent = yourComponent;
    }


    public TestValueChangeListenerWithBackReference(String id) {
        super(id);
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
