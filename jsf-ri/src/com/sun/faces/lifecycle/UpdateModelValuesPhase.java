/*
 * $Id: UpdateModelValuesPhase.java,v 1.7 2002/06/25 20:47:59 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// UpdateModelValuesPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.context.MessageResources;
import javax.faces.context.Message;
import com.sun.faces.util.Util;

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
 * @version $Id: UpdateModelValuesPhase.java,v 1.7 2002/06/25 20:47:59 jvisvanathan Exp $
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
		
		if (null != (model = comp.getModelReference())) {
		    try {
			facesContext.setModelValue(model, comp.getValue());
		    }
		    catch (Throwable e) {
                        Object[] params = new Object[3];
                        params[0] = comp.getValue();
                        params[1] = model;
                        params[2] = e.getMessage(); 
			MessageResources resources = Util.getMessageResources();
                        Assert.assert_it( resources != null );
                        Message msg = resources.getMessage(facesContext, 
                                "MSG0005",params);
			facesContext.addMessage(comp, msg);
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

		if (null != (model = comp.getModelReference())) {
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
    Iterator messageIter = null;
    callback = pushValues;
    rc = traverseTreeInvokingCallback(facesContext);

    messageIter = facesContext.getMessagesAll();
    Assert.assert_it(null != messageIter);
    
    if (messageIter.hasNext()) {
	// If we have gotten one or more errors, go to render phase.
	rc = Phase.GOTO_RENDER;
    }
    if (Phase.GOTO_NEXT == rc) {
	callback = clearValues;
	rc = traverseTreeInvokingCallback(facesContext);
    }
    return rc;
}



// The testcase for this class is TestUpdateModelValuesPhase.java


} // end of class UpdateModelValuesPhase
