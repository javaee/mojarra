/*
 * $Id: ObjectManagerFactoryImpl.java,v 1.2 2002/01/18 21:52:29 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ObjectManagerFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.ObjectManager;
import javax.faces.FacesException;

import javax.servlet.ServletContext;

/**
 *
 *  <B>ObjectManagerFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ObjectManagerFactoryImpl.java,v 1.2 2002/01/18 21:52:29 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ObjectManagerFactoryImpl extends ObjectManagerFactory
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
// Constructors and Initializers    
//

public ObjectManagerFactoryImpl()
{
    super();
}

//
// Methods from ObjectManagerFactory
//

//
// Class methods
//

public ObjectManager newObjectManager(ServletContext servletContext) throws FacesException {
    ObjectManager result = new ObjectManagerImpl(servletContext);
    return result;
}

//
// General Methods
//

} // end of class ObjectManagerFactoryImpl
