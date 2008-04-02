/*
 * $Id: UpdateModelValuesPhase.java,v 1.16 2003/01/21 23:23:16 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// UpdateModelValuesPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.context.FacesContextImpl;

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
 * @version $Id: UpdateModelValuesPhase.java,v 1.16 2003/01/21 23:23:16 rkitain Exp $
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


//
// Constructors and Genericializers    
//

public UpdateModelValuesPhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId);
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

    UIComponent component = 
        (UIComponent)facesContext.getTree().getRoot();
    Assert.assert_it(null != component);

    try {
        component.processUpdates(facesContext);
    } catch (Throwable e) {
        Object[] params = new Object[3];
        params[0] = component.getValue();
        params[1] = component.getModelReference();
        params[2] = e.getMessage();
        MessageResources resources = Util.getMessageResources();
        Assert.assert_it( resources != null );
        Message msg = resources.getMessage(facesContext,
            Util.MODEL_UPDATE_ERROR_MESSAGE_ID,params);
        facesContext.addMessage(component, msg);
    }

    if (((FacesContextImpl)facesContext).getResponseComplete()) {
        return Phase.GOTO_EXIT;
    } else if (((FacesContextImpl)facesContext).getRenderResponse()) {
        return Phase.GOTO_RENDER;
    }

    messageIter = facesContext.getMessages();
    Assert.assert_it(null != messageIter);

    if (messageIter.hasNext()) {
        // Proceed based on the number of errors present
        rc = Phase.GOTO_RENDER;
    }

    return rc;
}



// The testcase for this class is TestUpdateModelValuesPhase.java


} // end of class UpdateModelValuesPhase
