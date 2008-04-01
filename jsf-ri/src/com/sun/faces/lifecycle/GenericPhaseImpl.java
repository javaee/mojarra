/*
 * $Id: GenericPhaseImpl.java,v 1.1 2002/05/28 20:52:01 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// GenericPhaseImpl.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.PhaseListener;
import javax.faces.context.FacesContext;

import java.util.ArrayList;

/**

 *  <B>GenericPhaseImpl</B> is the base class for Phase implementations
 *  in the JSF RI. <P>

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: GenericPhaseImpl.java,v 1.1 2002/05/28 20:52:01 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Phase
 *
 */

public class GenericPhaseImpl extends Phase
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

protected int id = -1;

// Relationship Instance Variables

protected Lifecycle lifecycleDriver = null;

protected Object lock = null;

protected ArrayList beforePhases = null;
protected ArrayList afterPhases = null;

//
// Constructors and Genericializers    
//

public GenericPhaseImpl(Lifecycle newDriver, int newId)
{
    super();
    ParameterCheck.withinRange(newId, LifecycleFactoryImpl.FIRST_PHASE, 
			       LifecycleFactoryImpl.LAST_PHASE);

    lifecycleDriver = newDriver;
    id = newId;
    lock = new Object();
}

//
// Class methods
//

//
// General Methods
//

public void setLifecycle(Lifecycle newDriver)
{
    ParameterCheck.nonNull(newDriver);
    lifecycleDriver = newDriver;
}

public int getId()
{
    return id;
}

// 
// Methods from Phase
//


/**


*/

public int execute(FacesContext facesContext) throws FacesException
{
    return Phase.GOTO_NEXT;
}


// The testcase for this class is TestLifecycleImpl.java 


} // end of class GenericPhaseImpl
