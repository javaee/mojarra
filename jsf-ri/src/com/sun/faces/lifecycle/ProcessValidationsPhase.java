/*
 * $Id: ProcessValidationsPhase.java,v 1.10 2003/01/21 23:23:16 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ProcessValidationsPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.context.FacesContextImpl;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.validator.Validator;

import java.util.Iterator;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: ProcessValidationsPhase.java,v 1.10 2003/01/21 23:23:16 rkitain Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#PROCESS_VALIDATIONS_PHASE
 *
 */

public class ProcessValidationsPhase extends GenericPhaseImpl
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

public ProcessValidationsPhase(Lifecycle newDriver, int newId)
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

    component.processValidators(facesContext);

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


// The testcase for this class is TestProcessValidationsPhase.java


} // end of class ProcessValidationsPhase
