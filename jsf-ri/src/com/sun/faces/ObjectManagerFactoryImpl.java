/*
 * $Id: ObjectManagerFactoryImpl.java,v 1.3 2002/04/11 22:52:40 eburns Exp $
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
import javax.faces.FacesFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.util.Map;

/**
 *
 *  <B>ObjectManagerFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ObjectManagerFactoryImpl.java,v 1.3 2002/04/11 22:52:40 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ObjectManagerFactoryImpl extends Object implements FacesFactory
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
// Class methods
//

public ObjectManager newObjectManager(ServletContext servletContext) throws FacesException {
    ObjectManager result = new ObjectManagerImpl(servletContext);
    return result;
}

public Object newInstance(String facesName, ServletRequest req, ServletResponse res) throws FacesException {
    throw new FacesException("Can't create ObjectManager instance from request and response");
}

public Object newInstance(String facesName, ServletContext ctx) throws FacesException {
    return new ObjectManagerImpl(ctx);
}

public Object newInstance(String facesName) throws FacesException {
    throw new FacesException("Can't create ObjectManager instance from nothing.");
}

public Object newInstance(String facesName, Map args) throws FacesException {
    throw new FacesException("Can't create ObjectManager instance from Map.");
}



//
// General Methods
//

} // end of class ObjectManagerFactoryImpl
