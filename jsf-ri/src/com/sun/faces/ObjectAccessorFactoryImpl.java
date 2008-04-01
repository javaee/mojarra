/*
 * $Id: ObjectAccessorFactoryImpl.java,v 1.2 2002/04/05 19:41:13 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ObjectAccessorFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.ObjectAccessor;
import javax.faces.BeanAccessor;
import javax.faces.FacesContext;
import javax.faces.FacesException;

/**
 *
 *  <B>ObjectAccessorFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ObjectAccessorFactoryImpl.java,v 1.2 2002/04/05 19:41:13 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ObjectAccessorFactoryImpl extends ObjectAccessorFactory
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

public ObjectAccessorFactoryImpl()
{
    super();
}

//
// Methods from ObjectAccessorFactory
//

//
// Class methods
//

public ObjectAccessor newObjectAccessor(FacesContext fc) throws FacesException {
    ObjectAccessor result = new BeanAccessor(fc);
    return result;
}

//
// General Methods
//

} // end of class ObjectAccessorFactoryImpl
