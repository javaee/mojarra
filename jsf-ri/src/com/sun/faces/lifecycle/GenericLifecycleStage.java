/*
 * $Id: GenericLifecycleStage.java,v 1.1 2002/03/13 18:04:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// GenericLifecycleStage.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesContext;
import javax.faces.RenderContext;
import javax.faces.EventContext;
import javax.faces.TreeNavigator;
import javax.faces.LifecycleStage;
import javax.faces.FacesException;

/**
 *
 *  <B>GenericLifecycleStage</B> 
 *
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * LifecycleDriverImpl.
 *
 * @version $Id: GenericLifecycleStage.java,v 1.1 2002/03/13 18:04:21 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.LifecycleDriverImpl
 *
 */

public class GenericLifecycleStage extends Object implements LifecycleStage
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

protected String name = null;

// Relationship Instance Variables

protected LifecycleDriverImpl lifecycleDriver = null;

//
// Constructors and Genericializers    
//

public GenericLifecycleStage(LifecycleDriverImpl newDriver, String newName)
{
    super();
    ParameterCheck.nonNull(newDriver);
    ParameterCheck.nonNull(newName);

    lifecycleDriver = newDriver;
    name = newName;
}

//
// Class methods
//

//
// General Methods
//

// 
// Methods from LifecycleStage
//

public String getName()
{
    return name;
}

/**

* This implementation of execute simply calls
* lifecycleDriver.traverseTreeInvokingMethod().

*/

public boolean execute(FacesContext ctx, TreeNavigator root) throws FacesException
{
    return lifecycleDriver.traverseTreeInvokingMethod(ctx, root, getName());
}


// Delete this text and replace the below text with the name of the file
// containing the testcase covering this class.  If this text is here
// someone touching this file is poor software engineer.

// The testcase for this class is TestGenericLifecycleStage.java 


} // end of class GenericLifecycleStage
