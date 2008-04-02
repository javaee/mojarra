/*
 * $Id: InvokeApplicationPhase.java,v 1.10 2003/10/06 18:11:32 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// InvokeApplicationPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.event.FacesEvent;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: InvokeApplicationPhase.java,v 1.10 2003/10/06 18:11:32 eburns Exp $
 * 
 */

public class InvokeApplicationPhase extends Phase {
//
// Protected Constants
//

//
// Class Variables
//

    // Log instance for this class
    protected static Log log = LogFactory.getLog(InvokeApplicationPhase.class);

//
// Instance Variables
//
private Lifecycle lifecycleDriver = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Genericializers    
//

public InvokeApplicationPhase(Lifecycle newDriver) {
    lifecycleDriver = newDriver;
}

public PhaseId getId() {
    return PhaseId.INVOKE_APPLICATION;
}

public void execute(FacesContext facesContext) throws FacesException
{
    UIViewRoot root = facesContext.getViewRoot();
    Assert.assert_it(null != root);
    
    
    try {
	root.processApplication(facesContext);
    } catch (RuntimeException re) {
	String exceptionMessage = re.getMessage();
	if (null != exceptionMessage) {
	    if (log.isErrorEnabled()) {
		log.error(exceptionMessage, re);
	    }
	}
	throw new FacesException(exceptionMessage, re);
    }
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


// The testcase for this class is TestInvokeApplicationPhase.java


} // end of class InvokeApplicationPhase
