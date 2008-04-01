/*
 * $Id: UpdateModelValuesPhase.java,v 1.2 2002/06/07 00:01:03 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// UpdateModelValuesPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import java.util.Iterator;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: UpdateModelValuesPhase.java,v 1.2 2002/06/07 00:01:03 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#UPDATE_MODEL_VALUES_PHASE
 *
 */

public class UpdateModelValuesPhase extends GenericPhaseImpl
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

protected LifecycleCallback pushValues = null;
protected LifecycleCallback clearValues = null;

//
// Constructors and Genericializers    
//

public UpdateModelValuesPhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId);
    pushValues = new LifecycleCallback() {
	    public int takeActionOnComponent(FacesContext facesContext,
					     UIComponent comp) throws FacesException {
		int rc = Phase.GOTO_NEXT;
		String model = null,
		    message = null;
		
		if (null != (model = comp.getModel())) {
		    try {
			facesContext.setModelValue(model, comp.getValue());
		    }
		    catch (Throwable e) {
			message = "Exception caught during setModelValue for " +
			    comp.getCompoundId() + "\nThrowable " +
			    e.toString() + ": " + e.getMessage();
			facesContext.getMessageList().add(message,
							  comp.getCompoundId(),
							  null);
			rc = Phase.GOTO_RENDER;
		    }
		}
		
		return rc;
	    }
	};
    clearValues = new LifecycleCallback() {
	    public int takeActionOnComponent(FacesContext facesContext,
					     UIComponent comp) throws FacesException {
		int rc = Phase.GOTO_NEXT;
		String model = null,
		    message = null;

		if (null != (model = comp.getModel())) {
		    comp.setValue(null);
		}
		return rc;
	    }
	};
}

//
// Class methods
//

//
// General Methods
//

//
// Methods from Phase
//

public int execute(FacesContext facesContext) throws FacesException
{
    int rc = Phase.GOTO_NEXT;
    callback = pushValues;
    rc = traverseTreeInvokingCallback(facesContext);
    if (Phase.GOTO_NEXT == rc) {
	callback = clearValues;
	rc = traverseTreeInvokingCallback(facesContext);
    }
    return rc;
}



// The testcase for this class is TestUpdateModelValuesPhase.java


} // end of class UpdateModelValuesPhase
